package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Country;
import org.example.Model.CountryApiProperties;
import org.example.Model.CountryDTO;
import org.example.Model.KafkaData;
import org.example.Repository.KafkaRepository;
import org.example.Service.KafkaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class KafkaServiceTest {

    @Mock
    private KafkaRepository kafkaRepository;

    @Mock
    private RestTemplate countriesAPIClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CountryApiProperties apiProperties;


    @InjectMocks
    private KafkaService kafkaService;

    @Test
    void getKafkaCountry_cached(){
        KafkaData cachedKafka = new KafkaData();
        cachedKafka.setCountryName("Greece");
        cachedKafka.setCountryCode("GR");
        cachedKafka.setCapital("Athens");
        cachedKafka.setContinent("Europe");
        cachedKafka.setOfficialLanguage("Greek");
        cachedKafka.setCurrencyName("Euro");
        cachedKafka.setCreatedAt_kafka(new Date());

        Country expectedCountry = objectMapper.convertValue(cachedKafka, Country.class);
        when(kafkaRepository.findAllByCountryName("Greece"))
                .thenReturn(List.of(cachedKafka));

        //the actuall method call
        Country result = kafkaService.getCountries("Greece");

        // tsekarw ta swsta ena ena gia na vrw pio eukola to la8os
        assertEquals(expectedCountry, result);

    }

    @Test
    void getKafkaCountryApi(){
        // to fake dt0[1] pou epistrefei to API call
        CountryDTO[] dt0 = new CountryDTO[1];
        dt0[0] = new CountryDTO();
        CountryDTO.Name name = new CountryDTO.Name();
        name.setCommon("Greece");
        dt0[0].setName(name);

        dt0[0].setCca2("GR");
        dt0[0].setCapital(List.of("Athens"));
        dt0[0].setContinents(List.of("Europe"));
        dt0[0].setLanguages(Map.of("ell", "Greek"));
        dt0[0].setCurrencies(Map.of("EUR", new CountryDTO.Currency("Euro", "€")));

        Country expectedResult = new Country("Greece","GR","Athens","Europe","Greek","Euro");

        //check oti den iparxei stom kafka
        when(kafkaRepository.findAllByCountryName("Greece"))
                .thenReturn(Collections.emptyList());
        when(apiProperties.getCountry()).thenReturn("KafkaUrl");
        when(countriesAPIClient.getForObject(eq("KafkaUrl"), eq(CountryDTO[].class), eq("Greece")))
                .thenReturn(dt0);

        Country result = kafkaService.getCountries("Greece");

        assertEquals(expectedResult, result);
        //assertEquals(country.getCountryCode(), dto.getCca2());


    }


    @Test
    void getCapital_Exception() {

        when(kafkaRepository.findAllByCountryName("Salami"))
                .thenReturn(Collections.emptyList());
        when(apiProperties.getCountry()).thenReturn("URL");
        when(countriesAPIClient.getForObject(eq("URL"), eq(CountryDTO[].class), eq("Salami")))
                .thenThrow(new RestClientException("API call failed"));

        Country result = kafkaService.getCountries("Salami");
        assertNull(result);

    }

    @Test
    void getCapital_Null() {

        when(kafkaRepository.findAllByCountryName("Salami"))
                .thenReturn(Collections.emptyList());
        when(apiProperties.getCountry()).thenReturn("URL");
        when(countriesAPIClient.getForObject(eq("URL"), eq(CountryDTO[].class), eq("Salami")))
                .thenReturn(null);
        Country result = kafkaService.getCountries("Salami");
        assertNull(result);

    }
}
