package com.jezh.textsaver.entity;


import com.jezh.textsaver.hybernateType.IntArrayType;
import com.jezh.textsaver.hybernateType.StringArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.util.ClassUtils;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "textCommonData")
@Builder

@Entity
@Table(name = "bookmarks")
// https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
// https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
@TypeDefs({@TypeDef(name = "string-array", typeClass = StringArrayType.class), @TypeDef(name = "int-array", typeClass = IntArrayType.class)})
public class Bookmarks {

    @Setter(value = AccessLevel.PRIVATE)
    @Id
    private Long id;

    @Type(type = "string-array")
    @Column(name = "last_open_array", columnDefinition = "text[]")
    private String[] lastOpenArray;

    @Type(type = "int-array")
    @Column(name = "special_bookmarks", columnDefinition = "integer[]")
    private int[] specialBookmarks;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_common_data_id")
    @MapsId // simply it means that bookmarks.id == textCommonData.id
    private TextCommonData textCommonData;

    @Override
    @SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        else if (!this.getClass().equals(ClassUtils.getUserClass(obj))) return false;
        else {Bookmarks that = (Bookmarks) obj;
        return this.getId() != null && this.getId().equals(that.getId());
        }
    }

    @Override
    public int hashCode() {
        return 17 + (this.getId() == null ? 0 : this.getId().hashCode() * 31);
    }
}
