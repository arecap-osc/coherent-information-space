package org.hkrdi.eden.ggm.vaadin.console.featureflag;

import org.ff4j.FF4j;
import org.ff4j.spring.boot.autoconfigure.FF4JConfiguration;
import org.ff4j.web.FF4jDispatcherServlet;
import org.ff4j.web.embedded.ConsoleServlet;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(FF4JConfiguration.class)
public class FF4JWebConfiguration extends SpringBootServletInitializer {

	//secure web console with https://github.com/ff4j/ff4j/wiki/Web-Concepts#web-console
	@Bean
	public ServletRegistrationBean servletRegistrationBean(ConsoleServlet ff4jConsoleServlet) {
		return new ServletRegistrationBean(ff4jConsoleServlet, "/ff4j-console");
	}

	@Bean
	@ConditionalOnMissingBean
	public ConsoleServlet getFF4jServlet(FF4j ff4j) {
		ConsoleServlet ff4jConsoleServlet = new ConsoleServlet();
		ff4jConsoleServlet.setFf4j(ff4j);
		return ff4jConsoleServlet;
	}

	@Bean
	public ServletRegistrationBean ff4jDispatcherServletRegistrationBean(FF4jDispatcherServlet ff4jDispatcherServlet) {
		return new ServletRegistrationBean(ff4jDispatcherServlet, "/ff4j-web-console/*");
	}

	@Bean
	@ConditionalOnMissingBean
	public FF4jDispatcherServlet getFF4jDispatcherServlet(FF4j ff4j) {
		FF4jDispatcherServlet ff4jConsoleServlet = new FF4jDispatcherServlet();
		ff4jConsoleServlet.setFf4j(ff4j);
		return ff4jConsoleServlet;
	}
}