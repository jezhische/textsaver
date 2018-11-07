package com.jezh.textsaver.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TextCommonDataDTO implements Serializable {

    private Long id;
    private String name;
    private Long firstItem;
    private Date creatingDate;
    private Date updatingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        return id.equals(((TextCommonDataDTO) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
