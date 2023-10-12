package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="deleted_message")
public class DeletedMessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="message_id")
    private MessageModel message;

    @ManyToOne
    @JoinColumn(name="deleted_by")
    private UserModel user;

    @Column
    private Timestamp deleted_at;

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

    public Timestamp getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Timestamp deleted_at) {
        this.deleted_at = deleted_at;
    }
}
