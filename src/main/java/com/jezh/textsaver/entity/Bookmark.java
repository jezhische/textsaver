package com.jezh.textsaver.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Bookmark extends AbstractIdentifier {

    /** List of the numbers of last open pages as the simplest way to create Bookmark list. The use of the
     * {@code @ElementCollection} here is justified because the list won't include too much elements (up to 10 or 15) */
    @ElementCollection
    @Column(name = "last_open_list")
    private List<Integer> lastOpenList;

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


}
