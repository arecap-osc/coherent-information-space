package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.UnicursalDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.vaadin.flow.spring.annotation.SpringComponent;

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
		String coloanaS = semanticP.substring(0, semanticP.indexOf("RAND")).replace("COLOANA", "").replace("-", "").trim();
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
		
		List<FeedbackDataMap> feedbacks = feedbackService.findAll(application.getId(), row, column);
		Assert.isTrue(feedbacks.size()==81, "Baza de date incompleta"+feedbacks.size()+" pt appid=" + application.getId() +","+  row +","+ column);
		
		long feedbackPosition = 0;
		List<FeedbackDataMap> dms = new ArrayList<FeedbackDataMap>();
		for (int tablNo=0; tablNo<doc.getTables().size(); tablNo+=3) {
			XWPFTable completeCycleT = doc.getTables().get(tablNo);
			Assert.isTrue(completeCycleT.getRow(1).getTableCells().size() == 4, tablNo+":This is not a Feedback complete table");
			Assert.isTrue(completeCycleT.getRow(1).getTableCells().get(3).getText().trim().toLowerCase().contains("Feedback complet".toLowerCase()), tablNo+":This is not a Feedback complete table");
			
			XWPFTable inOutCycle = doc.getTables().get(tablNo+1);
			XWPFTable outInCycle = doc.getTables().get(tablNo+2);
			final Long feedbackPositionF = feedbackPosition;
			FeedbackDataMap dm = feedbacks.stream().filter(f->f.getFeedbackPosition().equals(feedbackPositionF)).findFirst().get();
//			dm.setApplicationId(application.getId());
//			dm.setRow(row);
//			dm.setColumn(column);
			dm.setFeedbackPosition(feedbackPosition++);
			dm.setIesireDate(completeCycleT.getRow(1).getTableCells().get(0).getText());
			dm.setProcesareDate(completeCycleT.getRow(1).getTableCells().get(1).getText());
			dm.setBazeStrategii(completeCycleT.getRow(1).getTableCells().get(2).getText());
			dm.setCompleteSemantic(completeCycleT.getRow(2).getTableCells().get(3).getText());
			dm.setIesireDate(completeCycleT.getRow(3).getTableCells().get(0).getText());
			dm.setEvaluareRaspunsuri(completeCycleT.getRow(3).getTableCells().get(1).getText());
			dm.setBazeExperiente(completeCycleT.getRow(3).getTableCells().get(2).getText());
			
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
