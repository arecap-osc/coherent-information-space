package org.hkrdi.eden.ggm.vaadin.console.etl.data.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hkrdi.eden.ggm.repository.common.documentimage.DocumentImageJpaService;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage.Source;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.repository.EtlRepository;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.spring.annotation.UIScope;

@Service
@UIScope
public class ETLRCService {
	
	@Autowired
	private EtlRepository etlRepository;
	
	@Autowired
	private ETLv1Service etLv1Service;
	
	@Autowired
	private ETLv2Service etLv2Service;
	
	@Autowired
	private ETLFromDBService eTLFromDBService;
	
	@Autowired
	private ApplicationDataRepositoryService applicationDataService;
	
	@Autowired
	private ApplicationRepositoryService applicationService;
	
	@Autowired
	private DocumentImageJpaService documentImageJpaService;

	public static int getLevel(String network) {
		if (network == "SUSTAINABLE_VERTICES::0") return 0;
		int deep_ = new Integer(network.split("\\::")[1]) - 1;
		if (network.startsWith("SUSTAINABLE_EDGES::")) return deep_*4+1;
		if (network.startsWith("SUSTAINABLE_VERTICES::")) return deep_*4+2;
		if (network.startsWith("METABOLIC_VERTICES::")) return deep_*4+3;
		return deep_*4+4;
    }

	@Transactional
	public Integer etl(Application application, InputStream is, String fileName)throws IOException, JAXBException{
		try {
			persistFileToDb(is, fileName, application);
			etLv1Service.etl(etlRepository, applicationDataService, applicationService, application, is);
			return 1;
		} catch (JAXBException e) {
			is.reset();
			//try v2 format
			etLv2Service.etl(etlRepository, applicationDataService, applicationService, application, is);
			return 2;
		}
	}
	
	private void persistFileToDb(InputStream is, String fileName, Application application) throws IOException {
		byte[] contentInBytes = IOUtils.toByteArray(is);
		is.reset();
		documentImageJpaService.save(new DocumentImage(
				application.getId(),
				fileName, 
				LocalDateTime.now(), 
				contentInBytes, 
				application.getId(),//???
				application.getId(),
				"application_import",
				Source.ATTACHED
				));
	}
	
	@Transactional
	public void reimport(Application application)throws IOException, JAXBException{
		eTLFromDBService.etl(etlRepository, applicationDataService, applicationService, application, null);
	}
	
//	public List<String> findAllDescriptionsBySemioticsAndNotUsedInApplication(Semiotics semiotics){
//		return etlRepository.findAllBySemioticsAndUsedInApplication(semiotics, false).stream().map(etl->etl.getDescription()).collect(Collectors.toList());
//	}
	
	public Stream<Etl> findAllBySemiotics(Application application){
		return etlRepository.findAllByApplication(application);
	}

	public List<Etl> findAllByApplicationAndNotUsedInApplication(Application application, Integer level){
		return etlRepository.findAllByApplicationAndUsedInApplicationAndLevel(application, Boolean.FALSE, level);
	}

	public List<Etl> findAllByApplicationAndUsedInApplication(Application application, Integer level){
		return etlRepository.findAllByApplicationAndUsedInApplicationAndLevel(application, Boolean.TRUE, level);
	}
	
	@Transactional
	public void persistEtlUsedInApplication(Etl etlNew, Integer node){
		Etl etl = etlRepository.findById(etlNew.getId()).get();
		etl.setNode(node);
		etl.setUsedInApplication(true);
		etlRepository.save(etl);
	}
	
	@Transactional
	public void persistEtlNotUsedInApplication(Etl etlNew, Integer node){
		Etl etl = etlRepository.findById(etlNew.getId()).get();
		etl.setNode(null);
		etl.setUsedInApplication(false);
		etlRepository.save(etl);
	}
}
