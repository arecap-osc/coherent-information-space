package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository.FeedbackDataMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FeedbackDataMapService {

	public static String a[] = new String[] {"ce", "cum", "de ce"};
	public static String b[] = new String[] {"ce", "cum", "de ce"};
	public static String d[] = new String[] {"ce", "cum", "de ce"};
	public static String e[] = new String[] {"ce", "cum", "de ce"};
	public static String c[] = new String[] {"unde/cand"};
	public static String f[] = new String[] {"unde/cand"};

	@Autowired
	private FeedbackDataMapRepository repository;
	
	public List<FeedbackDataMap> findAll(Long applicationId) {
		return repository.findAllByApplicationId(applicationId);
	}
	
	public List<FeedbackDataMap> findAll(Long applicationId, int row, int column) {
		return repository.findAllByApplicationIdAndRowAndColumn(applicationId, row, column);
	}
	
	public FeedbackDataMap save(FeedbackDataMap entity) {
		return repository.save(entity);
	}
	
	public void generateAllCombinationsAndSave(Long applicationId, int row, int column){
		List<FeedbackDataMap> list = generateAllCombinations(applicationId, row, column);
		list.stream().forEach(d->save(d));
	}
	
	public List<FeedbackDataMap> generateAllCombinations(Long applicationId, int row, int column){
		String[] letterMapping = UnicursalDataMapService.getUnicursalDefaultDefinition(row, column).split(",");
		List<FeedbackDataMap> result = new ArrayList<FeedbackDataMap>();
		long counter = 0;
		for (int ai=0; ai< a.length; ai++) {
			for (int bi=0; bi< b.length; bi++) {
				for (int ci=0; ci< c.length; ci++) {
					for (int di=0; di< d.length; di++) {
						for (int ei=0; ei< e.length; ei++) {
							for (int fi=0; fi< f.length; fi++) {
								FeedbackDataMap data = new FeedbackDataMap();
								data.setFeedbackPosition(counter++);
								data.setApplicationId(applicationId);
								data.setColumn(column);
								data.setRow(row);
								data.setIesireDate(getValueForLetterMapping(0, letterMapping, ai, bi, ci, di, ei, fi));
								data.setProcesareDate(getValueForLetterMapping(1, letterMapping, ai, bi, ci, di, ei, fi));
								data.setBazeStrategii(getValueForLetterMapping(2, letterMapping, ai, bi, ci, di, ei, fi));
								data.setIntrareDate(getValueForLetterMapping(3, letterMapping, ai, bi, ci, di, ei, fi));
								data.setEvaluareRaspunsuri(getValueForLetterMapping(4, letterMapping, ai, bi, ci, di, ei, fi));
								data.setBazeExperiente(getValueForLetterMapping(5, letterMapping, ai, bi, ci, di, ei, fi));
								data.setUnicursal(UnicursalDataMapService.getUnicursalDefinition(column));
								data.setDimenssion(UnicursalDataMapService.getDimensionDefinition(column));
								result.add(data);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	private String getValueForLetterMapping(int position, String[] letterMapping, int ai, int bi, int ci, int di, int ei, int fi) {
		String letter = letterMapping[position];
		switch(letter) {
			case "a": return a[ai];
			case "b": return b[bi];
			case "c": return c[ci];
			case "d": return d[di];
			case "e": return e[ei];
			case "f": return f[fi];
		}
		throw new IllegalArgumentException();
	}
}
