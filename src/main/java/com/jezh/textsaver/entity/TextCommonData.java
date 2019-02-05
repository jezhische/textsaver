package com.jezh.textsaver.entity;

import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;


/*
  * need to create separate entity in order not to update this data while AssemblyData updating
*/
@Entity
@Table(name = "text_common_data")
//@Data // getters, setters, equals, hashCode, toString
@Getter
@Setter
// I can to create toString() automatically since I have no more any collection of TextPart
@ToString(callSuper = true)
@NoArgsConstructor
@Builder
@AllArgsConstructor
// https://www.callicoder.com/spring-boot-rest-api-tutorial-with-mysql-jpa-hibernate/
//  "Spring Boot uses Jackson for Serializing and Deserializing Java objects to and from JSON.
//This annotation is used because we don’t want the clients of the rest api to supply the createdDate and updatedDate values.
// If they supply these values then we’ll simply ignore them. However, we’ll include these values in the JSON response."
//@JsonIgnoreProperties(value = {"createdDate", "updatedDate"},
//        /*to support defining "read-only" properties*/ allowGetters = true)
//// for Date fields proper assigning; need @EnableJpaAuditing to activate auditing in the classes marked this annotation
//@EntityListeners(AuditingEntityListener.class)
public class TextCommonData extends AbstractIdentifier {

    // the name of the text
//  @NotBlank - must contain at least one non-whitespace character; for CharSequence
    @NotBlank
    @Column
    private String name;

    // id of text_part that is the first in the sequence
    @Column
    private Long firstItem;

    // created date
    @Column(name = "creating_date")
// only with Date and Calendar: it converts the date and time values from Java Object to compatible database type
// and vice versa.
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Column(name = "updating_date")
// only with Date and Calendar: it converts the date and time values from Java Object to compatible database type
// and vice versa.
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
