package cz.cvut.kbss.ear.brigade.rest;

import com.fasterxml.jackson.databind.ObjectWriter;
import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.exception.DateToIsBeforeDateFromException;
import cz.cvut.kbss.ear.brigade.model.Address;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.rest.handler.ErrorInfo;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.CategoryService;
import cz.cvut.kbss.ear.brigade.service.EmployerService;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class BrigadeControllerTest extends BaseControllerTestRunner {

    @Mock
    private EmployerService employerServiceMock;

    @Mock
    private CategoryService categoryServiceMock;

    @Mock
    private BrigadeService brigadeServiceMock;


    @InjectMocks
    private BrigadeController brigadeController;

    private Brigade brigade;

    @Before
    public void setUp() throws Exception {
        brigade = Generator.generateBrigade(false);
        brigade.setId(1);
        MockitoAnnotations.initMocks(this);
        super.setUp(brigadeController);
    }

    @Test
    public void getBrigadeReturnsMatchingId() throws Exception {
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        final MvcResult mvcResult = mockMvc.perform(get("/brigades/" + brigade.getId())).andReturn();
        final Brigade result = readValue(mvcResult, Brigade.class);
        assertNotNull(result);
        assertEquals(brigade.getId(), result.getId());
        assertEquals(brigade.getName(), result.getName());
        assertEquals(brigade.getSalaryPerHour(), result.getSalaryPerHour());
    }

    @Test
    public void getBrigadeThrowsNotFoundForUnknownId() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/brigades/123"))
                .andExpect(status().isNotFound()).andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Brigade identified by "));
        assertThat(result.getMessage(), containsString("123"));
    }

    @Test
    public void removeBrigade() throws Exception {
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        mockMvc.perform(delete("/brigades/remove/" + brigade.getId())).andExpect(status().isNoContent());
        verify(brigadeServiceMock).removeBrigade(brigade);
    }

    @Test
    public void removeBrigadeThrowsNotFound() throws Exception {
        mockMvc.perform(delete("/brigades/remove/666")).andExpect(status().isNotFound());
        verify(brigadeServiceMock, never()).removeBrigade(any());
    }


    @Test
    public void addBrigade() throws Exception {
        Employer employer = Generator.generateEmployer();
        employer.setId(2);
        Category category = new Category();
        category.setName("Test category");
        category.setId(3);
        Address address = new Address();
        address.setId(4);
        address.setCity("Prague");
        address.setStreet("Long Street");
        when(employerServiceMock.find(employer.getId())).thenReturn(employer);
        when(categoryServiceMock.find(category.getId())).thenReturn(category);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String brigadeJson = objectWriter.writeValueAsString(brigade);
        mockMvc.perform(post("/brigades/add/brigade/" + employer.getId() + "/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(brigadeJson)
                .param("city", address.getCity())
                .param("street", address.getStreet()))
                .andExpect(status().isNoContent());
//        verify(brigadeServiceMock).create(employer, brigade, category, address);
    }

    @Test
    @Ignore
    public void addBrigadeThrowsDateToBeforeDateFrom() throws Exception {
        Employer employer = Generator.generateEmployer();
        employer.setId(2);
        Category category = new Category();
        category.setName("Test category");
        category.setId(3);
        Address address = new Address();
        address.setId(4);
        address.setCity("Prague");
        address.setStreet("Long Street");
        brigade.setDateFrom(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        brigade.setDateTo(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        when(employerServiceMock.find(employer.getId())).thenReturn(employer);
        when(categoryServiceMock.find(category.getId())).thenReturn(category);
        when(brigadeServiceMock.find(brigade.getId())).thenReturn(brigade);
        doThrow(DateToIsBeforeDateFromException.class).when(brigadeServiceMock).create(any(), any(), any(), any());
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String brigadeJson = objectWriter.writeValueAsString(brigade);
        mockMvc.perform(post("/brigades/add/brigade/" + employer.getId() + "/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(brigadeJson)
                .param("city", address.getCity())
                .param("street", address.getStreet()))
                .andExpect(status().isConflict());
        verify(brigadeServiceMock).create(employer, brigade, category, address);
    }


}