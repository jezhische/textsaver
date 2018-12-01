package com.jezh.textsaver.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jezh.textsaver.hybernateType.IntArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(exclude = "textCommonData")

@Entity
@Table(name = "bookmarks")
// https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
// https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
@TypeDefs(@TypeDef(name = "int-array", typeClass = IntArrayType.class))
public class Bookmark {

    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    @Id
    private Long id;

    /**
     * Array of the numbers of last open pages as the simplest way to create Bookmark list.
     */
    @Type(type = "int-array")
    @Column(name = "last_open_array", columnDefinition = "integer[]")
    private int[] lastOpenArray;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_common_data_id")
    @MapsId // simply it means that bookmark.id == textCommonData.id
    private TextCommonData textCommonData;

//    private String color; // поле "цвет" перенести в DTO, а значения хранятся в ENUM - в двух, типа "красный" и "зеленый".
// Также в dto 1 WeakedHashMap, а цвет определяется по номеру по порядку - это номер элемента в ENUM, а isEdited определяет,
// из какого ENUM брать цвет


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
