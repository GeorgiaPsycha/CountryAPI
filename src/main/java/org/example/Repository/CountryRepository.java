package org.example.Repository;
import org.example.Model.CountryData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CountryRepository extends MongoRepository<CountryData, String> {

    List<CountryData> findAllByCountryName(String countryName);
}
