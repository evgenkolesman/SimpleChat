package ru.kolesnikov.simplechat.model;

import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;

import javax.persistence.*;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @Column(name = "login")
    private String login;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "photo_path")
    private String photoPath;
    @Column(name = "password")
    private String password;

}
