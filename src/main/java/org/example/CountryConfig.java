package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//to use rest and be able to call an external API
@Configuration
public class CountryConfig {

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper()
    { return new ObjectMapper();
    }
}
