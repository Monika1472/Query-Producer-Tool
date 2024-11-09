package com.Springpro.Devtools.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="MessageDetails")
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message_title;
    private String message_desc;
    private String bootstrap_servers;
    private String topic_name;
    private String message_content;
    private String type_of_placeholders;
}