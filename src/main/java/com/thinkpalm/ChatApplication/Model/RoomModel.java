package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "room")
public class RoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String desc;

    @Column
    private String room_pic;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp modified_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoom_pic() {
        return room_pic;
    }

    public void setRoom_pic(String room_pic) {
        this.room_pic = room_pic;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getModified_at() {
        return modified_at;
    }

    public void setModified_at(Timestamp modified_at) {
        this.modified_at = modified_at;
    }
}
