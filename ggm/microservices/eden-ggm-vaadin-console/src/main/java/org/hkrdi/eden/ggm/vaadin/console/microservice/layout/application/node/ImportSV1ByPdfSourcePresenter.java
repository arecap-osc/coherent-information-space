package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.hkrdi.eden.ggm.repository.common.documentimage.DocumentImageJpaService;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ImportSV1ByPdfSourcePresenter extends DefaultFlowPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSV1ByPdfSourcePresenter.class);

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Autowired
    private DocumentImageJpaService attachService;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Override
    public ImportSV1ByPdfSourceView getView() {
        return (ImportSV1ByPdfSourceView)super.getView();
    }

    public void fileUploadSucceeded(SucceededEvent succeededEvent) {
        LOGGER.info("Started uploading document " + succeededEvent.getFileName());
        List<Application> application =  applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase("Arta de a gandi");
        if (application.size() <= 0) {
            LOGGER.info("Document upload failed. There is no application selected");
            return;
        }
        Long nodeIndex = null;
        try {
            String node = succeededEvent.getFileName().substring(0, succeededEvent.getFileName().indexOf("_"));
            nodeIndex = new Long(node);
        }catch(RuntimeException e) {
            LOGGER.info("Document upload failed due to wrong file format. Document does not respect the convention regarding the name");
            Notification.show("wrong file format. should start with 23_"+succeededEvent.getFileName(), 5000, Notification.Position.MIDDLE);
            return;
        }
        Optional<DataMap> dataMapOpt = coherentSpaceService.findNodeDataMap(new NodeBean("SUSTAINABLE_VERTICES::1",nodeIndex));

        if (!dataMapOpt.isPresent()) {
            LOGGER.info("Document upload failed due to wrong indicated node");
            Notification.show("node "+ nodeIndex+" not found for file"+succeededEvent.getFileName(), 5000, Notification.Position.MIDDLE);
            return;
        }

        ApplicationData appData = applicationDataService.getApplicationData(application.get(0).getId(), dataMapOpt.get());

        appData.setSemantic(succeededEvent.getFileName().substring(succeededEvent.getFileName().indexOf(" "), succeededEvent.getFileName().lastIndexOf(".") ).trim());
        applicationDataService.save(appData);

        try {
            //checks if the file exists, if yes then replace it.
            DocumentImage fileExists = attachService
                    .findAllByApplicationIdAndParentIdAndParentDocAndSource(application.get(0).getId(), appData.getId(), ApplicationData.class.getSimpleName(), DocumentImage.Source.ATTACHED).
                    stream().filter(di->di.getName().equals(succeededEvent.getFileName())).findFirst().orElse(null);
            if (fileExists != null) {
                attachService.delete(application.get(0).getId(), fileExists.getId());
            }
            attachService.save(new DocumentImage(
                    application.get(0).getId(),
                    succeededEvent.getFileName(),
                    LocalDateTime.now(),
                    IOUtils.toByteArray(getView().getBuffer().getInputStream(succeededEvent.getFileName())),
                    application.get(0).getId(),//???
                    appData.getId(),
                    ApplicationData.class.getSimpleName(),
                    DocumentImage.Source.ATTACHED
            ));
        } catch (IOException e) {
            LOGGER.info("Document upload failed with " + e.getMessage());
            Notification.show("import failed for node "+ nodeIndex+" for file"+succeededEvent.getFileName()+" with " + e.getMessage(), 10000, Notification.Position.MIDDLE);
            return;
        }
    }

}
