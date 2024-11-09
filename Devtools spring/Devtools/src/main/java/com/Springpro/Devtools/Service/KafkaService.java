package com.Springpro.Devtools.Service;

import com.Springpro.Devtools.Entity.KafkaDetails;
import com.Springpro.Devtools.Repository.KafkaDetailsRepository;
import com.Springpro.Devtools.Repository.MessageTemplateRepository;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class KafkaService {
    private final KafkaDetailsRepository kafkaDetailsRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaService(
            KafkaDetailsRepository kafkaDetailsRepository,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.kafkaDetailsRepository = kafkaDetailsRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public KafkaDetails saveDetails(KafkaDetails kafkaDetails){
        return kafkaDetailsRepository.save(kafkaDetails);
    }
    public List<KafkaDetails> getAll(){
        return kafkaDetailsRepository.findAll();
    }
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    public String sendMessageWithServer(String bootstrapServer, String topicName, String key, String message, Map<String, String> Header) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        try (org.apache.kafka.clients.producer.Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties)) {
            Iterable<Header> kafkaHeaders = Header.entrySet().stream()
                    .map(entry -> new RecordHeader(entry.getKey(), entry.getValue().getBytes()))
                    .collect(Collectors.toList());
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, message);
            for (Header header : kafkaHeaders) {
                record.headers().add(header);
            }
            RecordMetadata metadata = producer.send(record).get();
            return "Message sent successfully to partition " + metadata.partition() + " with offset " + metadata.offset();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending message: " + e.getMessage();
        }
    }



}
