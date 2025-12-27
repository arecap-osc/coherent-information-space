package org.hkrdi.eden.ggm.vaadin.console.etl.data.service;

import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.repository.EtlRepository;
import org.hkrdi.eden.ggm.vaadin.console.etl.v1.NodeContentCollection;
import org.hkrdi.eden.ggm.vaadin.console.etl.v1.ObjectFactory;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@UIScope
public class ETLv1Service implements EtlService{
	
	private static Map<Integer, Integer> mapping =

			Arrays.asList(
			new Integer[][]
			{
				{0 ,65},
				{1 ,64},
				{2 ,57},
				{3 ,6},
				{4 ,13},
				{5 ,66},
				{6 ,69},
				{7 ,68},
				{8 ,67},
				{9 ,72},
				{10 ,71},
				{11 ,70},
				{12 ,56},
				{13 ,55},
				{14 ,54},
				{15 ,47},
				{16 ,5},
				{17 ,60},
				{18 ,59},
				{19 ,58},
				{20 ,63},
				{21 ,62},
				{22 ,61},
				{23 ,46},
				{24 ,45},
				{25 ,44},
				{26 ,37},
				{27 ,4},
				{28 ,50},
				{29 ,49},
				{30 ,48},
				{31 ,53},
				{32 ,52},
				{33 ,51},
				{34 ,36},
				{35 ,35},
				{36 ,34},
				{37 ,27},
				{38 ,3},
				{39 ,38},
				{40 ,43},
				{41 ,42},
				{42 ,41},
				{43 ,40},
				{44 ,39},
				{45 ,26},
				{46 ,25},
				{47 ,24},
				{48 ,17},
				{49 ,2},
				{50 ,28},
				{51 ,33},
				{52 ,32},
				{53 ,31},
				{54 ,30},
				{55 ,29},
				{56 ,14},
				{57 ,1},
				{58 ,16},
				{59 ,15},
				{60 ,19},
				{61 ,18},
				{62 ,23},
				{63 ,22},
				{64 ,21},
				{65 ,20},
				{66 ,8},
				{67 ,7},
				{68 ,12},
				{69 ,11},
				{70 ,10},
				{71 ,9},
					}
			).stream().collect(Collectors.toMap(v->v[0], v->v[1]));

	private NodeContentCollection unmarshall(InputStream is) throws JAXBException, FileNotFoundException {
		return (NodeContentCollection) JAXBContext.newInstance(NodeContentCollection.class, ObjectFactory.class).createUnmarshaller().unmarshal(
				is);
	}
	
	@Transactional
	public void persistEtl(EtlRepository etlRepository,  Etl etl, ApplicationRepositoryService applicationService) {
		etl.setApplication(applicationService.getApplicationById(etl.getApplication().getId()));
		etlRepository.save(etl);
	}
	
	public Integer mapping(Integer level, Integer originalNode) {
		return mapping.get(originalNode);
	}
	
	public String getNetworkName(int level) {
		return "SUSTAINABLE_VERTICES::1";
	}
	
	@Override
	@Transactional
	public void etl(EtlRepository etlRepository,
					ApplicationDataRepositoryService applicationDataService, ApplicationRepositoryService applicationService, Application application, InputStream is) throws FileNotFoundException, JAXBException{
		unmarshall(is).getNodeContents().getNodeContent().stream().forEach(c->{
			String log = null;
			Integer mappedToNode = null;
			if (mapping(2, c.getNumber())!=null) {
				try {
				persistSemantics(applicationDataService, new Etl(application, 2, mappedToNode = mapping(2, c.getNumber()), 
						c.getTitle(), c.getTitle(), 
						c.getTitle().equals("Empty Title")?c.getContent():c.getTitle()+"\n"+c.getContent()));
				}catch(Exception e) {
					log = "Failed: "+e.getMessage();
				}
			}else {
				log = "Skip: node not mapped";
			}
			
			persistEtl(etlRepository, new Etl(application, 2, mappedToNode, 
					c.getTitle(), c.getTitle(), c.getContent(), log, c.getNumber(), mappedToNode != null), applicationService);
		});
		
	}

}
