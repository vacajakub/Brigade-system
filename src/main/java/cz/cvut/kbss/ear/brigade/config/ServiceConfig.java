package cz.cvut.kbss.ear.brigade.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan(basePackages =  "cz.cvut.kbss.ear.eshop.service")
public class ServiceConfig {
}
