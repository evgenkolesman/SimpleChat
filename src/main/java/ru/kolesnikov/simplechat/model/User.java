package ru.kolesnikov.simplechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
@Table(name = "users",
        uniqueConstraints= @UniqueConstraint(columnNames={"login"}) ) //TODO need to test
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @Column(name = "login")
    private String login;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "photoPath")
    private String photoPath;
    @Column(name = "password")
    private String password; //maybe redundant
}
