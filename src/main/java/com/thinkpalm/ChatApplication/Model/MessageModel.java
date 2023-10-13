package com.thinkpalm.ChatApplication.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "message")
public class MessageModel extends Auditable{

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

}
