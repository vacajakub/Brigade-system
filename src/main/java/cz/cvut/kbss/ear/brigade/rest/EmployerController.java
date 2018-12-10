package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.EmployerService;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
public class EmployerController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployerController.class);

    private final EmployerService employerService;
    private final WorkerService workerService;
    private final BrigadeService brigadeService;

    @Autowired
    public EmployerController(EmployerService employerService, WorkerService workerService, BrigadeService brigadeService) {
        this.employerService = employerService;
        this.workerService = workerService;
        this.brigadeService = brigadeService;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createEmployer(@RequestBody Employer employer) {
        employerService.persist(employer);
        LOG.debug("Created employer {}.", employer);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", employer.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Employer getEmployer(@PathVariable("id") Integer id) {
        final Employer employer = employerService.find(id);
        if (employer == null) {
            throw NotFoundException.create("Employer", id);
        }
        return employer;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employer> getEmployers() {
        return employerService.findAll();
    }


    @RequestMapping(value = "/{employerId}/{brigadeId}/{workerId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWorkerFromBrigade(@PathVariable("employerId") Integer employerId,
                                        @PathVariable("brigadeId") Integer brigadeId,
                                        @PathVariable("workerId") Integer workerId) {
        Employer employer = employerService.find(employerId);
        if (employer == null) {
            throw NotFoundException.create("Employer", employerId);
        }
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        employerService.removeWorkerFromBrigade(employer, brigadeId, worker);
        LOG.debug("Worker {} removed from brigade {}.", worker, brigade);
    }


    @RequestMapping(value = "/blacklist/{employerId}/{brigadeId}/{workerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveWorkerToBlackList(@PathVariable("employerId") Integer employerId,
                                      @PathVariable("brigadeId") Integer brigadeId,
                                      @PathVariable("workerId") Integer workerId) {
        Employer employer = employerService.find(employerId);
        if (employer == null) {
            throw NotFoundException.create("Employer", employerId);
        }
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        employerService.moveWorkerToBlacklist(employer, brigadeId, worker);
        LOG.debug("Worker {} moved to blacklist of brigade {}.", worker, brigade);
    }

    @RequestMapping(value = "/{id}/brigades/future", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getFutureBrigades(@PathVariable("id") Integer id) {
        Employer employer = employerService.find(id);
        if (employer == null) {
            throw NotFoundException.create("Employer", id);
        }
        return employerService.getFutureBrigades(employer);
    }

    @RequestMapping(value = "/{id}/brigades/past", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getPastBrigades(@PathVariable("id") Integer id) {
        Employer employer = employerService.find(id);
        if (employer == null) {
            throw NotFoundException.create("Employer", id);
        }
        return employerService.getPastBrigades(employer);
    }

    @RequestMapping(value = "/{id}/score", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Pair<Integer, Integer> getEmployerScore(@PathVariable("id") Integer id) {
        Employer employer = employerService.find(id);
        if (employer == null) {
            throw NotFoundException.create("Employer", id);
        }
        return employerService.getEmployerScore(employer);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Integer id) {
        Employer employer = employerService.find(id);
        if (employer == null) {
            throw NotFoundException.create("Employer", id);
        }
        employerService.remove(employer);
    }
}
