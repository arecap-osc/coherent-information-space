package org.arecap.eden.ia.console.featureflag;

import org.ff4j.FF4j;
import org.ff4j.cache.FeatureCacheProviderEhCache;
import org.ff4j.spring.boot.web.api.config.EnableFF4jSwagger;
import org.ff4j.springjdbc.store.EventRepositorySpringJdbc;
import org.ff4j.springjdbc.store.FeatureStoreSpringJdbc;
import org.ff4j.springjdbc.store.PropertyStoreSpringJdbc;
import org.ff4j.web.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableFF4jSwagger
public class FF4JConfiguration {

	@Autowired
	private DataSource dataSource;

	@Bean
	public FF4j getFF4j() {
		FF4j ff4j = new FF4j();

		ff4j.setFeatureStore(new FeatureStoreSpringJdbc(dataSource));
		ff4j.setPropertiesStore(new PropertyStoreSpringJdbc(dataSource));
		ff4j.setEventRepository(new EventRepositorySpringJdbc(dataSource));

		ff4j.cache(new FeatureCacheProviderEhCache());
//		ff4j.setAutocreate(true);
		ff4j.audit(true);

		ApiConfig apiCfg= new ApiConfig(ff4j);
		apiCfg.setAuthenticate(true);
		apiCfg.setAutorize(true);

		Set < String > setofRoles = new HashSet<>();
		setofRoles .add("USER");
		setofRoles .add("ADMIN");
		
		apiCfg.createUser("iaas.noian@gmail.com", "", true , true , setofRoles );
		return ff4j;
	}
}