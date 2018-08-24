package com.jezh.textsaver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "assembly_data")
@Data // getters, setters, equals, hashCode, toString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AssemblyData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    // the date of the last updating - the date of last request arrived from TextPart for modifying of this field
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "parts_order")
    // LinkedList is faster in add() and remove(), but slower in get() as compared with ArrayList. Since the add()
    // and remove() operations will perform often, I choose LinkedList.
    private List<Long> partsOrder = new LinkedList<>();

    @OneToOne
    @JoinColumn(name = "text_common_data_id")
    // to customize the endpoint (default "textCommonData")
    @RestResource(path = "common-data", rel = "textCommonData")
    private TextCommonData textCommonData;

    @OneToMany(mappedBy = "assemblyData")
    private List<TextPart> textParts;
}
