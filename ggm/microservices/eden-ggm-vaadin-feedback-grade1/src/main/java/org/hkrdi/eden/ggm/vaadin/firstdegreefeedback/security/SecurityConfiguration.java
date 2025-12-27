package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security;

import java.io.UnsupportedEncodingException;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.controller.LogoutController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.auth0.AuthenticationController;

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

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutController();
    }

    @Bean
    public AuthenticationController authenticationController() throws UnsupportedEncodingException {
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
		     // Register our CustomRequestCache that saves unauthorized access attempts, so
	        // the user is redirected after login.
//	        .requestCache().requestCache(new CustomRequestCache())
//	        .and()
	        // Restrict access to our application.
            .authorizeRequests()
            .antMatchers("/VAADIN/**", "/frontend/**").permitAll()
            // Allow all flow internal requests.
            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .mvcMatchers("/callback", "/login", "/info", "/error").permitAll()
            .antMatchers("/callback", "/login", "/info", "/error", "/", "/*.png", "/css/**", "/js/**").permitAll()
            
            // Allow all requests by logged in users.
            .anyRequest().authenticated()
            .and().addFilter(new JwtAuthorizationFilter(authenticationManager(), clientSecret))
//            .and()
            .logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll()
        ;
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
				// Vaadin Flow static resources
				"/VAADIN/**",

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
