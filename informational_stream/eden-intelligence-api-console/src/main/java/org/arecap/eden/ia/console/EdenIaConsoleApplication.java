package org.arecap.eden.ia.console;

import com.vaadin.flow.server.VaadinServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.context.request.RequestContextListener;
import org.vaadin.spring.events.annotation.EnableEventBus;

@VaadinServletConfiguration(productionMode = false, /* closeIdleSessions = true, */heartbeatInterval = 10)
@SpringBootApplication //(exclude = ErrorMvcAutoConfiguration.class)
@EnableEventBus
@ComponentScan
@EntityScan
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:auth0.properties")
})
public class EdenIaConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdenIaConsoleApplication.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

}
