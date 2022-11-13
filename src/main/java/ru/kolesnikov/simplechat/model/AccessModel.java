package ru.kolesnikov.simplechat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "access")
@NoArgsConstructor
@Getter
public class AccessModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    public AccessModel(String login) {
        this.login = login;
    }

}
