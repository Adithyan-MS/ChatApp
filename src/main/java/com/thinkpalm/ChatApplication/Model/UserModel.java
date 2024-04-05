package com.thinkpalm.ChatApplication.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkpalm.ChatApplication.Validation.BioDescValid;
import com.thinkpalm.ChatApplication.Validation.EmailValid;
import com.thinkpalm.ChatApplication.Validation.NameValid;
import com.thinkpalm.ChatApplication.Validation.PasswordValid;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserModel extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    @NameValid
    private String name;

    @Column
    @NotNull
    @NotEmpty
    @PasswordValid
    private String password;

    @Column
    @EmailValid
    private String email;

    @Column
    @NotNull
    @Pattern(regexp = "^[0-9]{10}$",message = "invalid phone number")
    private String phone_number;

    @Column
    private String bio;

    @Column
    private String profilePic;

    @Column
    private boolean isOnline = false;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;

}
