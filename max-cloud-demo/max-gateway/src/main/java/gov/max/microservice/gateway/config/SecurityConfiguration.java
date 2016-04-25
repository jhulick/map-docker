package gov.max.microservice.gateway.config;

import gov.max.microservice.gateway.filter.TokenGeneratorFilter;
import gov.max.microservice.gateway.security.CustomAccessDeniedHandler;
import gov.max.microservice.gateway.security.CustomUserDetailsService;
import gov.max.microservice.gateway.security.jwt.TokenAuthenticationService;
import gov.max.microservice.gateway.security.jwt.UserService;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Environment env;

    @Value("${cas.service.login}")
    private String casUrlLogin;

    @Value("${cas.service.logout}")
    private String casUrlLogout;

    @Value("${cas.url.prefix}")
    private String casUrlPrefix;

    @Value("${app.service.security}")
    private String casServiceUrl;

    @Value("${app.service.home}")
    private String appServiceHome;

    @Value("${app.admin.userName}")
    private String appAdminUserName;

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserService userService;

    public SecurityConfiguration() {
        super(false);
        this.userService = new UserService();
        this.tokenAuthenticationService = new TokenAuthenticationService("tooManySecrets", userService);
    }

    @Bean
    public Set<String> adminList() {
        Set<String> admins = new HashSet<>();
        String adminUserName = appAdminUserName;

        admins.add("admin");
        if (adminUserName != null && !adminUserName.isEmpty()) {
            admins.add(adminUserName);
        }
        return admins;
    }

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties sp = new ServiceProperties();
        sp.setService(casServiceUrl);
        sp.setSendRenew(false);
        return sp;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
        return casAuthenticationProvider;
    }

    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
        return new CustomUserDetailsService(adminList());
    }

    @Bean
    public SessionAuthenticationStrategy sessionStrategy() {
        SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
        return sessionStrategy;
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casUrlPrefix);
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casUrlLogin);
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casUrlPrefix);
        return singleSignOutFilter;
    }

    @Bean
    public LogoutFilter requestCasGlobalLogoutFilter() {
        String logoutUrl = casUrlLogout + "?service=" + appServiceHome;
        LogoutFilter logoutFilter = new LogoutFilter(logoutUrl, new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout");
        logoutFilter.setFilterProcessesUrl("/j_spring_cas_security_logout");
        logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
        return logoutFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/app/**")
            .antMatchers("/bower_components/**")
            .antMatchers("/content/**")
            .antMatchers("/fonts/**")
            .antMatchers("/images/**")
            .antMatchers("/scripts/**")
            .antMatchers("/styles/**")
            .antMatchers("/views/**")
            .antMatchers("/i18n/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilter(casAuthenticationFilter())
            .exceptionHandling()
            .accessDeniedHandler(new CustomAccessDeniedHandler())
            .authenticationEntryPoint(casAuthenticationEntryPoint())

        .and()
            .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
            .addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class)
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        .and()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .authorizeRequests()
            .antMatchers("/index.html", "/").authenticated()
            .anyRequest().authenticated()
        .and()
            .csrf()
            .csrfTokenRepository(csrfTokenRepository())
        .and()
            .addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class)
            .addFilterAfter(new TokenGeneratorFilter(tokenAuthenticationService, userService), LogoutFilter.class);
    }

    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
