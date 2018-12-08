package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.exception.NotFoundException;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerController.class);

    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWorker(@RequestBody Worker worker) {
        workerService.persist(worker);
        LOG.debug("Created worker {}.", worker);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", worker.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Worker getWorker(@PathVariable("id") Integer id) {
        final Worker worker = workerService.find(id);
        if (worker == null) {
            throw NotFoundException.create("Worker", id);
        }
        return worker;
    }

}
