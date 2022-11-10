package ru.kolesnikov.simplechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "messageBody", nullable = false)
    private String messageBody;

    @Column(name = "dateOfMessage", nullable = false)
    private Instant dateOfMessage; //TODO maybe need to change type

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "foreign_key_login")
    private User user;

}
