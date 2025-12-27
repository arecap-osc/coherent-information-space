package org.hkrdi.eden.ggm.provider.services;

import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.provider.conversion.DataMapService;
import org.hkrdi.eden.ggm.repository.DataMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NetworkLoader {

    @Autowired
    private NetworkBuilderService networkBuilder;

    @Autowired
    private DataMapService dataMapService;
    
    @Autowired
    private JdbcTemplate jdbc;

    @Value("${hexavalent.logic.deep.max}")
    private int deepMax;

    @PostConstruct
    protected void buildDataMap() {
		networkBuilder.buildSomething(NettingType.SUSTAINABLE_VERTICES, 0);
        networkBuilder.buildSomething(NettingType.METABOLIC_VERTICES, 0);
//        networkBuilder.buildSomething(NettingType.SUSTAINABLE_EDGES, 0);
//        networkBuilder.buildSomething(NettingType.SUSTAINABLE_EDGES, 1);
    	for (int deep = 1; deep <= deepMax; deep++) {
	        networkBuilder.buildCacheDeep(deep);
    	}
    	collectStatistics();
    }
    
    private void collectStatistics() {
    	jdbc.execute("analyze");
    }

}
