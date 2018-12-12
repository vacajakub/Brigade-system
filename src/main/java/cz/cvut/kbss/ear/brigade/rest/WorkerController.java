package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.AuthenticationToken;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/workers")
public class WorkerController {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerController.class);

    private final WorkerService workerService;
    private final BrigadeService brigadeService;

    @Autowired
    public WorkerController(WorkerService workerService, BrigadeService brigadeService) {
        this.workerService = workerService;
        this.brigadeService = brigadeService;
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public AuthenticationToken getToken() {
        final Worker worker = new Worker();
        worker.setFirstName("FirstName");
        worker.setLastName("LastName");
        worker.setEmail("username" + "@kbss.felk.cvut.cz");
        worker.setPassword("23224");
        return SecurityUtils.setCurrentUser(new UserDetails(worker));
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWorker(@RequestBody Worker worker) {
        workerService.persist(worker);
        LOG.debug("Created worker {}.", worker);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", worker.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Worker> getWorkers() {
        final List<Worker> workers = workerService.findAll();
        LOG.debug("Called getWorkers() method.");
        if (workers == null) {
            throw NotFoundException.create("Workers", "findAll()");
        }
        return workers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Worker getWorker(@PathVariable("id") Integer id) {
        final Worker worker = findWorker(id);
        LOG.debug("Returned worker with id {}.", worker.getId());
        return worker;
    }

    @RequestMapping(value = "/{id}/brigades/future", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getFutureBrigades(@PathVariable("id") Integer id) {
        Worker worker = findWorker(id);
        LOG.debug("Returned future brigades of worker with id {}.", worker.getId());
        return workerService.getFutureBrigades(worker);
    }

    @RequestMapping(value = "/{id}/brigades/past", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getPastBrigades(@PathVariable("id") Integer id) {
        Worker worker = findWorker(id);
        LOG.debug("Returned past brigades of worker with id {}.", worker.getId());
        return workerService.getPastBrigades(worker);
    }

    @RequestMapping(value = "/signOn/brigade/{workerId}/{brigadeId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOnToBrigade(@PathVariable("brigadeId") Integer brigadeId,
                                @PathVariable("workerId") Integer workerId) {
        Worker worker = findWorker(workerId);
        Brigade brigade = findBrigade(brigadeId);
        LOG.debug("Signed on worker {} to brigade {}.", worker, brigade);
        workerService.singOnToBrigade(worker, brigade);
    }

    @RequestMapping(value = "/signOff/brigade/{workerId}/{brigadeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOffFromBrigade(@PathVariable("workerId") Integer workerId, @PathVariable("brigadeId") Integer brigadeId) {
        Worker worker = findWorker(workerId);
        Brigade brigade = findBrigade(brigadeId);
        LOG.debug("Signed of worker {} from brigade {}.", worker, brigade);
        workerService.singOffFromBrigade(worker, brigade);
    }

    @RequestMapping(value = "/add/thumbsUp/brigade/{workerId}/{brigadeId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addThumbsUpBrigade(@PathVariable("workerId") Integer workerId, @PathVariable("brigadeId") Integer brigadeId) {
        Worker worker = findWorker(workerId);
        Brigade brigade = findBrigade(brigadeId);
        LOG.debug("Worker {} added thumbsup to brigade {}.", worker, brigade);
        workerService.addThumbsUpToBrigade(worker, brigade);
    }

    @RequestMapping(value = "/add/thumbsDown/brigade/{workerId}/{brigadeId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addThumbsDownBrigade(@PathVariable("workerId") Integer workerId, @PathVariable("brigadeId") Integer brigadeId) {
        Worker worker = findWorker(workerId);
        Brigade brigade = findBrigade(brigadeId);
        LOG.debug("Worker {} added thumbsdown to brigade {}.", worker, brigade);
        workerService.addThumbsDownToBrigade(worker, brigade);
    }

    @RequestMapping(value = "/{id}/score", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Pair<Integer, Integer> getWorkerScore(@PathVariable("id") Integer id) {
        Worker worker = findWorker(id);
        LOG.debug("Returned worker score for worker {}", worker);
        return workerService.getWorkerScore(worker);
    }


    private Worker findWorker(Integer id) {
        Worker worker = workerService.find(id);
        if (worker == null) {
            throw NotFoundException.create("Worker", id);
        }
        return worker;
    }

    private Brigade findBrigade(Integer id) {
        Brigade brigade = brigadeService.find(id);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", id);
        }
        return brigade;
    }


}
















