package com.jezh.textsaver.entity;

import com.jezh.textsaver.extension.AbstractIdentifier;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
/* for test purpose */
@AllArgsConstructor
public class AppUser extends AbstractIdentifier {

    @Column(name = "NAME")
    private String username;

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
        return "AppUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
