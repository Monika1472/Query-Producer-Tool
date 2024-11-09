package com.Springpro.Devtools.Controller;

import com.Springpro.Devtools.Entity.KafkaDetails;
import com.Springpro.Devtools.Service.KafkaAdminService;
import com.Springpro.Devtools.Service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private final KafkaService kafkaService;

    @Autowired
    @Lazy
    private final KafkaAdminService kafkaAdminService;

    @Autowired
    public KafkaController(KafkaService kafkaService, KafkaAdminService kafkaAdminService) {
        this.kafkaService = kafkaService;
        this.kafkaAdminService = kafkaAdminService;
    }
    @GetMapping("/admin/inbuiltAllTopics/{bootstrap_servers}")
    public List<String> getAllInbuiltTopics(@PathVariable String bootstrap_servers) {
        return kafkaAdminService.getAllTopics(bootstrap_servers);
    }
    @GetMapping("/user/topics")
    public List<KafkaDetails> getAllTopics() {
        return kafkaService.getAll();
        //return kafkaAdminService.getAllTopics();
    }

    @PostMapping("/admin/create-topic")
    public String createTopic(@RequestBody KafkaDetails kafkaDetails) {
        kafkaService.saveDetails(kafkaDetails);
        kafkaAdminService.createTopic(kafkaDetails.getBootstrap_servers(),kafkaDetails.getTopic_name(), kafkaDetails.getPartitions(), kafkaDetails.getReplication_factor());
        return("Topic created successfully!");
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(
            @RequestBody Map<String, Object> request
    ) {
        try {
            String topicName = (String) request.get("topicName");
            String bootstrapServers = (String) request.get("bootstrap_servers");
            String key = (String) request.get("key");
            String message = (String) request.get("message");
            Map<String, Object> jsonMessage = (Map<String, Object>) request.get("jsonMessage");
            Map<String, String> Header = (Map<String, String>) request.get("header");

            if (message != null) {
                String decodedMessage = URLDecoder.decode(message, "UTF-8");
                kafkaService.sendMessageWithServer(bootstrapServers, topicName, key, decodedMessage, Header);
            } else if (jsonMessage != null) {
                String serializedMessage = new ObjectMapper().writeValueAsString(jsonMessage);
                kafkaService.sendMessageWithServer(bootstrapServers, topicName, key, serializedMessage, Header);
            } else {
                return ResponseEntity.status(400).body("Either 'message' or 'jsonMessage' must be provided.");
            }

            return ResponseEntity.ok("Message sent to the topic: " + topicName);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing the message: " + e.getMessage());
        }
    }
}