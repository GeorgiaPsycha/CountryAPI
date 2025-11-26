package org.example.Repository;

import org.example.Model.CountryData;
import org.example.Model.KafkaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KafkaRepository  extends MongoRepository<KafkaData, String> {

    List<KafkaData> findAllByCountryName(String countryName);
}

