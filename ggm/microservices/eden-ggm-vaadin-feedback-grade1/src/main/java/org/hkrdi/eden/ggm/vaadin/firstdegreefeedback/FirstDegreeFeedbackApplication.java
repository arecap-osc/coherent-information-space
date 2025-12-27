package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback;

import com.vaadin.flow.spring.eventbus.EnableUiEventBus;
import com.vaadin.flow.spring.portalwindow.EnablePortalWindow;
import com.vaadin.flow.spring.template.route.EnableRouteComponents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.cop.annotation.EnableContextOriented;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.exchange.annotation.EnableWebExchange;

@SpringBootApplication
@EnableContextOriented
@EnableUiEventBus
@EnablePortalWindow
@EnableRouteComponents
@EnableWebExchange
@ComponentScan
@ComponentScan("org.hkrdi.eden.ggm")
@EnableJpaRepositories(basePackages = {"org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository"})
@EntityScan({"org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity"})
@PropertySources({
	@PropertySource("classpath:application.properties"),
	@PropertySource("classpath:auth0.properties")
})	
public class FirstDegreeFeedbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstDegreeFeedbackApplication.class, args);
	}

}