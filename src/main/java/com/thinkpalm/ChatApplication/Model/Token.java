package com.thinkpalm.ChatApplication.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue
    private Integer id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

}
