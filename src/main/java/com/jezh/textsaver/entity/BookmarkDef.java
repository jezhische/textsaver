package com.jezh.textsaver.entity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDef {

    private int page_number;

    private boolean is_edited;

    @Override
    public String toString() {
        return page_number + " " + is_edited;
    }
}
