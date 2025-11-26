package org.example.Controller;
import org.example.Service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.Model.Country;

@RestController
@RequestMapping("/country") // path = /country
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping() // path = /country/Greece
    public Country getCountryByName(@RequestParam("name") String countryName) {

        return countryService.getCountry(countryName);
    }
}
