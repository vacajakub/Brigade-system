package cz.cvut.kbss.ear.brigade.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.rest.util.RestUtils;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MvcResult;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @Ignore
    @Test(expected = AccessDeniedException.class)
    public void getFutureBrigadesThrowsExceptionForDifferentWorker() throws Exception {
        worker.setRole(Role.WORKER);
        worker.setId(1);
        Worker workerOther = Generator.generateWorker();
        workerOther.setRole(Role.WORKER);
        workerOther.setId(2);
        SecurityUtils.setCurrentUser(new UserDetails(workerOther));
        final List<Brigade> brigades = IntStream.range(0, 5).mapToObj(i -> Generator.generateBrigade(false)).collect(
                Collectors.toList());
//        when(workerServiceMock.find(workerOther.getId())).thenReturn(workerOther);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(workerServiceMock.getFutureBrigades(worker)).thenReturn(brigades);
        final MvcResult mvcResult = mockMvc.perform(get("/workers/1/brigades/future")).andReturn();
        final List<Brigade> result = readValue(mvcResult, new TypeReference<List<Brigade>>() {
        });
    }

    @Test
    public void getPastBrigades() {
    }


}