package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "like_message")
public class LikeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageModel message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Column
    private Timestamp liked_at;

}
