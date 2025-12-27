package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import java.util.EventObject;
import java.util.List;
import java.util.concurrent.Executors;

import org.hkrdi.eden.ggm.repository.entity.EmbeddedBrief;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.service.ETLRCService;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ImportApplicationDialogPresenter extends DefaultFlowPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportApplicationDialogPresenter.class);

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Autowired
    private ETLRCService etlService;

    @Autowired
    private SemanticMapRepositoryService semanticMapService;

    @Override
    public ImportApplicationDialogView getView() {
        return (ImportApplicationDialogView) super.getView();
    }

    private ApplicationPresenter applicationPresenter;

    @Override
    public void prepareModel(EventObject event) {
        List<SemanticMap> semanticMaps = semanticMapService.findAll();
        getView().getSemanticMapComboBox().setItems(semanticMaps);
        if (semanticMaps.size() > 0) {
            getView().getSemanticMapComboBox().setValue(semanticMaps.get(0));
        }
    }

    public void fileUploadSucceeded(SucceededEvent succeededEvent) {
    	UI ui = UI.getCurrent();
    	SecurityContext sc = SecurityContextHolder.getContext();
    	Thread thread = new Thread(()-> {
		    	try {
					SecurityContextHolder.clearContext();
					SecurityContextHolder.getContext().setAuthentication(sc.getAuthentication());
					
					fileUploadSucceededInternal(succeededEvent, ui);
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
    
    private Application createApplication(String name) {
    	Application application = new Application();
        application.setBrief(new EmbeddedBrief());
        application.getBrief().setLabel(name);
        application.getBrief().setBrief(application.getBrief().getLabel());
        application.getBrief().setDescription(application.getBrief().getLabel());
        return application;
    }
    
    private void markApplicationAsFailed(Application application) {
    	application.getBrief().setLabel("FAILED-" + application.getBrief().getLabel());
        application.getBrief().setBrief("FAILED-" + application.getBrief().getBrief());
        application.getBrief().setDescription("FAILED-" + application.getBrief().getDescription());
    }
    
    private void markApplicationAsUnfailed(Application application) {
    	application.getBrief().setLabel(application.getBrief().getLabel().replace("FAILED-", ""));
        application.getBrief().setBrief(application.getBrief().getBrief().replace("FAILED-", ""));
        application.getBrief().setDescription(application.getBrief().getDescription().replace("FAILED-", ""));
    }
    
    public void fileUploadSucceededInternal(SucceededEvent succeededEvent, UI ui) {
        String fileName = succeededEvent.getFileName();
        LOGGER.info("User uploaded file: " + fileName);
        Application application = createApplication(fileName.replace(".xml", "").replace("_", " ").replace(".", " "));
        ui.access(()->{
        	getView().close();
        });

        if (getView().getSemanticMapComboBox().getValue() != null) {
            application.setSemanticGridId(getView().getSemanticMapComboBox().getValue().getId());
        }
        if (applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase(application.getBrief().getLabel()).size() > 0) {
            LOGGER.error("The uploaded application already exists");
            notify(ui, "Exista deja o aplicatie cu acest nume!!! Perluare esuata!");
            return;
        }
        
        markApplicationAsFailed(application);
        application = applicationService.save(application);
        notify(ui, "Aplicatia " + application.getBrief().getLabel().replace("FAILED-", "") + " a fost incarcata si va fi prelucrata. O notificare va fi afisata la finalizarea operatiei!");
        try {
            Integer etlVersion = etlService.etl(application, getView().getBuffer().getInputStream(fileName), fileName);
            application.setEtlVersion(etlVersion);
            markApplicationAsUnfailed(application);
            applicationService.save(application);

            LOGGER.info("Successfully imported application " + application.getBrief().getLabel());
            notify(ui, "Aplicatia " + application.getBrief().getLabel() + " a fost preluata cu succes!");

        } catch (Throwable e) {
        	ui.access(()->{
        		getView().getUpload().interruptUpload();
        	});
            LOGGER.error("Failed to import application " + application.getBrief().getLabel().replace("FAILED-", ""));
            LOGGER.error(e.getMessage(), e);
            markApplicationAsUnfailed(application);
            notify(ui, "!!!Preluarea aplicatiei " + application.getBrief().getLabel() + " a esuat cu mesajul:\n" + e.getMessage());
        }
        ui.access(()->{
        	applicationPresenter.applicationComboBoxSetItemsAndSelectValue();
        });
    }

    public void setApplicationPresenter(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }
}
