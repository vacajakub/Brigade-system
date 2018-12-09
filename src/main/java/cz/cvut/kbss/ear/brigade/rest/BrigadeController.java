package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.dao.implementations.AddressDao;
import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.*;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.CategoryService;
import cz.cvut.kbss.ear.brigade.service.EmployerService;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brigades")
public class BrigadeController {

    private static final Logger LOG = LoggerFactory.getLogger(BrigadeController.class);

    private final BrigadeService brigadeService;
    private final WorkerService workerService;
    private final EmployerService employerService;
    private final CategoryService categoryService;
    private final AddressDao addressDao;

    @Autowired
    public BrigadeController(BrigadeService brigadeService, WorkerService workerService, EmployerService employerService,
                             CategoryService categoryService, AddressDao addressDao) {
        this.brigadeService = brigadeService;
        this.workerService = workerService;
        this.employerService = employerService;
        this.categoryService = categoryService;
        this.addressDao = addressDao;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBrigade(@RequestBody Brigade brigade) {
        brigadeService.persist(brigade);
        LOG.debug("Created brigade {}.", brigade);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", brigade.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getBrigades() {
        return brigadeService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBrigade(@PathVariable("id") Integer id) {
        Brigade brigade = brigadeService.find(id);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", id);
        }
        brigadeService.removeBrigade(brigade);
    }

    @RequestMapping(value = "/add/brigade/{brigadeId}/{employerId}/{categoryId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBrigade(@PathVariable("brigadeId") Integer brigadeId,
                           @PathVariable("employerId") Integer employerId,
                           @PathVariable("categoryId") Integer categoryId,
                           @RequestBody Address address) {
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }
        Employer employer = employerService.find(employerId);
        if (employer == null) {
            throw NotFoundException.create("Employer", employerId);
        }
        Category category = categoryService.find(categoryId);
        if (category == null) {
            throw NotFoundException.create("Category", categoryId);
        }
        addressDao.persist(address);
        brigadeService.addBrigade(employer, brigade, category, address);
    }

    @RequestMapping(value = "/add/worker/{brigadeId}/{workerId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addWorker(@PathVariable("brigadeId") Integer brigadeId,
                          @PathVariable("workerId") Integer workerId) {
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        brigadeService.addWorker(brigade, worker);
    }

    @RequestMapping(value = "/remove/worker/{brigadeId}/{workerId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWorkerFromBrigade(@PathVariable("brigadeId") Integer brigadeId,
                                        @PathVariable("workerId") Integer workerId) {
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        brigadeService.removeWorkerFromBrigade(brigade, worker);
    }


    @RequestMapping(value = "/filters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> findByFilters(@RequestParam(value = "city", required = false) String city,
                                       @RequestParam(value = "money", required = false) Integer money,
                                       @RequestParam(value = "days", required = false) Integer days,
                                       @RequestParam(value = "categoryId", required = false) Integer categoryId) {
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


}
