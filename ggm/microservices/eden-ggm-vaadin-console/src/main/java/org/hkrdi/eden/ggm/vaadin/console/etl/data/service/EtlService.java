package org.hkrdi.eden.ggm.vaadin.console.etl.data.service;

import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.repository.EtlRepository;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.springframework.cop.support.BeanUtil;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface EtlService {

	default Integer mapping(Integer level, Integer originalNode) {
		return originalNode;
	}
	
	default String getNetworkName(int level) {
		if (level == 0) return "SUSTAINABLE_VERTICES::0";
		if (level%4 == 0) return "SUSTAINABLE_EDGES::"+(level/4+1);
		else if (level%4 == 1) return "SUSTAINABLE_VERTICES::"+(level/4+1);
		else if (level%4 == 2) return "METABOLIC_VERTICES::"+(level/4+1);
		else return "METABOLIC_EDGES::"+(level/4+1);
    }

    //TODO need to be as designed
	default void persistSemantics(ApplicationDataRepositoryService applicationDataService, Etl etl) {
		DataMap dataMap = BeanUtil.getBean(CoherentSpaceService.class).getNetworkDataMaps(getNetworkName(etl.getLevel()))
				.stream().filter(DataMapFilterUtil.byAddressIndex(new Long(etl.getNode()))).findFirst().orElse(null);
		if(dataMap == null) {
			throw new IllegalArgumentException("Node index nod found in network");
		}
		ApplicationData applicationData = applicationDataService.getApplicationData(etl.getApplication().getId(), dataMap);
		applicationData.setSemantic(etl.getDescription());
		applicationData.setSyntax(Optional.ofNullable(applicationData.getSyntax()).orElse(""));
		applicationData.setWhoWhat(Optional.ofNullable(applicationData.getWhoWhat()).orElse(""));
		applicationData.setHow(Optional.ofNullable(applicationData.getHow()).orElse(""));
		applicationData.setWhy(Optional.ofNullable(applicationData.getWhy()).orElse(""));
		applicationData.setWhereWhen(Optional.ofNullable(applicationData.getWhereWhen()).orElse(""));
		applicationDataService.save(applicationData, true);
	}
	
	@Transactional
	default void persistEtl(EtlRepository etlRepository, Etl etl, ApplicationRepositoryService applicationService ) {
		etl.setApplication(applicationService.getApplicationById(etl.getApplication().getId()));
		etlRepository.save(etl);
	}
	
	@Transactional
	default void persistEtlLog(EtlRepository etlRepository,  Etl etl, ApplicationRepositoryService applicationService ) {
		etl.setApplication(applicationService.getApplicationById(etl.getApplication().getId()));
		etlRepository.save(etl);
	}
	
	void etl(EtlRepository etlRepository, ApplicationDataRepositoryService applicationDataService, ApplicationRepositoryService applicationService, Application application, InputStream is) throws IOException, JAXBException;
}
