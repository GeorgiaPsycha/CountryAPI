package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Country;
import org.example.Model.KafkaData;
import org.example.Repository.KafkaRepository;
import org.example.Service.KafkaCosnumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerTest {

    @Mock
    private KafkaRepository kafkaRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaCosnumer kafkaConsumer; // το service σου

    @Test
    void consume_existingCountry() {

        KafkaData cachedkafka = new KafkaData();
        when(kafkaRepository.findAllByCountryName("Greece"))
                .thenReturn(List.of(cachedkafka));

        //gt dexetai country h me8odos
        Country country = new Country("Greece");
        kafkaConsumer.consume(country);

        verify(kafkaRepository, never()).save(any());
    }

    @Test
    void consume_newCountry() {
        //gt dexetai country h me8odos
        Country country = new Country("Greece");
        kafkaConsumer.consume(country);
        KafkaData kafkaData = new KafkaData();

        when(kafkaRepository.findAllByCountryName("Greece")).thenReturn(Collections.emptyList());
        when(objectMapper.convertValue(country, KafkaData.class)).thenReturn(kafkaData);

        kafkaConsumer.consume(country);
        verify(kafkaRepository).save(kafkaData);
    }
}
