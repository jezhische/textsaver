package com.jezh.textsaver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jezh.textsaver.entity.extension.AbstractIdentifier;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "text_parts", indexes = {@Index(name = "idx_next_it", columnList = "next_item")})
@Getter
@Setter
@NoArgsConstructor
@Builder
// for test purpose
@AllArgsConstructor
@JsonIgnoreProperties(value = {"lastUpdate"},
        /*to support defining "read-only" properties: */ allowGetters = true)
@EntityListeners(AuditingEntityListener.class)
public class TextPart extends AbstractIdentifier {
    /**
     * text part body
     * */
    @Column
    private String body;

    /**
     * pageLink to the next text part. NB though unique = true, this constraint don't spread to null value
     * */
    // todo: consider @JsonIgnore to remove (why I placed here this annotation? the field is used in create() method etc)
    @JsonIgnore
    @Column(name = "next_item", nullable = true, unique = true)
        private Long nextItem;

    /**
     * This field will not be serialized to/from JSON, since there is no need to retrieve it from this entity.
     * The only reason to have this field existing is the request to db to find all the text_parts items
     * in certain order from the selection like the following:
     * <p>
     * FOR r IN SELECT * FROM public.text_parts AS tp WHERE tp.text_common_data_id = this_text_common_data_id LOOP...END LOOP
     * <p>
     * To avoid
     * "InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer..."
     * (JavassistLazyInitializer is a Javassist-based lazy initializer proxy, that handles fetching of the underlying
     * entity for a proxy), I need to disable this field for serialization
     * */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // default FetchType.EAGER - it's JPA requirement,
    // since in hibernate all select fetching is LAZY
    @JoinColumn(name = "text_common_data_id")
        private TextCommonData textCommonData;

    /**
    * for enabling to modify application in future to sort text parts by the last modifying date
    * to track the last editions
    */
    @Column(name = "last_update", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastUpdate;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    // don't use the textCommonData because of its LAZY fetch type
    @Override
    public String toString() {
        return "TextPart{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", nextItem=" + nextItem +
//                ", textCommonDataName=" + textCommonData.getName() +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
