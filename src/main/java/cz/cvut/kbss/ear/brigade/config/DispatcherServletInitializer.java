package cz.cvut.kbss.ear.brigade.config;

import cz.cvut.kbss.ear.brigade.security.SecurityConstants;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * This class is called when our project is deployed into an application server - the servers have hooks for such
 * cases.
 * <p>
 * It initializes Spring context and starts building beans according to our configuration
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    /**
     * This specifies URL paths where the Spring dispatcher servlet will be listening.
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/rest/*", "/static/*"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("****** Application Context Initialization ******");

        initSecurityFilter(servletContext);
        servletContext.addListener(new RequestContextListener());
        servletContext.getSessionCookieConfig().setName(SecurityConstants.SESSION_COOKIE_NAME);
        super.onStartup(servletContext);
    }

    private void initSecurityFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic securityFilter = servletContext.addFilter("springSecurityFilterChain",
                DelegatingFilterProxy.class);
        final EnumSet<DispatcherType> es = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        securityFilter.addMappingForUrlPatterns(es, true, "/*");
    }
}
