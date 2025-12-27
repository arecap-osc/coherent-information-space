package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.backend.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SpringComponent
public class FeedbackImportService {
	@Autowired
	private FeedbackDataMapService feedbackService;
	
	@Autowired
	private UnicursalDataMapService unicursalService;

	@Transactional
	public List<FeedbackDataMap> importFile(FeedbackApplication application, InputStream template) throws Exception{
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(template));
		String semanticP = getParagraphText(doc.getParagraphs().get(0));
//		COLOANA 1 - RAND 6 
		semanticP = semanticP.replace("Coloana", "COLOANA").replace("Rand", "RAND");
		
		String coloanaS = semanticP.substring(semanticP.indexOf("COLOANA")+"COLOANA".length(), semanticP.indexOf("RAND")).replace("COLOANA", "").
				replace("-", "").replace("–", "").trim();
		int pos;
		String randS = semanticP.substring(pos = semanticP.indexOf("RAND")+"RAND".length(), pos+2).trim();
		String semantica = semanticP.substring(pos = semanticP.indexOf("RAND")+"RAND".length()+2).trim();
		int column = new Integer(coloanaS)-1;
		Assert.isTrue(column>=0 && column<6,"Column no wrong "+column);
		int row = new Integer (randS)-1;
		Assert.isTrue(row>=0 && row<6,"row no wrong "+row);
				
		UnicursalDataMap unicursalDataMap = unicursalService.findByApplicationIdAndRowAndColumn(application.getId(), row, column);;
		unicursalDataMap.setSemantic(semantica);
		unicursalService.save(unicursalDataMap);
		
		List<FeedbackDataMap> feedbacks = feedbackService.findAllByApplicationIdAndRowAndColumn(application.getId(), row, column);
		Assert.isTrue(feedbacks.size()==81, "Baza de date incompleta"+feedbacks.size()+" pt appid=" + application.getId() +","+  row +","+ column);
		
		long feedbackPosition = 0;
		int tableNoIncreaser = 3;
		int initialTableNoValue = 0;
		if (doc.getTables().size() == 324) {
			//our export case
			tableNoIncreaser = 4;
			initialTableNoValue = 1;	
		}
		List<FeedbackDataMap> dms = new ArrayList<FeedbackDataMap>();
		for (int tablNo=initialTableNoValue; tablNo<doc.getTables().size(); tablNo+=tableNoIncreaser) {
			XWPFTable completeCycleT = doc.getTables().get(tablNo);
			Assert.isTrue(completeCycleT.getRow(1).getTableCells().size() == 4, tablNo+":This is not a Feedback complete table");
//			Assert.isTrue(completeCycleT.getRow(1).getTableCells().get(3).getText().trim().toLowerCase().contains("Feedback complet".toLowerCase()), tablNo+":This is not a Feedback complete table");
			
			XWPFTable inOutCycle = doc.getTables().get(tablNo+1);
			XWPFTable outInCycle = doc.getTables().get(tablNo+2);
			final Long feedbackPositionF = feedbackPosition;
			FeedbackDataMap dm = feedbacks.stream().filter(f->f.getFeedbackPosition().equals(feedbackPositionF)).findFirst().get();
//			dm.setApplicationId(application.getId());
//			dm.setRow(row);
//			dm.setColumn(column);
			dm.setFeedbackPosition(feedbackPosition++);
			dm.setIesireDate(completeCycleT.getRow(1).getTableCells().get(0).getText().toUpperCase().trim());
			dm.setProcesareDate(completeCycleT.getRow(1).getTableCells().get(1).getText().toUpperCase().trim());
			dm.setBazeStrategii(completeCycleT.getRow(1).getTableCells().get(2).getText().toUpperCase().trim());
			dm.setCompleteSemantic(completeCycleT.getRow(2).getTableCells().get(3).getText());
			dm.setIntrareDate(completeCycleT.getRow(3).getTableCells().get(0).getText().toUpperCase().trim());
			dm.setEvaluareRaspunsuri(completeCycleT.getRow(3).getTableCells().get(1).getText().toUpperCase().trim());
			dm.setBazeExperiente(completeCycleT.getRow(3).getTableCells().get(2).getText().toUpperCase().trim());
			
			dm.setInOutSemantic(inOutCycle.getRow(1).getTableCells().get(4).getText());
			dm.setOutInSemantic(outInCycle.getRow(1).getTableCells().get(4).getText());
			dms.add(dm);
			feedbackService.save(dm);
		}
		Assert.isTrue(dms.size()==81, "Document incomplete");
		return dms;
	}
	
	private String getParagraphText(XWPFParagraph p) {
		return p.getIRuns().stream().map(r->r.toString()).collect(Collectors.joining());
	}
	
}
