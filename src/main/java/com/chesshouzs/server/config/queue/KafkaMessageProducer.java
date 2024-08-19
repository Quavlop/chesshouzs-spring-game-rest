package com.chesshouzs.server.config.queue;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;  
import com.google.gson.Gson;

@Component
public class KafkaMessageProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new Gson();

    public void publish(String topic, String key, Object message) {
        try {
            String jsonString = gson.toJson(message);
            kafkaTemplate.send(topic, key, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}