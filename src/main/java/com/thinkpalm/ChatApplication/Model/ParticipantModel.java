package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "participant")
public class ParticipantModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomModel room;

    @Column
    private Boolean is_admin;

    @Column
    private Boolean is_active;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp joined_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp left_at;

}
