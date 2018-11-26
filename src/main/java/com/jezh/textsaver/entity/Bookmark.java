package com.jezh.textsaver.entity;


import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "bookmarks")
////@Data // getters, setters, equals, hashCode, toString
//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//@Builder
//@AllArgsConstructor
public class Bookmark extends AbstractIdentifier {

    private boolean isLastOpen; // это поле помечается "true" при построении массива (листа), который будет отправлен на страницу,
    // у первого в списке элемента, а у остальных помечается "false"

    private boolean isEdited;

    private Integer pageNumber;

    private Long nextBookmarkId;

//    private String color; // поле "цвет" перенести в DTO, а значения хранятся в ENUM - в двух, типа "красный" и "зеленый"
}
