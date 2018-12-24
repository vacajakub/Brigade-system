package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Address;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.CategoryService;
import cz.cvut.kbss.ear.brigade.service.EmployerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/brigades")
public class BrigadeController {

    private static final Logger LOG = LoggerFactory.getLogger(BrigadeController.class);

    private final BrigadeService brigadeService;
    private final EmployerService employerService;
    private final CategoryService categoryService;

    @Autowired
    public BrigadeController(BrigadeService brigadeService, EmployerService employerService,
                             CategoryService categoryService) {
        this.brigadeService = brigadeService;
        this.employerService = employerService;
        this.categoryService = categoryService;
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getBrigades() {
        return brigadeService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Brigade getBrigade(@PathVariable("id") Integer id) {
        final Brigade brigade = findBrigade(id);
        LOG.debug("Returned brigade with id {}.", brigade.getId());
        return brigade;
    }

    @RequestMapping(value = "remove/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBrigade(@PathVariable("id") Integer id) {
        Brigade brigade = findBrigade(id);
        LOG.debug("Removed brigade with id {}.", brigade.getId());
        brigadeService.removeBrigade(brigade);
    }

    @RequestMapping(value = "/add/brigade/{employerId}/{categoryId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBrigade(@RequestBody Brigade brigade,
                           @PathVariable("employerId") Integer employerId,
                           @PathVariable("categoryId") Integer categoryId,
                           @RequestParam(value = "city") String city,
                           @RequestParam(value = "street") String street,
                           @RequestParam(value = "zipCode", required = false) String zipCode) {
        Employer employer = findEmployer(employerId);
        Category category = categoryService.find(categoryId);
        if (category == null) {
            throw NotFoundException.create("Category", categoryId);
        }
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        if (zipCode != null) {
            address.setZipCode(zipCode);
        }
        LOG.debug("Created brigade {} with address {}, category {} and employer {}.", brigade, address, category, employer);
        brigadeService.create(employer, brigade, category, address);
    }


//    @RequestMapping(value = "/remove/worker/{brigadeId}/{workerId}", method = RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void removeWorkerFromBrigade(@PathVariable("brigadeId") Integer brigadeId,
//                                        @PathVariable("workerId") Integer workerId) {
//        Brigade brigade = brigadeService.find(brigadeId);
//        if (brigade == null) {
//            throw NotFoundException.create("Brigade", brigadeId);
//        }
//        Worker worker = workerService.find(workerId);
//        if (worker == null) {
//            throw NotFoundException.create("Worker", workerId);
//        }
//        brigadeService.removeWorkerFromBrigade(brigade, worker);
//    }


    @RequestMapping(value = "/filters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> findByFilters(@RequestParam(value = "city", required = false) String city,
                                       @RequestParam(value = "money", required = false) Integer money,
                                       @RequestParam(value = "days", required = false) Integer days,
                                       @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        LOG.debug("Called findByFilters() method");
        if (categoryId != null) {
            Category category = categoryService.find(categoryId);
            if (category == null) {
                throw NotFoundException.create("Category", categoryId);
            }
            return brigadeService.findByFilters(category, city, money, days);
        } else {
            return brigadeService.findByFilters(null, city, money, days);
        }
    }

    private Employer findEmployer(Integer id) {
        Employer employer = employerService.find(id);
        if (employer == null) {
            throw NotFoundException.create("Employer", id);
        }
        return employer;
    }

    private Brigade findBrigade(Integer id) {
        Brigade brigade = brigadeService.find(id);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", id);
        }
        return brigade;
    }


}
