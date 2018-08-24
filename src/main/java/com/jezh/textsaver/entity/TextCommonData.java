package com.jezh.textsaver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;


/*
  * need to create separate entity in order not to update this data while AssemblyData updating
*/
@Entity
@Table(name = "text_common_data")
@Data // getters, setters, equals, hashCode, toString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TextCommonData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    // the name of the text
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    // creating date
    @Column
    private LocalDateTime creatingDate;

    @OneToOne(mappedBy = "textCommonData")
    private AssemblyData assemblyData;
}
