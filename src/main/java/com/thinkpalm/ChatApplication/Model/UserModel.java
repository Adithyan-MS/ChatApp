package com.thinkpalm.ChatApplication.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;

}
