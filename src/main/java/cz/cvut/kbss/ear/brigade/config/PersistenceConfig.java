package cz.cvut.kbss.ear.brigade.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySources(
        {@PropertySource("classpath:jpa.properties"),
                @PropertySource("classpath:jdbc.properties")})
@ComponentScan(basePackages = "cz.cvut.kbss.ear.brigade.dao.implementations")
public class PersistenceConfig {

    private final Environment environment;

    public PersistenceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(name = "ear-brigade-ds")
    public DataSource dataSource() {
        final BoneCPDataSource ds = new BoneCPDataSource();
        ds.setDriverClass("org.postgresql.Driver");
//        ds.setJdbcUrl("jdbc:postgresql://kbss.felk.cvut.cz:5433/ear2018zs_10");
//        ds.setUsername("ear2018zs_10");
//        ds.setPassword("zirafa");
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/ear_localhost");
        ds.setUsername("sedlifil");
        ds.setPassword("sedlifil");
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
        emf.setPackagesToScan("cz.cvut.kbss.ear.brigade.model");

        final Properties props = new Properties();
        props.setProperty("databasePlatform", "org.eclipse.persistence.platform.database.PostgreSQLPlatform");
        props.setProperty("generateDdl", "true");
        props.setProperty("showSql", "true");
        props.setProperty("eclipselink.weaving", "static");
        props.setProperty("eclipselink.ddl-generation", "create-tables");
       props.setProperty("eclipselink.logging.level", "INFO");
        props.setProperty("eclipselink.logging.parameters", "true");
        emf.setJpaProperties(props);
        return emf;
    }

    @Bean(name = "txManager")
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
