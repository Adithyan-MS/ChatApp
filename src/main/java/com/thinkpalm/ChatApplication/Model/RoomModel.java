package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "room")
public class RoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String room_pic;

    @Column
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp modified_at;

}
