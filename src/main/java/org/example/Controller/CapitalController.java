package org.example.Controller;
import org.example.Model.Country;
import org.example.Service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// path /capital/Athens
@RestController// to return raw data /json kai oxi search gia .html arxeio
@RequestMapping("/capital") // to path ksekinaei me /capital
public class CapitalController {

    @Autowired
    private CountryService countryService;


    @GetMapping ("/{capital-name}") // in order to take the name from the link i use {}
    public Country getCapitalName(@PathVariable("capital-name") String capitalName) {
        return countryService.getCapital(capitalName);

    }
}
