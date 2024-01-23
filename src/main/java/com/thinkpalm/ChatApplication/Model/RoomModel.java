package com.thinkpalm.ChatApplication.Model;

import com.thinkpalm.ChatApplication.Validation.RoomNameValid;
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
public class RoomModel extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @RoomNameValid
    private String name;

    @Column
    private String description;

    @Column
    private String room_pic;

    @Column(unique = true)
    private String room_code;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserModel createdBy;

}
