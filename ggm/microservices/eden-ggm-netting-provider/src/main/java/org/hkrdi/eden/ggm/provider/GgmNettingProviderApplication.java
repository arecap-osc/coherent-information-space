package org.hkrdi.eden.ggm.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cop.annotation.EnableContextOriented;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@ComponentScan("org.hkrdi.eden.ggm.services")
@EnableJpaRepositories(basePackages = {"org.hkrdi.eden.ggm.repository"})
@EntityScan("org.hkrdi.eden.ggm.repository")
@EnableContextOriented
@EnableAsync
public class GgmNettingProviderApplication {


    public static void main(String[] args) {
        SpringApplication.run(GgmNettingProviderApplication.class, args);
    }

}