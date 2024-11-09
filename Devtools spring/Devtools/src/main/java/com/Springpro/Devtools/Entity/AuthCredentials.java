package com.Springpro.Devtools.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="login_credentials")
@NoArgsConstructor
@AllArgsConstructor

public class AuthCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loginid;
    private String username;
    private String password;
    private String role;

}
