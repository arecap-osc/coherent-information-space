package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.export;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import org.hkrdi.eden.ggm.vaadin.console.common.DownloadWithSecurityContext;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@UIScope
@SpringComponent
@Primary
public class FeedbackExportComponent extends HorizontalLayout{

	@Autowired
	private FeedbackExportService exportDataService;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;

	private Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());

	public void setup() {
		removeAll();
		setPadding(false);
		downloadButton.getElement().setAttribute("title", "tooltip.exportfeedback.button");
		add(buildExport("exportfeedback"));
	}
	
	protected InputStream createExport() {
		try {
			byte [] result = createExportAsByte();
			return new ByteArrayInputStream(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	protected byte[] createExportAsByte() throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(org.apache.commons.io.IOUtils.toByteArray(getClass().getResourceAsStream("/feedback/feedback_export.docx"))); 
		byte [] result = exportDataService.generate(feedbackSelection.getApplication(), is);
		is.close();
		return result;
    }
    
	protected Component buildExport(String feedbackId) {
		DownloadWithSecurityContext download = new DownloadWithSecurityContext(downloadButton);
		download.configureDownload(buildFileName(), () -> createExport());
	    download.setId(feedbackId);
    	return download;
    }
    
	protected String buildFileName() {
    	return feedbackSelection.getApplication().getLabel()+"-"+
    			"COLOANA " + (feedbackSelection.getUnicursalDataMap().getColumn()+1) + " RAND " + (feedbackSelection.getUnicursalDataMap().getRow()+1) + " " +
    			feedbackSelection.getUnicursalDataMap().getSemantic().replace("/", "") +
    			".docx";
    }
}
