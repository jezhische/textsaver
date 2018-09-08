package com.jezh.textsaver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "text_parts")
//@Data // getters, setters, equals, hashCode, toString
@Getter
@Setter
@NoArgsConstructor
@Builder
/* for test purpose */
@AllArgsConstructor
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JsonIgnoreProperties(value = {"creatingDate", "updatingDate"},
        /*to support defining "read-only" properties: */ allowGetters = true)
@EntityListeners(AuditingEntityListener.class)
public class TextPart {

    @Id
// todo: NB: this id generation (strategy = GenerationType.IDENTITY) works only if I let db the primary key incrementation
// by creating table with "id SERIAL PRIMARY KEY"
// The DDL (and DML to initialize tables with data) scripts may be fulfilled automatically from files "schema.sql" and
// "data.sql", and also "schema-${platform}.sql" and "data-${platform}.sql", where "platform" is the value of
// "spring.datasource.platform" (to allow to switch to database-specific scripts if necessary).
// see https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html#howto-initialize-a-database-using-spring-jdbc
    // NB: .AUTO works too
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    /**
     * text part body
     * */
    @Column
    private String body;

    /**
     * link to previous text part
     * */
    @Column(name = "previous_item")
    private Integer previousItem;

    /**
     * link to next text part
     * */
    @Column(name = "next_item")
    private Integer nextItem;

    @ManyToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.REFRESH*/) // default EAGER - it's JPA requirement,
    // since in hibernate all select fetching is LAZY
    @JoinColumn(name = "text_common_data_id")
    private TextCommonData textCommonData;



    /**
    * for enabling to modify application in future to sort text parts by the last modifying date
    * to track the last editions
    */
    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastUpdate;

    /**
     * equals() and hashCode() have to be configured manually for entity purposes
     * */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        return id != null && id.equals(((TextPart) o).id);
    }

// NB: if I use "return (int)(id * 31);", I get NullPointerException when I persist collection of TextPart objects in
// "addTextParts(TextPart...textParts)"
// In other case, if I use something like Objects.hash(body, previousItem, nextItem, textCommonData, lastUpdate)
// with or without using id, I get StackOverflowError with cyclic reference
    @Override
    public int hashCode() {
        return 31;
//        return id != null? id.intValue() : 31;
    }

// to prevent cyclic reference, I must to ovverride toString() in the point "textCommonData.getId()" instead of
// "textCommonData". So I need no @Data, only @Getter and @Setter
    @Override
    public String toString() {
        return "TextPart{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", previousItem=" + previousItem +
                ", nextItem=" + nextItem +
                ", text_common_data_id=" + textCommonData.getId() +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
