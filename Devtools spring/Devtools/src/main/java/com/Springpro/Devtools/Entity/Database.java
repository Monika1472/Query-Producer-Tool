package com.Springpro.Devtools.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="database_connection")
@ToString
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Databaseid;
    private String Databasetype;
    private String Hostname;
    private String Portno;
    private int Userid;
    private String Username;
    private String Password;
    private String Dbname;
}
