package com.jezh.textsaver.entity;

import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@Entity
@Table(name = "text_parts")
@Data // getters, setters, equals, hashCode, toString
@NoArgsConstructor
@Builder
/* for test purpose */
@AllArgsConstructor
//@RequiredArgsConstructor
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
// With the "save()" JpaRepository method:
// GenerationType.AUTO brings "could not extract ResultSet... ERROR: relation "hibernate_sequence" does not exist"
// GenerationType.SEQUENCE brings the same "could not extract ResultSet... ERROR: relation "hibernate_sequence" does not exist"
// GenerationType.TABLE brings "error performing isolated work... ERROR: relation "hibernate_sequence" does not exist"
// GenerationType.IDENTITY brings "ERROR: null value in column "id" violates not-null constraint"

// todo: NB: this id generation works only if I let db the primary key incrementation by creating table with "id SERIAL PRIMARY KEY"
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    /* text body */
    @Column
    private String body;

    public TextPart(String body) {
        this.body = body;
    }
}
