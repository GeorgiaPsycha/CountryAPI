package org.example.Model;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection ="KafkaCountries") // collection will be saved in the DB
public class KafkaData {
    @Id
    private String countryName; // countryName as unique id to search based on
    private String countryCode;
    private String capital;
    private String continent;
    private String officialLanguage;
    private String currencyName;

    @Indexed(expireAfter ="600" ) // ttl 10
    private Date createdAt_kafka= new Date();



}

