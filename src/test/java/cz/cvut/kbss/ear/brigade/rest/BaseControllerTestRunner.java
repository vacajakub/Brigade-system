package cz.cvut.kbss.ear.brigade.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.ear.brigade.rest.handler.RestExceptionHandler;
import cz.cvut.kbss.ear.eshop.environment.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static cz.cvut.kbss.ear.eshop.environment.Environment.createDefaultMessageConverter;
import static cz.cvut.kbss.ear.eshop.environment.Environment.createStringEncodingMessageConverter;
import static org.junit.Assert.assertEquals;

public class BaseControllerTestRunner {

    ObjectMapper objectMapper;

    MockMvc mockMvc;

    public void setUp(Object controller) {
        setupObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestExceptionHandler())
                                      .setMessageConverters(createDefaultMessageConverter(),
                                              createStringEncodingMessageConverter())
                                      .setUseSuffixPatternMatch(false)
                                      .build();
    }

    void setupObjectMapper() {
        this.objectMapper = Environment.getObjectMapper();
    }

    String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    <T> T readValue(MvcResult result, Class<T> targetType) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), targetType);
    }

    <T> T readValue(MvcResult result, TypeReference<T> targetType) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), targetType);
    }

    void verifyLocationEquals(String expectedPath, MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost" + expectedPath, locationHeader);
    }
}
