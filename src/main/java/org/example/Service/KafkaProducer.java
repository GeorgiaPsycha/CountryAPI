package org.example.Service;
import lombok.Data;
import org.example.Model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class KafkaProducer {

    //pairnei onoma topic apo application properties
    @Value("${kafka.topic.name}")
    private String topicName;

    @Autowired
    public KafkaTemplate<String, Country> kafkaTemplate;


    public void sendCountry(Country country) {
        kafkaTemplate.send(topicName,country);
    }

}
