package org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback;

import org.hkrdi.eden.ggm.repository.degree1feedback.FeedbackDataMapRepository;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackDataMapService  implements CrudRepositoryService<FeedbackDataMap, Long> {

	public static String a[] = new String[] {"msg.feedback.datamap.array.what", "msg.feedback.datamap.array.how", "msg.feedback.datamap.array.why"};
	public static String b[] = new String[] {"msg.feedback.datamap.array.what", "msg.feedback.datamap.array.how", "msg.feedback.datamap.array.why"};
	public static String d[] = new String[] {"msg.feedback.datamap.array.what", "msg.feedback.datamap.array.how", "msg.feedback.datamap.array.why"};
	public static String e[] = new String[] {"msg.feedback.datamap.array.what", "msg.feedback.datamap.array.how", "msg.feedback.datamap.array.why"};
	public static String c[] = new String[] {"msg.feedback.datamap.array.whenwhere"};
	public static String f[] = new String[] {"msg.feedback.datamap.array.whenwhere"};
	
	public static String a_label = "msg.feedback.datamap.whathowwhy.label";
	public static String b_label = "msg.feedback.datamap.whathowwhy.label";
	public static String d_label = "msg.feedback.datamap.whathowwhy.label";
	public static String e_label = "msg.feedback.datamap.whathowwhy.label";
	public static String c_label = "msg.feedback.datamap.wherewhen.label";
	public static String f_label = "msg.feedback.datamap.wherewhen.label";

	@Autowired
	private FeedbackDataMapRepository repository;
	
	public List<FeedbackDataMap> findAllByApplicationId(Long applicationId) {
		return repository.findAllByApplicationId(applicationId);
	}
	
	public List<FeedbackDataMap> findAllByApplicationIdAndRowAndColumn(Long applicationId, int row, int column) {
		return repository.findAllByApplicationIdAndRowAndColumnOrderByFeedbackPosition(applicationId, row, column);
	}
	
	public Optional<FeedbackDataMap> findAllByApplicationIdAndRowAndColumnAndFeedbackPosition(Long applicationId, int row, int column, long feedbackPosition) {
		return repository.findByApplicationIdAndRowAndColumnAndFeedbackPosition(applicationId, row, column, feedbackPosition);
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
								data.setUnicursalPurple(UnicursalDataMapService.getUnicursalPurpleDefinition(column));
								data.setUnicursalGreen(UnicursalDataMapService.getUnicursalGreenDefinition(column));
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
	
//	public String getUnicursalDefaultDefinition(int row, int column) {
//		return UnicursalDataMapService.getUnicursalDefaultDefinition(row, column);
//	}
	
	public String getUnicursalDefaultDefinitionLabel(int row, int column) {
		String[] letters = UnicursalDataMapService.getUnicursalDefaultDefinition(row, column).split(",");
		String result = "";
		for (int i=0; i<letters.length; i++) {
			String rep = "";
			switch(letters[i]) {
				case "a": rep = a_label; break;
				case "b": rep = b_label; break;
				case "c": rep = c_label; break;
				case "d": rep = d_label; break;
				case "e": rep = e_label; break;
				case "f": rep = f_label; break;
			}
			result += rep + (i+1 != letters.length ? "~" : "");
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

	@Override
	public CrudRepository<FeedbackDataMap, Long> getRepository() {
		return repository;
	}
}
