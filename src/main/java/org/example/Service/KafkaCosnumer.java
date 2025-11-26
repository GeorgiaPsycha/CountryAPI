package org.example.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.Country;
import org.example.Model.KafkaData;
import org.example.Repository.KafkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class KafkaCosnumer  {

    @Value("${kafka.topic.name}")
    private String topicName;

    @Autowired
    private KafkaRepository kafkaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    //currency 3 gia na exw parallilismo me 3 partitions
    @KafkaListener(topics = "${kafka.topic.name}", concurrency = "3")
    public void consume(Country country) {
        log.info("getCountry called");
        List<KafkaData> cachedCountry = kafkaRepository.findAllByCountryName(country.getCountryName());
        if (!cachedCountry.isEmpty()) {
            log.info("KafkaCountry found in the DB");
        }else {
            System.out.println("Consumed: " + country);
            KafkaData kafkaData = objectMapper.convertValue(country, KafkaData.class);
            kafkaRepository.save(kafkaData);
        }
    }
}
