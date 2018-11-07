package com.jezh.textsaver.dto;

import com.jezh.textsaver.entity.TextCommonData;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextPartDTO implements Serializable {

    private Long id;
    private String body;
    private Long nextItem;
    private TextCommonData textCommonData;
    private Date lastUpdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        return id.equals(((TextPartDTO) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TextPartDTO{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", nextItem=" + nextItem +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
