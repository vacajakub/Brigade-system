package cz.cvut.kbss.ear.brigade.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class WorkerControllerTest extends BaseControllerTestRunner {


    @Mock
    private WorkerService workerServiceMock;

    @InjectMocks
    private WorkerController workerController;

    private Worker worker;

    @Before
    public void setUp() {
        worker = Generator.generateWorker();
        MockitoAnnotations.initMocks(this);
        super.setUp(workerController);
    }


    @Test
    public void getFutureBrigadesReturnForAdmin() throws Exception {
        worker.setRole(Role.ADMIN);
        worker.setId(1);
        SecurityUtils.setCurrentUser(new UserDetails(worker));
        final List<Brigade> brigades = IntStream.range(0, 5).mapToObj(i -> Generator.generateBrigade(false)).collect(
                Collectors.toList());
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(workerServiceMock.getFutureBrigades(worker)).thenReturn(brigades);
        final MvcResult mvcResult = mockMvc.perform(get("/workers/1/brigades/future")).andReturn();
        final List<Brigade> result = readValue(mvcResult, new TypeReference<List<Brigade>>() {
        });
        assertNotNull(result);
        assertEquals(brigades.size(), result.size());
        for (int i = 0; i < brigades.size(); i++) {
            assertEquals(brigades.get(i).getId(), result.get(i).getId());
        }
    }

    @Test
    public void getFutureBrigadesReturnForWorkerWithSameUsername() throws Exception {
        worker.setRole(Role.WORKER);
        worker.setId(1);
        SecurityUtils.setCurrentUser(new UserDetails(worker));
        final List<Brigade> brigades = IntStream.range(0, 5).mapToObj(i -> Generator.generateBrigade(false)).collect(
                Collectors.toList());
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(workerServiceMock.getFutureBrigades(worker)).thenReturn(brigades);
        final MvcResult mvcResult = mockMvc.perform(get("/workers/1/brigades/future")).andReturn();
        final List<Brigade> result = readValue(mvcResult, new TypeReference<List<Brigade>>() {
        });
        assertNotNull(result);
        assertEquals(brigades.size(), result.size());
        for (int i = 0; i < brigades.size(); i++) {
            assertEquals(brigades.get(i).getId(), result.get(i).getId());
        }
    }


    @Test
    public void getPastBrigades() {
    }


}