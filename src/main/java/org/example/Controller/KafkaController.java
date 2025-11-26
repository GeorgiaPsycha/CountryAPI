package org.example.Controller;
import org.example.Model.KafkaData;
import org.example.Service.KafkaProducer;
import org.example.Service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.Model.Country;

import java.util.List;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private  KafkaProducer kafkaProducer;
    @Autowired
    private KafkaService kafkaService;


    @PostMapping
    public ResponseEntity<String> postCountry(@RequestBody Country country) {
        kafkaProducer.sendCountry(country);
        return ResponseEntity.ok("Country sent to Kafka topic.");
    }

    @GetMapping("/{country-name}")
    public Country getKafkaCountry(@PathVariable("country-name") String countryName) {
        return kafkaService.getCountries(countryName);
    }


}
