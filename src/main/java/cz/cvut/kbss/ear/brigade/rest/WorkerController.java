package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
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
        if (workers == null) {
            throw NotFoundException.create("Workers", "findAll()");
        }
        return workers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Worker getWorker(@PathVariable("id") Integer id) {
        final Worker worker = workerService.find(id);
        if (worker == null) {
            throw NotFoundException.create("Worker", id);
        }
        return worker;
    }

    @RequestMapping(value = "/{id}/brigades/future", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getFutureBrigades(@PathVariable("id") Integer id) {
        Worker worker = workerService.find(id);
        if (worker == null) {
            throw NotFoundException.create("Worker", id);
        }
        return workerService.getFutureBrigades(worker);
    }

    @RequestMapping(value = "/{id}/brigades/past", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Brigade> getPastBrigades(@PathVariable("id") Integer id) {
        Worker worker = workerService.find(id);
        if (worker == null) {
            throw NotFoundException.create("Worker", id);
        }
        return workerService.getPastBrigades(worker);
    }

    @RequestMapping(value = "/signOff/brigade/{workerId}/{brigadeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOffFromBrigade(@PathVariable("workerId") Integer workerId, @PathVariable("brigadeId") Integer brigadeId) {
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }
        workerService.singOffFromBrigade(worker, brigade);
    }

    @RequestMapping(value = "/add/thumbsUp/brigade/{workerId}/{brigadeId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addThumbsUpBrigade(@PathVariable("workerId") Integer workerId, @PathVariable("brigadeId") Integer brigadeId) {
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }

        workerService.addThumbsUpToBrigade(worker, brigade);
    }

    @RequestMapping(value = "/add/thumbsDown/brigade/{workerId}/{brigadeId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addThumbsDownBrigade(@PathVariable("workerId") Integer workerId, @PathVariable("brigadeId") Integer brigadeId) {
        Worker worker = workerService.find(workerId);
        if (worker == null) {
            throw NotFoundException.create("Worker", workerId);
        }
        Brigade brigade = brigadeService.find(brigadeId);
        if (brigade == null) {
            throw NotFoundException.create("Brigade", brigadeId);
        }

        workerService.addThumbsDownToBrigade(worker, brigade);
    }

    @RequestMapping(value = "/{id}/score", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Pair<Integer, Integer> getWorkerScore(@PathVariable("id") Integer id) {
        Worker worker = workerService.find(id);
        if (worker == null) {
            throw NotFoundException.create("Worker", id);
        }
        return workerService.getWorkerScore(worker);
    }
}
















