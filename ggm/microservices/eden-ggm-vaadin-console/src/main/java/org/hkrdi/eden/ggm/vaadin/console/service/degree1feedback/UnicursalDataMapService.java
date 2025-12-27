package org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback;

import org.hkrdi.eden.ggm.repository.degree1feedback.UnicursalDataMapRepository;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UnicursalDataMapService implements CrudRepositoryService<UnicursalDataMap, Long> {

	public static String[][] unicursalCombinations = new String[][]{
		{ "a,b,c,d,e,f", "a,b,c,f,e,d", "a,b,c,e,f,d", "a,b,c,f,d,e", "a,b,c,d,f,e", "a,b,c,e,d,f"},
		{ "b,c,a,e,f,d", "b,c,a,d,f,e", "b,c,a,f,d,e", "b,c,a,d,e,f", "b,c,a,e,d,f", "b,c,a,f,e,d"},
		{ "c,a,b,f,d,e", "c,a,b,e,d,f", "c,a,b,d,e,f", "c,a,b,e,f,d", "c,a,b,f,e,d", "c,a,b,d,f,e"},
		{ "b,a,c,e,d,f", "b,a,c,f,d,e", "b,a,c,d,f,e", "b,a,c,f,e,d", "b,a,c,e,d,f", "b,a,c,d,e,f"},
		{ "c,b,a,f,e,d", "c,b,a,d,e,f", "c,b,a,e,d,f", "c,b,a,d,f,e", "c,b,a,f,d,e", "c,b,a,e,f,d"},
		{ "a,c,b,d,f,e", "a,c,b,e,f,d", "a,c,b,f,e,d", "a,c,b,e,d,f", "a,c,b,d,e,f", "a,c,b,f,d,e"}
	};
	
	public static String[][] unicursalDescription = new String[][] {
		{"msg.unicursal.description.dimensiune.orizontidealperspectiva", null, "msg.unicursal.description.diagrama.echilibru", null}, 
		{"msg.unicursal.description.dimensiune.echilibru", null, "msg.unicursal.description.diagrama.masurabilitate", null}, 
		{"msg.unicursal.description.dimensiune.intrerupereaviziunii", null, "msg.unicursal.description.diagrama.modificareatraseului", null}, 
		{"msg.unicursal.description.dimensiune.modificareaviziunii", null, "msg.unicursal.description.diagrama.intrerupereatraseului", null}, 
		{"msg.unicursal.description.dimensiune.delimitarea", null, "msg.unicursal.description.diagrama.proiectareadetaliata", null}, 
		{"msg.unicursal.description.dimensiune.proiectarea", null, "msg.unicursal.description.diagrama.delimitareadetaliata", null}, 
	};
	
	public static String[][] semicicleDescription = new String[][] {
		{"msg.semicicle.description.purple.evolutiei", "msg.semicicle.description.green.invatarii"},
		{"msg.semicicle.description.purple.adaptarii", "msg.semicicle.description.green.apartenentei"},
		{"msg.semicicle.description.purple.specializarii", "msg.semicicle.description.green.mostenirii"},
		{"msg.semicicle.description.purple.oportunitatii", "msg.semicicle.description.green.legilorlocale"},
		{"msg.semicicle.description.purple.aprofundarii", "msg.semicicle.description.green.validariipractice"},
		{"msg.semicicle.description.purple.recunoasteriipatternuri", "msg.semicicle.description.green.portofoliilordesolutii"}
	};
	
	@Autowired
	private UnicursalDataMapRepository repository;

	public UnicursalDataMap findByApplicationIdAndRowAndColumn(Long applicationId, int row, int column) {
		return repository.findOneByApplicationIdAndRowAndColumn(applicationId, row, column);
	}

	public List<UnicursalDataMap> findAllByApplicationId(Long applicationId){
		return repository.findAllByApplicationId(applicationId);
	}
	
	public void generateAndSave(Long applicationId){
		List<UnicursalDataMap> list = generate(applicationId);
		list.stream().forEach(u->save(u));
	}
	
	public List<UnicursalDataMap> generate(Long applicationId){
		List<UnicursalDataMap> result = new ArrayList<UnicursalDataMap>();
		for(int row=0; row<6; row++) {
			for(int column=0; column<6; column++) {
				UnicursalDataMap data = new UnicursalDataMap();
				data.setApplicationId(applicationId);
				data.setColumn(column);
				data.setRow(row);
				data.setSemantic("semantic for "+row+"/"+column);
				data.setDefinition(getUnicursalDefaultDefinition(row, column));
				data.setDimenssion(getDimensionDefinition(column));
				data.setDimenssionUrl(getDimensionUrl(column));
				data.setUnicursal(getUnicursalDefinition(column));
				data.setUnicursalUrl(getUnicursalUrl(column));
				data.setUnicursalPurple(getUnicursalPurpleDefinition(column));
				data.setUnicursalGreen(getUnicursalGreenDefinition(column));
				
				result.add(data);
			}
		}
		return result;
	}
	
	public static String getUnicursalDefaultDefinition(int row, int column) {
		return unicursalCombinations[row][column];
	}
	
	public static String getDimensionDefinition(int column) {
		return unicursalDescription[column][0];
	}
	
	public static String getDimensionUrl(int column) {
		return unicursalDescription[column][1];
	}
	
	public static String getUnicursalDefinition(int column) {
		return unicursalDescription[column][2];
	}
	
	public static String getUnicursalUrl(int column) {
		return unicursalDescription[column][3];
	}

	@Override
	public CrudRepository<UnicursalDataMap, Long> getRepository() {
		return repository;
	}

	public static String getUnicursalPurpleDefinition(int column) {
		return semicicleDescription[column][0];
	}
	
	public static String getUnicursalGreenDefinition(int column) {
		return semicicleDescription[column][1];
	}
}
