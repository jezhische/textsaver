package com.jezh.textsaver.entity;

import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Role {

    @Id
    private Long id;

    @Column(name = "role")
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        return id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                ", id=" + id +
                '}';
    }
}
