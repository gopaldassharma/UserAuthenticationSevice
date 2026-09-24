package com.example.userauthenticationsevice.clients;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient {
    // data type of topic
    // data type of message
    private KafkaTemplate<String, String> kafkaTemplate;
    public KafkaClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String topic, String message){
        System.out.println("--> PRODUCER: Sending message to topic [" + topic + "]: " + message);
        kafkaTemplate.send(topic, message);
    }
}
