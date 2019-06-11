package com.jezh.textsaver.entity;

import com.jezh.textsaver.entity.extension.AbstractIdentifier;
import lombok.*;
import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "username"))
//@Getter
//@Setter
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AppUser extends AbstractIdentifier {

    @Column(name = "username")
    @NotEmpty(message = "*Please provide your username")
    private String username;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


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
                ", enabled = " + enabled +
                ", roles=" + roles +
                ", id=" + id +
                '}';
    }
}
