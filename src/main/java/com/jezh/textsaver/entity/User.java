package com.jezh.textsaver.entity;

import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

//@Entity
//@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
/* for test purpose */
@AllArgsConstructor
public class User extends AbstractIdentifier {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
