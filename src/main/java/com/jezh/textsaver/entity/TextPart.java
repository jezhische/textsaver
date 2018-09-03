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
// todo: NB: this id generation (strategy = GenerationType.IDENTITY) works only if I let db the primary key incrementation
// by creating table with "id SERIAL PRIMARY KEY"
// The DDL (and DML to initialize tables with data) scripts may be fulfilled automatically from files "schema.sql" and
// "data.sql", and also "schema-${platform}.sql" and "data-${platform}.sql", where "platform" is the value of
// "spring.datasource.platform" (to allow to switch to database-specific scripts if necessary).
// see https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html#howto-initialize-a-database-using-spring-jdbc
    // NB: .AUTO works too
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    /* text body */
    @Column
    private String body;

    @Column
    private Long previous;

    @Column
    private Long next;

    public TextPart(String body, Long previous, Long next) {
        this.body = body;
        this.previous = previous;
        this.next = next;
    }

    // only to compilate the previously wrote tests
    public TextPart(String body) {
        this.body = body;
    }
}
