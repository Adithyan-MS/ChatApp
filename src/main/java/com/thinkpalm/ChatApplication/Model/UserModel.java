package com.thinkpalm.ChatApplication.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserModel extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private Integer phone_number;

    @Column
    private String bio;

    @Column
    private String profilePic;


}
