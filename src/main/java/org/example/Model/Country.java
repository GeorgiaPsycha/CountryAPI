package org.example.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // implement all getters/setters
@JsonIgnoreProperties(ignoreUnknown = true) // to ignore the createdAt in the mapper from DAO
public class Country {
    // the attributes I want to serve-view
    private String countryName;
    private String countryCode;
    private String capital;
    private String continent;
    private String officialLanguage;
    private String currencyName;


    public Country(String CountryName) {
        this.countryName = CountryName;
    }
}