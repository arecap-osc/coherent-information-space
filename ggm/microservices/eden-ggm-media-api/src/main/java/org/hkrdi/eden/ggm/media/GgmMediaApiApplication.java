package org.hkrdi.eden.ggm.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cop.annotation.EnableContextOriented;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@ComponentScan("org.hkrdi.eden.ggm.service")
@EnableJpaRepositories(basePackages = {"org.hkrdi.eden.ggm.repository"})
@EntityScan({"org.hkrdi.eden.ggm.repository", "org.hkrdi.eden.ggm.repository.ggm", "org.hkrdi.eden.ggm.repository.grid"})
@EnableContextOriented
@EnableAsync
public class GgmMediaApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(GgmMediaApiApplication.class, args);
	}

}