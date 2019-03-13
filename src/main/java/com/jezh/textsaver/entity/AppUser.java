package com.jezh.textsaver.entity;

import com.jezh.textsaver.entity.extension.AbstractIdentifier;
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

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;


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
                ", id=" + id +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled = " + enabled +
                '}';
    }
}
