package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.event.EdgeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class ReadEdgeSelectionPresenter extends LeftSideContentPresenter {

    @Autowired
    private GgmViewIe viewApplicationDataIe;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private ReadNodePresenter readNodePresenter;

    @Autowired
    private EventBus.UIEventBus uiEventBus;

    @PostConstruct
    private void initEventBus() {
        uiEventBus.subscribe(this);
    }

    @PreDestroy
    private void predestroyEventBus() {
        uiEventBus.unsubscribe(this);
    }

    @EventBusListenerMethod
    public void onEdgeSelectionChangeEvent(EdgeSelectionChangeEvent edgeSelectionChangeEvent) {
        Optional<EdgeBean> edge = (Optional<EdgeBean>)edgeSelectionChangeEvent.getSource();
        List<Application> application =  applicationService
                .getAllApplicationsByBriefLabelStartsWithIgnoreCase(viewApplicationDataIe.getApplicationName());
        getView().setVisible(edge.isPresent());
        if(edge.isPresent() && application.size() > 0) {
            getView().getReadEdgeView().getPresenter().setApplicationDataId(null);
            getView().getReadNodeView().getPresenter().setApplicationDataId(null);
            getView().getNodesWrapper().add(getView().getReadNodeView());
            Optional<DataMap> fromNodeDataMap = coherentSpaceService.findEdgeDataMap(edge.get());
            if (fromNodeDataMap.isPresent()) {
                ApplicationData applicationData =  applicationDataService
                        .getApplicationData(application.get(0).getId(), fromNodeDataMap.get());
                getView().getReadEdgeView()
                        .getPresenter().setApplicationDataId(applicationData.getId());
                getView().getPhraseIdFromNode().setText(applicationData.getSemantic());
                getView().getPhraseIdRoute().setText(applicationData.getSyntax());
            }
            Optional<DataMap> toNodeDataMap = coherentSpaceService.findNodeDataMap(edge.get().getToNode());
            if (toNodeDataMap.isPresent()) {
                ApplicationData applicationData =  applicationDataService
                        .getApplicationData(application.get(0).getId(), toNodeDataMap.get());
                getView().getReadNodeView().getPresenter().setApplicationDataId(applicationData.getId());
                getView().getPhraseIdToNode().setText(applicationData.getSemantic());
            }
            getView().getReadEdgeView().getPresenter().prepareModelAndView(edgeSelectionChangeEvent);
            getView().getReadNodeView().getPresenter().prepareModelAndView(edgeSelectionChangeEvent);
        }
    }

    @Override
    public ReadEdgeSelectionView getView() {
        return (ReadEdgeSelectionView) super.getView();
    }
}
