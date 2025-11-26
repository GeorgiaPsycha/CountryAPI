package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Country;
import org.example.Model.CountryApiProperties;
import org.example.Model.CountryDTO;
import org.example.Model.CountryData;
import org.example.Repository.CountryRepository;
import org.example.Service.CountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    // mock all the dependencies pou 8a xreiastw
    @Mock
    private CountryRepository countryRepository;

    @Mock
    private RestTemplate countriesAPIClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CountryApiProperties apiProperties;


    @InjectMocks
    private CountryService countryService;

    //find the country in the DB
    @Test
    void GetCountry_fromDB() {
        // create the fake data
        CountryData data = new CountryData();
        data.setCountryName("Greece");
        data.setCountryCode("GR");
        data.setCapital("Athens");
        data.setContinent("Europe");
        data.setOfficialLanguage("Greek");
        data.setCurrencyName("Euro");
        List<CountryData> dataList = List.of(data);

        Country expectedCountry = objectMapper.convertValue(dataList.get(0), Country.class);

        // ta 2 when statmnets gia to ti na kanei
        when(countryRepository.findAllByCountryName("Italy")).thenReturn(dataList);

        //the actual method call
        Country result = countryService.getCountry("Italy");

        // tsekarw ta swsta ena ena gia na vrw pio eukola to la8os
        assertEquals(expectedCountry, result);

    }


    // get the country from the API
    @Test
    void GetCountry_fromAPI() {
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

        //den iparxei sto DB
        when(countryRepository.findAllByCountryName("Greece"))
                .thenReturn(Collections.emptyList());
        when(apiProperties.getCountry()).thenReturn("fakeUrl");
        when(countriesAPIClient.getForObject(eq("fakeUrl"), eq(CountryDTO[].class), eq("Greece")))
                .thenReturn(dt0);

        Country expectedResult = new Country("Greece","GR","Athens","Europe","Greek","Euro");


        Country result = countryService.getCountry("Greece");
        assertEquals(expectedResult, result);



    }




    // la8os onoma xwra fail
        @Test
        void GetCountry_WrongName () {
            when(countryRepository.findAllByCountryName("Salami"))
                    .thenReturn(Collections.emptyList());
            when(apiProperties.getCountry()).thenReturn("fakeUrl");
            when(countriesAPIClient.getForObject(eq("fakeUrl"), eq(CountryDTO[].class), eq("Salami")))
                    .thenThrow(new RestClientException("404 Not Found"));


            Country result = countryService.getCountry("Salami");

            assertNull(result);

        }

        @Test
        void GetCapital_fromAPI(){

            // fake DTO
            Country expectedResult = new Country("Greece","GR","Athens","Europe","Greek","Euro");

            //prepei na gemisw olo to DTO gia na mporesw na testarw
            CountryDTO dto = new CountryDTO();
            CountryDTO.Name name = new CountryDTO.Name();
            name.setCommon("Greece");
            dto.setName(name);
            dto.setCca2("GR");
            dto.setCapital(List.of("Athens"));
            dto.setContinents(List.of("Europe"));
            dto.setLanguages(Map.of("ell", "Greek"));
            dto.setCurrencies(Map.of("EUR", new CountryDTO.Currency("Euro", "€")));

            List<CountryDTO> dtoList = List.of(dto);


            //giati to response entity exei kai header kai body ara 8elw kai status code
            ResponseEntity<CountryDTO[]> response = new ResponseEntity<>(new CountryDTO[]{dto}, HttpStatus.OK);

            when(apiProperties.getCapital()).thenReturn("AthensURL");
            when(countriesAPIClient.getForEntity(eq("AthensURL"), eq(CountryDTO[].class), eq("Athens")))
                    .thenReturn(response);

            Country result = countryService.getCapital("Athens");

            assertEquals(expectedResult, result);

        }

    @Test
    void getCapital_Exception() {


        when(apiProperties.getCapital()).thenReturn("FakeURL");
        when(countriesAPIClient.getForEntity(eq("FakeURL"), eq(CountryDTO[].class), eq("Salami")))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Country result = countryService.getCapital("Salami");
        assertNull(result);
    }

    @Test
    void getCapital_Null() {


        when(apiProperties.getCapital()).thenReturn("URL");
        ResponseEntity<CountryDTO[]> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(countriesAPIClient.getForEntity(eq("URL"), eq(CountryDTO[].class), eq("Greece")))
                .thenReturn(response);

        Country result = countryService.getCapital("Greece");
        assertNull(result);
    }

    @Test
    void GetCountry_Null () {
        when(countryRepository.findAllByCountryName("Greece"))
                .thenReturn(Collections.emptyList());
        when(apiProperties.getCountry()).thenReturn("Url");
        when(countriesAPIClient.getForObject(eq("Url"), eq(CountryDTO[].class), eq("Greece")))
                .thenReturn(null);

        Country result = countryService.getCountry("Greece");

        assertNull(result);

    }

    @Test
    void getCapital_HttpServerError() {
        when(apiProperties.getCapital()).thenReturn("URL");
        when(countriesAPIClient.getForEntity(eq("URL"), eq(CountryDTO[].class), eq("Greece")))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Country result = countryService.getCapital("Greece");
        assertNull(result);
    }

    @Test
    void getCapital_GenericRestClientException() {
        when(apiProperties.getCapital()).thenReturn("URL");
        when(countriesAPIClient.getForEntity(eq("URL"), eq(CountryDTO[].class), eq("Greece")))
                .thenThrow(new RestClientException("Generic error"));

        Country result = countryService.getCapital("Greece");
        assertNull(result);
    }


}