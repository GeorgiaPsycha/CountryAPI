package org.example;

import org.example.Model.Country;
import org.example.Service.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;


import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, Country> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(kafkaProducer, "topicName", "topicName");
    }

    @Test
    void sendCountry_callsKafkaTemplate() {
        Country country = new Country("Greece");
        kafkaProducer.sendCountry(country);
        // verify ότι το send καλείται σωστά
        verify(kafkaTemplate).send(eq("topicName"), eq(country));
    }
}