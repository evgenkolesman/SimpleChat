package ru.kolesnikov.simplechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccessModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loginuser", unique = true)
    private String login;

    public AccessModel(String login) {
        this.login = login;
    }

}
