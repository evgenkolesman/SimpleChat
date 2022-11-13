package ru.kolesnikov.simplechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "message_body", nullable = false)
    private String messageBody;

    @Column(name = "date_of_message", nullable = false)
    private Instant dateOfMessage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "foreign_key_login")
    private User user;

}
