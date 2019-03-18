package com.jezh.textsaver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @ManyToOne(fetch = FetchType.EAGER, optional = true) // LAZY?
    @JoinColumn(name = "user_id")
    private AppUser user;

    /** if this field exists, hibernate makes request to bookmarks each time when there is request to text_common_data */
//    @JsonIgnore
//    @OneToOne(mappedBy = "textCommonData", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, orphanRemoval = true, optional = false) // optional = false means not null, and
//    // hibernate doesn't need additional request to check for nullable
//    private Bookmarks bookmarks;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }



    //// "You can also use this annotation (by combining it with AccessLevel.NONE) to suppress generating a... setter."
//// "This lets you override the behaviour of a @Getter, @Setter or @Data annotation on a class."
//    @Setter(AccessLevel.NONE)
//    @Column
//// to avoid "fetch = FetchType.EAGER", I need to MAKE TRANSACTION in test methods (e.g. see testAddTextParts in
//// TextCommonDataRepositoryPostgresTest), in other case I have "org.hibernate.LazyInitializationException: failed to lazily
//// initialize a collection of role: com.jezh.textsaver.entity.TextCommonData.textParts, could not initialize proxy - no Session"
//    @OneToMany(mappedBy = "textCommonData", cascade = CascadeType.ALL, orphanRemoval = true
//            /*, fetch = FetchType.LAZY*/) // default LAZY, so I can not define it here
////    @JoinColumn(name = "text_common_data_id")
//    private Set<TextPart> textParts = new HashSet<>();
//
//    public void setTextParts(Set<TextPart> textParts) {
//        this.textParts = new HashSet<>(textParts);
//        textParts.forEach((textPart -> {
//            if (textPart.getTextCommonData() != this) textPart.setTextCommonData(this);
//        }));
//    }
//
//    public void addTextParts(TextPart...textParts) {
//        if (this.textParts == null) setTextParts(new HashSet<>(Arrays.asList(textParts)));
//        for (TextPart textPart : textParts) {
//            if (textPart.getTextCommonData() != this) {
//                this.textParts.add(textPart);
//                textPart.setTextCommonData(this);
//            }
//        }
//    }
//
//    public void removeTextParts(TextPart...textParts) throws ResNotFoundException, SQLWarning {
//        if (this.textParts != null && this.textParts.size() != 0) {
//            for (TextPart textPart : textParts) {
//                if (this.textParts.contains(textPart)) {
//                    this.textParts.remove(textPart);
//                    textPart.setTextCommonData(null);
//                } else throw new ResNotFoundException(
//                        String.format("The textPart entity with id %d is not found", textPart.getId()),
//                        new SQLException());
//            }
//        } else throw new SQLWarning("this textCommonData entity collection of textParts is empty or disrupted");
//    }


}
