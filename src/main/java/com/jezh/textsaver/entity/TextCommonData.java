package com.jezh.textsaver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;


/*
  * need to create separate entity in order not to update this data while AssemblyData updating
*/
@Entity
@Table(name = "text_common_data")
@Data // getters, setters, equals, hashCode, toString
@NoArgsConstructor
@Builder
@AllArgsConstructor
// https://www.callicoder.com/spring-boot-rest-api-tutorial-with-mysql-jpa-hibernate/
//  "Spring Boot uses Jackson for Serializing and Deserializing Java objects to and from JSON.
//This annotation is used because we don’t want the clients of the rest api to supply the createdAt and updatedAt values.
// If they supply these values then we’ll simply ignore them. However, we’ll include these values in the JSON response."
@JsonIgnoreProperties(value = {"creatingDate", "updatingDate"},
        /*to support defining "read-only" properties*/ allowGetters = true)
// to Data fields proper assigning
@EntityListeners(AuditingEntityListener.class)
public class TextCommonData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    // the name of the text
//  @NotBlank - must contain at least one non-whitespace character; for CharSequence
    @NotBlank
    @Column(name = "NAME")
    private String name;

    // creating date
    @Column
// only with Date and Calendar: it converts the date and time values from Java Object to compatible database type
// and vice versa.
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date creatingDate;

    @Column
// only with Date and Calendar: it converts the date and time values from Java Object to compatible database type
// and vice versa.
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatingDate;
}
