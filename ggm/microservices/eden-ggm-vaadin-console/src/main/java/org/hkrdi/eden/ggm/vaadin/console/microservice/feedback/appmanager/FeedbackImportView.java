package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.appmanager;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.common.UploadWithSecurityContext;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.backend.service.FeedbackImportService;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class FeedbackImportView extends HorizontalLayout {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackImportView.class);
			
    @Autowired
    private FeedbackSelectionIe feedbackSelection;
    
    @Autowired
    private FeedbackImportService feedbackImportService; 

    private Upload upload;
    
    private MultiFileMemoryBuffer buffer;
    
    @PostConstruct
	public void setup() {
    	setSpacing(false);
    	setPadding(false);
    	HorizontalLayout ho = new HorizontalLayout();
    	ho.setPadding(false);
    	ho.setWidthFull();
    	
    	buffer = new MultiFileMemoryBuffer();
    	upload = new UploadWithSecurityContext(SecurityContextHolder.getContext(), buffer);
    	upload.setDropAllowed(false);
    	Button uploadButton = new Button();
    	uploadButton.setIcon(VaadinIcon.UPLOAD.create());
    	uploadButton.getElement().setAttribute("title", "tooltip.importfeedback.button");

    	upload.setUploadButton(uploadButton);
    	upload.addSucceededListener(this::doImport);
    	upload.setMaxFiles(36);
    	ho.add(upload);
    	add(ho);
    }

    public void doImport(SucceededEvent succeededEvent) {
    	UI ui = UI.getCurrent();
    	SecurityContext sc = SecurityContextHolder.getContext();
    	Thread thread = new Thread(()-> {
		    	try {
					SecurityContextHolder.clearContext();
					SecurityContextHolder.getContext().setAuthentication(sc.getAuthentication());
					
					doImportInternal(succeededEvent, ui);
		    	}catch (Throwable e) {
		    		LOGGER.error("Import failed for "+succeededEvent.getFileName()+" with "+e.getMessage());
		    		e.printStackTrace();
				}finally {
					SecurityContextHolder.clearContext();
				}
		}, this.getClass().getSimpleName()+":"+succeededEvent.getFileName().replace(".xml", "").replace("_", " ").replace(".", " "));
    	thread.start();
    }
    
    private void notify(UI ui, String msg) {
    	ui.access(()->{
    		Notification.show(msg, 5000, Notification.Position.MIDDLE);
    	});
    }
    
    public void doImportInternal(SucceededEvent event, UI ui) {
    	if (feedbackSelection.getApplication() == null) {
			notify(ui, "Alegeti mai intai o aplicatie pentru a putea efectua importul!");
			return;
		}
		LOGGER.info("Feedback file upload started:"+event.getFileName());
		try {
			feedbackImportService.importFile(
					feedbackSelection.getApplication(), 
					buffer.getInputStream(
							event.getFileName()));
			LOGGER.info("Feedback file uploaded:"+event.getFileName());
			notify(ui, "Pereluare fisier "+event.getFileName() +" cu succes!");
		} catch (Exception e) {
			ui.access(()->{
				upload.interruptUpload();
			});
			notify(ui, "!!!Pereluare fisier "+event.getFileName() +" a esuat cu mesajul:\n"+e.getMessage());
			LOGGER.error("Feedback file upload failed:"+event.getFileName(), e);
		}
    }
}
