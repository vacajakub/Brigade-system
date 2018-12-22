package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.exception.BrigadeNotBelongToEmployerException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.EmployerService;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class EmployerControllerTest extends BaseControllerTestRunner {

    @Mock
    private EmployerService employerServiceMock;

    @Mock
    private WorkerService workerServiceMock;

    @Mock
    private BrigadeService brigadeServiceMock;


    @InjectMocks
    private EmployerController employerController;


    private Employer employer;

    @Before
    public void setUp() throws Exception {
        employer = Generator.generateEmployer();
        MockitoAnnotations.initMocks(this);
        super.setUp(employerController);
    }

    @Test
    public void removeWorkerFromBrigade() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        Worker worker = Generator.generateWorker();
        worker.setId(3);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        employer.addBrigade(brigade);
        employer.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        when(employerServiceMock.find(employer.getId())).thenReturn(employer);
        mockMvc.perform(delete("/employers/" + employer.getId() + "/" + brigade.getId() + "/" + worker.getId()))
                .andExpect(status().isNoContent());
        verify(employerServiceMock).removeWorkerFromBrigade(employer, brigade, worker);
    }

    @Test
    public void removeWorkerFromBrigadeThrowsBrigadeNotBelongToEmployer() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        Worker worker = Generator.generateWorker();
        worker.setId(3);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        employer.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        when(employerServiceMock.find(employer.getId())).thenReturn(employer);
        doThrow(BrigadeNotBelongToEmployerException.class).when(employerServiceMock).removeWorkerFromBrigade(employer, brigade, worker);
        mockMvc.perform(delete("/employers/" + employer.getId() + "/" + brigade.getId() + "/" + worker.getId()))
                .andExpect(status().isForbidden());
        verify(employerServiceMock).removeWorkerFromBrigade(employer, brigade, worker);
    }

    @Test
    public void moveWorkerToBlackList() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        Worker worker = Generator.generateWorker();
        worker.setId(3);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        employer.addBrigade(brigade);
        employer.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        when(employerServiceMock.find(employer.getId())).thenReturn(employer);
        mockMvc.perform(post("/employers/blacklist/" + employer.getId() + "/" + brigade.getId() + "/" + worker.getId()))
                .andExpect(status().isNoContent());
        verify(employerServiceMock).moveWorkerToBlacklist(employer, brigade, worker);
    }

    @Test
    public void moveWorkerToBlackListThrowsBrigadeNotBelongToEmployer() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        Worker worker = Generator.generateWorker();
        worker.setId(3);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        employer.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        when(employerServiceMock.find(employer.getId())).thenReturn(employer);
        doThrow(BrigadeNotBelongToEmployerException.class).when(employerServiceMock).moveWorkerToBlacklist(employer, brigade, worker);
        mockMvc.perform(post("/employers/blacklist/" + employer.getId() + "/" + brigade.getId() + "/" + worker.getId()))
                .andExpect(status().isForbidden());
        verify(employerServiceMock).moveWorkerToBlacklist(employer, brigade, worker);
    }

}