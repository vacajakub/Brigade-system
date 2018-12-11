package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.service.WorkerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class WorkerControllerTest extends BaseControllerTestRunner {



    @Mock
    private WorkerService workerServiceMock;

    @InjectMocks
    private WorkerController workerController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(workerController);
    }

    @Test
    public void createWorker() {
    }

    @Test
    public void getFutureBrigades() {
    }

    @Test
    public void getPastBrigades() {
    }

    @Test
    public void signOffFromBrigade() {
    }

    @Test
    public void addThumbsUpBrigade() {
    }

    @Test
    public void addThumbsDownBrigade() {
    }

    @Test
    public void getWorkerScore() {
    }
}