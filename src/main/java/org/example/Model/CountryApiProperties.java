package org.example.Model;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration // tonizei oti einai configuration
@ConfigurationProperties(prefix = "countries.api.url")
public class CountryApiProperties {
    private String capital;
    private String country;
}