package com.jezh.textsaver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@Entity
@Table(name = "texts")
@Data // getters, setters, equals, hashCode, toString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TextPart {

    // The unique identifier for the part of text. When I pull the text for reading or redacting,
    // I must to sort all the parts of the text in such order as defined in the ORDER table.
    // если я вставляю текст в середину, то в поле "порядок" или "версия" всавляется id нового кусочка текста, и это поле
    // апдейтится. Д.б. также возможность скачать текущую версию текста в виде файла. И, наверное, заменить
    // отредактированным файлом текущую версию
    // ORDER table is associated with TEXT table by One-To-One relationship

    // в конструкторе этого класса д.б. прописано, чтобы каждый раз, когда создается эта сущность, менялся и объект
    // AssemblyData. Или это д.б. прописано в методе create()
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    // text body
    @Column
    private String body;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "assembly_data_id")
    private AssemblyData assemblyData;
}
