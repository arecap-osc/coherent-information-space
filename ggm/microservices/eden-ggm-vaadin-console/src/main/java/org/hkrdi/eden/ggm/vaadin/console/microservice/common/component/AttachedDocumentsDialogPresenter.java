package org.hkrdi.eden.ggm.vaadin.console.microservice.common.component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.common.documentimage.DocumentImageJpaService;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.common.AttachedDocGridComponent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class AttachedDocumentsDialogPresenter implements FlowPresenter {
    private AttachedDocumentsDialogView view;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private DocumentImageJpaService documentService;

    @Override
    public void setView(FlowView view) {
        this.view = (AttachedDocumentsDialogView) view;
    }

    @Override
    public AttachedDocumentsDialogView getView() {
        return view;
    }

    public void setupAttachmentLayout(ApplicationData applicationData) {
        getView().getAttachmentsLayout().removeAll();
        AttachedDocGridComponent attachedDocGridComponent = new AttachedDocGridComponent(applicationData.getApplication().getId(),
                getApplicationDataAsNodeWellsIds(applicationData), ApplicationData.class, new HorizontalLayout());
        attachedDocGridComponent.setActive(false);

        getView().getLabel().setText(applicationData.getSemantic());
        getView().getAttachmentsLayout().add(attachedDocGridComponent);
    }

    private List<ApplicationData> getApplicationDataAsNodeWells(ApplicationData applicationData) {
        Optional<DataMap> dataMapOpt = coherentSpaceService
                .findNodeDataMap(new NodeBean(applicationData.getNetwork(), applicationData.getAddressIndex()));
        if (!dataMapOpt.isPresent()) {
            return Arrays.asList(new ApplicationData[]{applicationData});
        }
        NodeBean node = new NodeBean(dataMapOpt.get().getNetwork(), dataMapOpt.get().getAddressIndex());
        List<Long> dataMapIds = coherentSpaceService.findNodeWells(node).stream().map(DataMap::getId).collect(Collectors.toList());
        List<ApplicationData> applicationIds = applicationDataService.findAllByApplicationIdAndDataMapIdIn(applicationData.getApplication().getId(),
                dataMapIds);
        if (applicationIds.isEmpty()) {
            return Arrays.asList(new ApplicationData[]{applicationData});
        }
        return applicationIds;
    }

    public List<Long> getApplicationDataAsNodeWellsIds(ApplicationData applicationData) {
        return getApplicationDataAsNodeWells(applicationData).stream()
                .map(ApplicationData::getId)
                .collect(Collectors.toList());
    }
}
