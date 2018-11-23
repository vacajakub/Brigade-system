package cz.cvut.kbss.ear.brigade.config;


import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages =  "cz.cvut.kbss.ear.brigade.service")
public class ServiceConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * {@link RestTemplate} can be used to communicate with web services of another application -
     * see for example <a href="http://www.baeldung.com/rest-template">http://www.baeldung.com/rest-template</a>.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
