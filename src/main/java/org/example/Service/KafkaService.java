package org.example.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.*;
import org.example.Repository.KafkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class KafkaService {

    @Autowired
    private RestTemplate countriesAPIClient;
    @Autowired // pairnw dynamika ta urls
    private CountryApiProperties apiProperties;
    @Autowired
    private KafkaRepository kafkaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Country getCountries(String countryName) {
        Country country = new Country();
        try {
            List<KafkaData> cachedCountry = kafkaRepository.findAllByCountryName(countryName);
            if (!cachedCountry.isEmpty()) {
                KafkaData kafkacountry = cachedCountry.get(0);
                country = objectMapper.convertValue(kafkacountry, Country.class);
                return country;
            } else {
                System.out.println("No country found in Kafka bring it from API");
                String url = apiProperties.getCountry();
                CountryDTO[] cty = countriesAPIClient.getForObject(url, CountryDTO[].class, countryName);

                if (cty == null) {
                    return null;
                }

                List<CountryDTO> responseCountry = List.of(cty);
                return mappedToCountry(responseCountry);

            }
        } catch (RestClientException e) {

            System.out.println("Message not found ");
            return null;
        }


    }
    public Country mappedToCountry(List<CountryDTO> responseCountry){
        Country country = new Country();
        CountryDTO countryDTO = responseCountry.get(0);
        country.setCountryName(countryDTO.getName().getCommon());
        country.setCountryCode(countryDTO.getCca2());
        country.setCapital(countryDTO.getCapital().get(0));
        country.setCurrencyName(countryDTO.getCurrencies().values().iterator().next().getName());
        country.setContinent(countryDTO.getContinents().get(0));
        country.setOfficialLanguage(countryDTO.getLanguages().values().iterator().next());
        return country ;

    }
}
