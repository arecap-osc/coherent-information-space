package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository.UnicursalDataMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UnicursalDataMapService implements IGgmService<UnicursalDataMap>{

	public static String[][] unicursalCombinations = new String[][]{
		{ "a,b,c,d,e,f", "a,b,c,f,e,d", "a,b,c,e,f,d", "a,b,c,f,d,e", "a,b,c,d,f,e", "a,b,c,e,d,f"},
		{ "b,c,a,e,f,d", "b,c,a,d,f,e", "b,c,a,d,e,f", "b,c,a,f,f,f", "b,c,a,f,f,f", "b,c,a,f,f,f"},
		{ "c,a,b,f,d,e", "c,a,b,e,d,f", "c,a,b,e,f,d", "c,a,b,f,f,f", "c,a,b,f,f,f", "c,a,b,f,f,f"},
		{ "b,a,c,e,d,f", "b,a,c,f,d,e", "b,a,c,d,f,e", "b,a,c,f,e,d", "b,a,c,e,f,d", "b,a,c,d,e,f"},
		{ "c,b,a,f,e,d", "c,b,a,d,e,f", "c,b,a,e,d,f", "c,b,a,d,f,e", "c,b,a,f,d,e", "c,b,a,e,f,d"},
		{ "a,c,b,d,f,e", "a,c,b,e,f,d", "a,c,b,f,e,d", "a,c,b,e,d,f", "a,c,b,d,e,f", "a,c,b,f,d,e"}
	};
	
	public static String[][] unicursalDescription = new String[][] {
		{"ORIZONT, IDEAL, PERSPECTIVA", null, "ECHILIBRU SUSTENABILITATE", null}, 
		{"ECHILIBRU SUSTENABILITATE", null, "MASURABILITATE, CONECTIVITATE", null}, 
		{"INTRERUPEREA VIZIUNII", null, "MODIFICAREA TRASEULUI", null}, 
		{"MODIFICAREA VIZIUNII", null, "INTRERUPEREA TRASEULUI", null}, 
		{"DELIMITAREA TERITORIULUI", null, "PROIECTAREA DETALIATA", null}, 
		{"PROIECTAREA TERITORIULUI", null, "DELIMITAREA DETALIATA", null}, 
	};
	
	@Autowired
	private UnicursalDataMapRepository repository;

	public UnicursalDataMap findByApplicationIdAndRowAndColumn(Long applicationId, int row, int column) {
		return repository.findOneByApplicationIdAndRowAndColumn(applicationId, row, column);
	}

	public List<UnicursalDataMap> findAll(Long applicationId){
		return repository.findAllByApplicationId(applicationId);
	}
	
	@Override
	public UnicursalDataMap save(UnicursalDataMap dataMap) {
		return repository.save(dataMap);
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
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public UnicursalDataMap load(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public UnicursalDataMap findOne(Long id) {
		return repository.findById(id).get();
	}

}
