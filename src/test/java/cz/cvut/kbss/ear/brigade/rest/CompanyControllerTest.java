package cz.cvut.kbss.ear.brigade.rest;

import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.model.Company;
import cz.cvut.kbss.ear.brigade.service.CompanyService;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class CompanyControllerTest extends BaseControllerTestRunner {


    @Mock
    private CompanyService companyServiceMock;

    @InjectMocks
    private CompanyController companyController;

    private Company company;

    @Before
    public void setUp() throws Exception {
        company = Generator.generateCompany();
        company.setId(1);
        MockitoAnnotations.initMocks(this);
        super.setUp(companyController);
    }

    @Test
    public void createCompany() throws Exception {
        mockMvc.perform(post("/companies").content(toJson(company)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        final ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyServiceMock).persist(captor.capture());
        assertEquals(company.getName(), captor.getValue().getName());
    }

    @Test
    public void remove() throws Exception {
        when(companyServiceMock.find(company.getId())).thenReturn(company);
        mockMvc.perform(delete("/companies/" + company.getId())).andExpect(status().isNoContent());
        verify(companyServiceMock).remove(company);
    }
}