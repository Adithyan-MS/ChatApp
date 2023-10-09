package com.thinkpalm.ChatApplication.Model;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "message_history")
public class MessageHistoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageModel message;

    @Column
    private String edited_content;

    @ManyToOne
    @JoinColumn(name = "edited_by")
    private UserModel user;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp edited_at;

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

    public String getEdited_content() {
        return edited_content;
    }

    public void setEdited_content(String edited_content) {
        this.edited_content = edited_content;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Timestamp getEdited_at() {
        return edited_at;
    }

    public void setEdited_at(Timestamp edited_at) {
        this.edited_at = edited_at;
    }
}
