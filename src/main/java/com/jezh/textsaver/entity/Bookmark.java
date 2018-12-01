package com.jezh.textsaver.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "bookmarks", indexes = {@Index(name = "idx_next_bookmark_id", columnList = "next_bookmark_id"),
        @Index(name = "idx_is_last_open", columnList = "is_last_open")})
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Bookmark extends AbstractIdentifier {

    @Column(name = "is_last_open")
    private boolean isLastOpen; // это поле помечается "true" при построении массива (листа), который будет отправлен на страницу,
    // у первого в списке элемента, а у остальных помечается "false"

    @Column(name = "is_edited")
    private boolean isEdited;

    @Column(name = "page_number")
    private Integer pageNumber;

    @JsonIgnore
    @Column(name = "next_bookmark_id", unique = true)
    private Long nextBookmarkId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // default FetchType.EAGER - it's JPA requirement,
    // since in hibernate all select fetching is LAZY
    @JoinColumn(name = "text_common_data_id")
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

    // don't use the textCommonData because of its LAZY fetch type
    @Override
    public String toString() {
        return "Bookmark{" +
                "isLastOpen=" + isLastOpen +
                "isEdited=" + isEdited +
                ", pageNumber=" + pageNumber +
                ", id=" + id +
                ", nextBookmarkId=" + nextBookmarkId +
                '}';
    }
}
