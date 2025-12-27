package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.export;

import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.console.common.util.XWPFDocumentUtil;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@UIScope
public class FeedbackExportService implements XWPFDocumentUtil{

	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Autowired
	private FeedbackDataMapService feedbackDataMapService;
	
	@Transactional
	public byte[] generate(FeedbackApplication application, InputStream template) throws Exception{
		
    	XWPFDocument doc = new XWPFDocument(OPCPackage.open(template));
    	
    	Map<String, String> mappingsParagraph = new HashMap<String, String>();
    	mappingsParagraph.putAll(fillByEntity(feedbackSelection.getUnicursalDataMap()));
    	mappingsParagraph.put("UnicursalDataMap.column", (feedbackSelection.getUnicursalDataMap().getColumn()+1)+"");
    	mappingsParagraph.put("UnicursalDataMaprow", (feedbackSelection.getUnicursalDataMap().getRow()+1)+"");
    	mappingsParagraph.put("$Mov$", "Mov");
    	mappingsParagraph.put("$Verde$", "Verde");
    	
		XWPFParagraph feedbackNoParagrah = findParagraph(doc, "$FeedbackDataMap.feedbackPosition$");
		XWPFTable imageTable = doc.getTables().get(0);
    	XWPFTable completeCycleTable = doc.getTables().get(1);
    	XWPFParagraph inOutParagrah = findParagraph(doc, "$Mov$");
    	XWPFTable inOutCycleTable = doc.getTables().get(2);
    	XWPFParagraph outInParagrah = findParagraph(doc, "$Verde$");
    	XWPFTable outInCycleTable = doc.getTables().get(3);

    	//fill unicursal data
    	replaceParagrahVariables(doc, mappingsParagraph);
    	
    	feedbackDataMapService.findAllByApplicationIdAndRowAndColumn(feedbackSelection.getApplication().getId(),
    			feedbackSelection.getRow(), feedbackSelection.getColumn()).stream().forEach(feedbackDataMap->{
    				mappingsParagraph.putAll(fillByEntity(feedbackDataMap));   
    				copyParagraph(feedbackNoParagrah, doc.createParagraph(), mappingsParagraph);
    				try {
						fillFeedbackImages(doc, mappingsParagraph);
					} catch (InvalidFormatException | IOException e) {
						e.printStackTrace();
					}
    				
    				doc.createParagraph();
    				copyTable(completeCycleTable, doc.createTable(), mappingsParagraph);
    				doc.createParagraph();
    				doc.createParagraph();
    				copyParagraph(inOutParagrah, doc.createParagraph(), mappingsParagraph);
    				copyTable(inOutCycleTable, doc.createTable(), mappingsParagraph);
    				doc.createParagraph();
    				doc.createParagraph();
    				copyParagraph(outInParagrah, doc.createParagraph(), mappingsParagraph);
    				copyTable(outInCycleTable, doc.createTable(), mappingsParagraph);
    				doc.createParagraph().setPageBreak(true);
		});
    	
    	clearTemplatingParagraps(doc, feedbackNoParagrah, inOutParagrah, outInParagrah/*, feedbackImageParagrah*/);
    	clearTemplatingTables(doc, imageTable, completeCycleTable, inOutCycleTable, outInCycleTable);

    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*try (FileOutputStream out = new FileOutputStream("d:\\simple.docx")) */{
            doc.write(baos);
        }
        doc.close();
        
        return baos.toByteArray();
    }
	
	private void fillFeedbackImages(XWPFDocument doc, Map<String, String> mappingsParagraph) throws InvalidFormatException, IOException {
		XWPFTable imageTable = doc.getTables().get(0);
		
		XWPFTable imageTableReplica = doc.createTable();
		copyTable(imageTable, imageTableReplica, mappingsParagraph);
		XWPFParagraph dimenssionImage = findInTable(imageTableReplica, "$dimenssionimage$");
		dimenssionImage.removeRun(0);
		dimenssionImage.createRun();
		
		addImage(getClass().getClassLoader().getResourceAsStream(
				"static/frontend/img/dim_0"+feedbackSelection.getUnicursalDataMap().getColumn()+".png"), 
				dimenssionImage, XWPFDocument.PICTURE_TYPE_PNG, "Picture", 185, 81);
		
		XWPFParagraph unicursalImage = findInTable(imageTableReplica, "$unicursalimage$");
		unicursalImage.removeRun(0);
		unicursalImage.createRun();

    	addImage(getClass().getClassLoader().getResourceAsStream(
    			"static/frontend/img/diag_0"+feedbackSelection.getUnicursalDataMap().getColumn()+"_1.png"), 
    			unicursalImage, XWPFDocument.PICTURE_TYPE_PNG, "Picture", 185, 81);
    	
	}
	
	private void clearTemplatingTables(XWPFDocument doc, XWPFTable... tables) {
		Arrays.asList(tables).stream().forEach(table->doc.removeBodyElement(doc.getPosOfTable(table)));
	}
	
	private void clearTemplatingParagraps(XWPFDocument doc, XWPFParagraph... paragraphs) {
		Arrays.asList(paragraphs).stream().forEach(paragraph->doc.removeBodyElement(doc.getPosOfParagraph(paragraph)));
	}

}
