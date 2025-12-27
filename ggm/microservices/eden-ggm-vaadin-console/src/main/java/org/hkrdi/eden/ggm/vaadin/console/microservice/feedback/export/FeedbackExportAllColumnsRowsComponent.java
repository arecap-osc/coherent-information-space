package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.export;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@UIScope
@SpringComponent
public class FeedbackExportAllColumnsRowsComponent extends FeedbackExportComponent{

	@Autowired
	private FeedbackExportService exportDataService;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Autowired
	private UnicursalDataMapService unicursalDataMapService;
	
	@PostConstruct
	@Override
	public void setup() {
		super.setup();
	}
	
	protected InputStream createExport() {
    	try {
    		File f = File.createTempFile("exportapp-"+feedbackSelection.getApplication().getLabel(), ".zip");
    		f.deleteOnExit();
    		FileOutputStream fos = new FileOutputStream(f); 
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		for (int column=0; column<6; column++) {
    			for (int row=0; row<6; row++) {
    				feedbackSelection.setColumn(column);
    				feedbackSelection.setRow(row);
    				feedbackSelection.setUnicursalDataMap(unicursalDataMapService.findByApplicationIdAndRowAndColumn(feedbackSelection.getApplication().getId(), row, column));
    				zos.putNextEntry(new ZipEntry(super.buildFileName()));
                    byte[] bytes = createExportAsByte();
                    zos.write(bytes, 0, bytes.length);
                    System.out.println("column="+column+" row="+row);
                    zos.closeEntry();
    			}
    		}
    		zos.close();
			return new FileInputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }

	protected String buildFileName() {
    	return (feedbackSelection.getApplication()==null)?"feedbacks.zip":feedbackSelection.getApplication().getLabel()+".zip";
    }
}
