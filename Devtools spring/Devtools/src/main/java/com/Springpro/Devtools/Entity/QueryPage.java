package com.Springpro.Devtools.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="query_table")
@ToString
public class QueryPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Queryid;
    private String title;
    private String Querytype;
    private String Description;
    private String dbname;
    private String collectionname;
    private String query;
    private String type_of_placeholders;
    private int databaseid;

}
