package gov.max.microservice.resource;

import gov.max.microservice.resource.config.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@Configuration
@ComponentScan
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableRedisHttpSession
@EnableCircuitBreaker
@RibbonClient("resource")
public class ResourceApplication extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ResourceApplication.class);

    @Inject
    private Environment env;

    public static void main(String[] args) throws UnknownHostException  {
        SpringApplication app = new SpringApplication(ResourceApplication.class);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        addDefaultProfile(app, source);
        Environment env = app.run(args).getEnvironment();
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                 "Local: \t\thttp://127.0.0.1:{}\n\t" +
                 "External: \thttp://{}:{}\n----------------------------------------------------------",
                 env.getProperty("server.port"),
                 InetAddress.getLocalHost().getHostAddress(),
                 env.getProperty("server.port"));
    }

    /**
     * Initializes app.
     *
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                log.error("You have misconfigured your application! " +
                        "It should not run with both the 'dev' and 'prod' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION) && activeProfiles.contains(Constants.SPRING_PROFILE_TEST)) {
                log.error("You have misconfigured your application! " +
                        "It should not run with both the 'prod' and 'test' profiles at the same time.");
            }
        }
    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active") &&
                !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }

    // Security configuration

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Prevent the browser from popping up a dialog on a 401
        http
                .httpBasic().disable();
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
