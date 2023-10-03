package com.thinkpalm.ChatApplication.Model;


import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NonNull
    private String content;

    @NonNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sender_id")
    private UserModel sender;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_message_id")
    private MessageModel parent_messageModel;

    @Column
    private Integer like_count;

    @Column
    private Integer is_Starred;

    @Column
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @NonNull
    public UserModel getSender() {
        return sender;
    }

    public void setSender(@NonNull UserModel sender) {
        this.sender = sender;
    }

    public MessageModel getParent_message() {
        return parent_messageModel;
    }

    public void setParent_message(MessageModel parent_messageModel) {
        this.parent_messageModel = parent_messageModel;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }

    public Integer getIs_Starred() {
        return is_Starred;
    }

    public void setIs_Starred(Integer is_Starred) {
        this.is_Starred = is_Starred;
    }

    @NonNull
    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(@NonNull Timestamp created_at) {
        this.created_at = created_at;
    }
}
