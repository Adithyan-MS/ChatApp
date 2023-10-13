package com.thinkpalm.ChatApplication.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "message_receiver")
public class MessageReceiverModel extends Auditable{

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


}
