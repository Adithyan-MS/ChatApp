package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "starred_message")
public class StarredMessageModel extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageModel message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
