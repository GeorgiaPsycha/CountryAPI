package org.example.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.ConstructorParameters;
import java.util.List;
import java.util.Map;

// {} object [] array se json
// helps us take only the values that we want from the Json payload
@Data
public class CountryDTO {
    private Name name; //{"name": {"common": "Greece","official": "Hellenic Republic"}}
    private List<String> capital; // gt {"capital": ["Athens ]}
    private List<String> continents;//"continents": ["Europe"]
    private Map<String, String> languages;  //  {"ell": "Greek"} dynamic
    private Map<String, Currency> currencies; //"currencies": { "EUR": { "symbol": "€", "name": "Euro"}
    private String cca2;// "cca2": "GR"


    // Nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Name {
        private String common;
        private String official;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Currency {
        private String name;
        private String symbol;
    }
}