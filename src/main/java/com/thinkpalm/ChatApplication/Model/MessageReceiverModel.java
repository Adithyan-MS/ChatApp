package com.thinkpalm.ChatApplication.Model;


import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;

@Entity
@Table(name = "message_receiver")
public class MessageReceiverModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "message_id")
    private MessageModel message;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "receiver_id")
    private UserModel receiver;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp received_at;

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

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public Timestamp getReceived_at() {
        return received_at;
    }

    public void setReceived_at(Timestamp received_at) {
        this.received_at = received_at;
    }
}
