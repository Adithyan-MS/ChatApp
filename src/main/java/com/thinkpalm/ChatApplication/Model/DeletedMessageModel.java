package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="deleted_message")
public class DeletedMessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageModel message;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private UserModel user;

    @Column
    private Timestamp deleted_at;
}
