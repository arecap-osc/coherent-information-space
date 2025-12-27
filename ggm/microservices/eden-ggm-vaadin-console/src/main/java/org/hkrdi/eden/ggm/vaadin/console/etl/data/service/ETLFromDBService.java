package org.hkrdi.eden.ggm.vaadin.console.etl.data.service;

import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.repository.EtlRepository;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
@UIScope
public class ETLFromDBService implements EtlService{
	
	@Transactional
	public void persistEtl(EtlRepository etlRepository,  Etl etl, ApplicationRepositoryService applicationService ) {
		etl.setApplication(applicationService.getApplicationById(etl.getApplication().getId()));
		etlRepository.save(etl);
	}
	

	public EtlService originalEtlService(EtlRepository etlRepository, ApplicationRepositoryService applicationService,
			Application application) {
		Assert.notNull(application.getOriginalApplication());
		
		Application originalSSemiotic = applicationService.getApplicationById(application.getOriginalApplication());
		switch (originalSSemiotic.getEtlVersion()) {
			case 1: return new ETLv1Service();
			case 2: return new ETLv2Service();
			default: return new ETLv2Service();
		}
	}
	
	@Transactional
	public void etl(EtlRepository etlRepository, ApplicationDataRepositoryService applicationDataService, ApplicationRepositoryService applicationService,
					Application newApplication, InputStream is) throws FileNotFoundException, JAXBException{
		
		Application originalApplication = applicationService.getApplicationById(newApplication.getOriginalApplication());
		EtlService originalEtlService = originalEtlService(etlRepository, applicationService, newApplication);
		
		etlRepository.findAllByApplication(originalApplication).forEach(oldEtl->{
			String log = null;
//			Integer mappedToNode = originalEtlService.mapping(oldEtl.getLevel(), oldEtl.getOriginalNode());
			
			Integer mappedToNode = oldEtl.getNode();
			if (mappedToNode!=null) {
				try {
					persistSemantics(applicationDataService, new Etl(newApplication, oldEtl.getLevel(), mappedToNode, 
							oldEtl.getBrief(), oldEtl.getLabel(), oldEtl.getDescription()));
					log = "Original application["+originalApplication.getId()+"]"+" original etl["+oldEtl.getId()+"]";
				}catch(Exception e) {
					log = "Failed: "+e.getMessage();
				}
			}else {
				log = "Skip: node not mapped";
			}
			
			persistEtl(etlRepository, new Etl(newApplication, oldEtl.getLevel(), mappedToNode, 
					oldEtl.getBrief(), oldEtl.getLabel(), oldEtl.getDescription(), log, oldEtl.getOriginalNode(), false), applicationService);
		});
		
	}
	
}
