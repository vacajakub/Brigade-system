package cz.cvut.kbss.ear.brigade.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.exception.LateSignOffException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import cz.cvut.kbss.ear.brigade.util.Constants;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class WorkerControllerTest extends BaseControllerTestRunner {


    @Mock
    private WorkerService workerServiceMock;

    @Mock
    private BrigadeService brigadeServiceMock;

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
    public void getFutureBrigadesReturnsCorrectBrigades() throws Exception {
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
    public void getPastBrigadesReturnsCorrectBrigades() throws Exception {
        worker.setId(1);
        final List<Brigade> brigades = IntStream.range(0, 5).mapToObj(i -> Generator.generateBrigade(true)).collect(
                Collectors.toList());
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(workerServiceMock.getPastBrigades(worker)).thenReturn(brigades);
        final MvcResult mvcResult = mockMvc.perform(get("/workers/1/brigades/past")).andReturn();
        final List<Brigade> result = readValue(mvcResult, new TypeReference<List<Brigade>>() {
        });
        assertNotNull(result);
        assertEquals(brigades.size(), result.size());
        for (int i = 0; i < brigades.size(); i++) {
            assertEquals(brigades.get(i).getId(), result.get(i).getId());
        }
    }

    @Ignore
    @Test
    public void getWorkerScoreReturnsCorrect() throws Exception {
        worker.setId(1);
        final Pair<Integer, Integer> workerScore = new Pair<>(3, 2);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(workerServiceMock.getWorkerScore(worker)).thenReturn(workerScore);
        final MvcResult mvcResult = mockMvc.perform(get("/workers/1/score")).andReturn();
        final Pair result = readValue(mvcResult, Pair.class);
        Assert.assertNotNull(result);
        assertEquals(workerScore.getKey(), result.getKey());
        assertEquals(workerScore.getValue(), result.getValue());
    }


    @Test
    public void singOnToBrigade() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        worker.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        mockMvc.perform(post("/workers/signOn/brigade/1/2")).andExpect(status().isNoContent());
        verify(workerServiceMock).singOnToBrigade(worker, brigade);
    }

    @Test
    public void singOnToBrigadeThrowsNotFound() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        worker.setId(1);
        when(workerServiceMock.find(any())).thenReturn(worker);
        mockMvc.perform(post("/workers/signOn/brigade/1/66")).andExpect(status().isNotFound());
        verify(workerServiceMock, never()).singOnToBrigade(any(), any());
    }

    @Test
    public void singOffFromBrigade() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        brigade.setDateFrom(new Date(System.currentTimeMillis() + Constants.ONE_DAY * 2));
        worker.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        mockMvc.perform(delete("/workers/signOff/brigade/1/2")).andExpect(status().isNoContent());
        verify(workerServiceMock).singOffFromBrigade(worker, brigade);
    }

    @Test
    public void singOffFromBrigadeThrowsNotFound() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        brigade.setDateFrom(new Date(System.currentTimeMillis() + Constants.ONE_DAY * 2));
        worker.setId(1);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        mockMvc.perform(delete("/workers/signOff/brigade/69/2")).andExpect(status().isNotFound());
        verify(workerServiceMock, never()).singOffFromBrigade(any(), any());
    }


    @Test
    public void singOffFromBrigadeThrowsTooLate() throws Exception {
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setId(2);
        brigade.setDateFrom(new Date(System.currentTimeMillis()));
        worker.setId(1);
        when(workerServiceMock.find(worker.getId())).thenReturn(worker);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        doThrow(LateSignOffException.class).when(workerServiceMock).singOffFromBrigade(worker, brigade);
        mockMvc.perform(delete("/workers/signOff/brigade/1/2")).andExpect(status().isConflict());
        verify(workerServiceMock).singOffFromBrigade(worker, brigade);
    }

}