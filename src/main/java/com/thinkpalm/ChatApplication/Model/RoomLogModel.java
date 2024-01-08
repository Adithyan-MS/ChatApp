package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "room_log")
public class RoomLogModel extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NotNull
    private RoomModel room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private UserModel user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomAction action;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

}
