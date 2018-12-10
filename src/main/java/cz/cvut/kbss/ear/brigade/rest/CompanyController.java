package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Company;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
import cz.cvut.kbss.ear.brigade.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCompany(@RequestBody Company company) {
        companyService.persist(company);
        LOG.debug("Created company {}.", company);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", company.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBrigade(@PathVariable("id") Integer id) {
        Company company = companyService.find(id);
        if (company == null) {
            throw NotFoundException.create("Brigade", id);
        }
        companyService.remove(company);
    }
}
