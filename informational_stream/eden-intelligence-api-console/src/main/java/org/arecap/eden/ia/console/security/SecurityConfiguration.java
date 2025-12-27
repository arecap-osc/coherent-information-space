package org.arecap.eden.ia.console.security;

import com.auth0.AuthenticationController;
import org.arecap.eden.ia.console.security.controller.LogoutController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import java.io.UnsupportedEncodingException;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    @Value(value = "${com.auth0.domain}")
    private String domain;

    /**
     * This is the client id of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;

    @Value("${permissions.default}")
    private String permissionsDefault;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutController();
    }

    @Bean
    public AuthenticationController authenticationController() throws UnsupportedEncodingException {
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .build();
    }

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable();

        http
                // Register our CustomRequestCache that saves unauthorized access attempts, so
                // the user is redirected after login.
                //	        .requestCache().requestCache(new CustomRequestCache())
                //	        .and()
                .requestCache().requestCache(requestCache()).and()
                // Restrict access to our application.
                .authorizeRequests()
                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .mvcMatchers("/iaas", "/paas", "/about", "/nl", "/nl/*", "/graphs", "/graphs/*").permitAll() //noian landing pages
                .mvcMatchers("/callback", "/login", "/info", "/error","/ip").permitAll()
                .antMatchers("/callback", "/login", "/info", "/error", "/", "/*.png", "/css/**", "/js/**","/ip").permitAll()
                // Allow all requests by logged in users.
                .anyRequest().authenticated()
                .and().addFilter(new CookieJwtAuthorizationFilter(authenticationManager()))
                	   .addFilter(new JwtAuthorizationFilter(authenticationManager(), clientSecret, permissionsDefault))
                //            .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll();
    }

    //    @Bean
    //	@Override
    //	public UserDetailsService userDetailsService() {
    //		UserDetails user =
    //				User.withUsername("user")
    //						.password("{noop}password")
    //						.roles("USER")
    //						.build();
    //
    //		return new InMemoryUserDetailsManager(user);
    //	}

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
        		"/ip/**",
        		
                // Vaadin Flow static resources
                "/VAADIN/**",

                "/actuator/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // icons and images
                "/icons/**",
                "/images/**",

                // (development mode) static resources
                "/frontend/**",

                // (development mode) webjars
                "/webjars/**",

                // (development mode) H2 debugging console
                "/h2-console/**",

                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**");
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
