package com.jezh.textsaver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "text_parts", indexes = {@Index(name = "idx_next_it", columnList = "next_item")})
//@Data // getters, setters, equals, hashCode, toString
@Getter
@Setter
@NoArgsConstructor
@Builder
/* for test purpose */
@AllArgsConstructor
@JsonIgnoreProperties(value = {"lastUpdate"},
        /*to support defining "read-only" properties: */ allowGetters = true)
@EntityListeners(AuditingEntityListener.class)
public class TextPart extends AbstractIdentifier {

//    @Id
//// todo: NB: this id generation (strategy = GenerationType.IDENTITY) works only if I let db the primary key incrementation
//// by creating table with "id SERIAL PRIMARY KEY"
//// The DDL (and DML to initialize tables with data) scripts may be fulfilled automatically from files "schema.sql" and
//// "data.sql", and also "schema-${platform}.sql" and "data-${platform}.sql", where "platform" is the value of
//// "spring.datasource.platform" (to allow to switch to database-specific scripts if necessary).
//// see https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html#howto-initialize-a-database-using-spring-jdbc
//    // NB: .AUTO works too
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    @SequenceGenerator(name = "general_seq", sequenceName = "generalSequenceGenerator")
////    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "general_seq")
//    @Column(nullable = false, unique = true)
//    private Long id;

    /**
     * text part body
     * */
    @Column
    private String body;

    /**
     * link to the next text part. NB though unique = true, this constraint don't spread to null value
     * */
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

//    /**
//     * equals() and hashCode() have to be configured manually for entity purposes
//     * */
//    @Override
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (o.getClass() != getClass()) return false;
//        return id != null && id.equals(((TextPart) o).id);
//    }
//
//// NB: if I use "return (int)(id * 31);", I get NullPointerException when I persist collection of TextPart objects in
//// "addTextParts(TextPart...textParts)"
//// In other case, if I use something like Objects.hash(body, previousItem, nextItem, textCommonData, lastUpdate)
//// with or without using id, I get StackOverflowError with cyclic reference
//    @Override
//    public int hashCode() {
//        return 31;
////        return id != null? id.intValue() : 31;
//    }

//    /** for test purpose only */
//    @Override
//    public void setId(Long id) {super.setId(id);}

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

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
