package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MessageModel getMessage() {
        return message;
    }

    public void setMessage(MessageModel message) {
        this.message = message;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Timestamp getLiked_at() {
        return liked_at;
    }

    public void setLiked_at(Timestamp liked_at) {
        this.liked_at = liked_at;
    }
}
