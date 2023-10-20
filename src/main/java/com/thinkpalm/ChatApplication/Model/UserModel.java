package com.thinkpalm.ChatApplication.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

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
    @Size(max = 15,min = 3)
    private String name;

    @Column
    @NotNull
    @NotEmpty
    @Size(min = 5)
    private String password;

    @Column
    @NotNull
    @Email
    private String email;

    @Column
    @NotNull
    @Pattern(regexp = "^[0-9]{10}$",message = "invalid phone number")
    private String phone_number;

    @Column
    private String bio;

    @Column
    private String profilePic;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;

}
