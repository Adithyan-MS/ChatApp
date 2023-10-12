package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "message_room")
public class MessageRoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageModel message;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomModel room;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;

}
