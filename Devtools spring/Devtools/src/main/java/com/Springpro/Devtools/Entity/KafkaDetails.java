package com.Springpro.Devtools.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="KafkaDetails")
@NoArgsConstructor
@AllArgsConstructor
public class KafkaDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicid;
    private String bootstrap_servers;
    private String groupid;
    private int partitions;
    private short replication_factor;
    private String topic_name;
}