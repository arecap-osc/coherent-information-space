package org.hkrdi.eden.ggm.vaadin.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.cop.annotation.EnableContextOriented;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.exchange.annotation.EnableWebExchange;
import org.vaadin.spring.events.annotation.EnableEventBus;

import com.vaadin.flow.server.VaadinServletConfiguration;

//import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

//@SpringBootApplication(exclude = ThymeleafAutoConfiguration.class)
@VaadinServletConfiguration(productionMode = false, /* closeIdleSessions = true, */heartbeatInterval = 10)
@SpringBootApplication //(exclude = ErrorMvcAutoConfiguration.class) this is as a bug fix in
// vaadin 14 https://vaadin.com/tutorials/securing-your-app-with-spring-security/setting-up-spring-security
//https://vaadin.com/forum/thread/17784869/vaadin-14-with-spring-security-login-page-not-loading
@EnableContextOriented
@EnableWebExchange
@EnableEventBus
@ComponentScan
@ComponentScan(basePackages = {"org.hkrdi.eden.ggm.repository",
		"org.hkrdi.eden.ggm.vaadin.console.microservice", "org.hkrdi.eden.ggm.vaadin.console.common", 
		"org.hkrdi.eden.ggm.vaadin.console.featureflag"})
@EnableJpaRepositories(basePackages = {"org.hkrdi.eden.ggm.repository","org.hkrdi.eden.ggm.vaadin.console.etl.data.repository", 
		"org.hkrdi.eden.ggm.vaadin.console.microservice", "org.hkrdi.eden.ggm.vaadin.console.boot"} ,
		repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
		)
@EntityScan({"org.hkrdi.eden.ggm.repository", "org.hkrdi.eden.ggm.vaadin.console.microservice", "org.hkrdi.eden.ggm.vaadin.console.boot"})
@PropertySources({
	@PropertySource("classpath:application.properties"),
	@PropertySource("classpath:auth0.properties")
})	
public class GgmConsoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GgmConsoleApplication.class, args);
	}

	@Bean 
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    } 

}