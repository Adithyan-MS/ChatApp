package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "participant")
public class ParticipantModel {

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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public RoomModel getRoom() {
        return room;
    }

    public void setRoom(RoomModel room) {
        this.room = room;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
    }

    public Timestamp getLeft_at() {
        return left_at;
    }

    public void setLeft_at(Timestamp left_at) {
        this.left_at = left_at;
    }
}
