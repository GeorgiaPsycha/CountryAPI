package org.example.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Country;
import org.example.Model.CountryApiProperties;
import org.example.Model.CountryDTO;
import org.example.Model.CountryData;
import org.example.Repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class CountryService {

    // na printarw ta log
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);


    @Autowired
    private CountryRepository countryRepository;

    @Autowired // to bring the one from application context
    private RestTemplate countriesAPIClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired // pairnw dynamika ta urls
    private CountryApiProperties apiProperties;


    public Country getCapital(String capital) {

    try{
        String url = apiProperties.getCapital();
        ResponseEntity<CountryDTO[]> response = countriesAPIClient.getForEntity(url, CountryDTO[].class, capital);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            System.out.println("Message not found " );
            return null;
        }

        CountryDTO[] cty = response.getBody();
        if (cty == null || cty.length == 0) {
            return null;
        }

        List<CountryDTO> responseCountry = List.of(cty);

        Country country = DTOtoCountry(responseCountry);
        return country;

    } catch (HttpClientErrorException e) {
        //  404 Not Found
        System.out.println("Message not found client" );
        return null;

    } catch (HttpServerErrorException e) {
        // 500 Internal Server Error
        System.out.println("Message not found server " );
        return null;

    } catch (RestClientException e) {
        logger.error("Failed to call RestCountries API for capital {}", capital, e);

        System.out.println("Message not found rest " );
        return null;
    }
}


public Country getCountry (String countryName){
        try {
            //Country country = new Country();
            logger.info("getCountry called");
            List<CountryData> cachedCountry = countryRepository.findAllByCountryName(countryName);
            if (!cachedCountry.isEmpty()) {
                logger.info("Country found in the DB");
                Country country = objectMapper.convertValue(cachedCountry.get(0), Country.class);
                return country;

            }
            String url = apiProperties.getCountry();
            CountryDTO[] cty = countriesAPIClient.getForObject(url, CountryDTO[].class, countryName);

            if (cty == null) {
                return null;
            }
            List<CountryDTO> responseCountry = List.of(cty);

            Country country = DTOtoCountry(responseCountry);

            CountryData countryData = new CountryData();
            countryData.setCountryName(country.getCountryName());
            countryData.setCountryCode(country.getCountryCode());
            countryData.setCapital(country.getCapital());
            countryData.setCurrencyName(country.getCurrencyName());
            countryData.setContinent(country.getContinent());
            countryData.setOfficialLanguage(country.getOfficialLanguage());
            countryData.setCreatedAt(new Date());

            countryRepository.save(countryData);

            return country;

        } catch (RestClientException e) {

        System.out.println("Message not found " );
        return null;
    }
    }

    public Country DTOtoCountry(List<CountryDTO> responseCountry){
        Country country = new Country();
        CountryDTO countryDTO = responseCountry.get(0);
        country.setCountryName(countryDTO.getName().getCommon());
        country.setCountryCode(countryDTO.getCca2());
        country.setCapital(countryDTO.getCapital().get(0));
        country.setCurrencyName(countryDTO.getCurrencies().values().iterator().next().getName());
        country.setContinent(countryDTO.getContinents().get(0));
        country.setOfficialLanguage(countryDTO.getLanguages().values().iterator().next());
        return  country;
    }

}
