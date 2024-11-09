package com.Springpro.Devtools.Service;

import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class KafkaAdminService {

    private final KafkaAdmin kafkaAdmin;

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;

    @Autowired
    public KafkaAdminService(@Qualifier("kafkaAdmin") KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    public void createTopic(String bootstrapServers, String topicName, int partitions, short replicationFactor) {
        try (AdminClient adminClient = AdminClient.create(Collections.singletonMap("bootstrap.servers", bootstrapServers))) {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllTopics(String bootstrapServers) {
        try (AdminClient adminClient = AdminClient.create(Collections.singletonMap("bootstrap.servers", bootstrapServers))) {
            ListTopicsOptions options = new ListTopicsOptions().listInternal(true);
            ListTopicsResult topicsResult = adminClient.listTopics(options);
            Collection<org.apache.kafka.clients.admin.TopicListing> topics = topicsResult.listings().get();
            return topics.stream()
                    .map(org.apache.kafka.clients.admin.TopicListing::name)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

