package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application.node;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.event.NodeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.EventObject;
import java.util.Optional;

@SpringComponent
@UIScope
public class NodeSelectionPresenter extends LeftSideContentPresenter {

    private Optional<Application> application = Optional.empty();

    private Optional<NodeBean> fistNode = Optional.empty();

    private Optional<NodeBean> secondNode = Optional.empty();

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Override
    public NodeSelectionView getView() {
        return (NodeSelectionView) super.getView();
    }

    @Override
    public void prepareModel(EventObject event) {
        application = Optional.empty();
        if(applicationDataIe.getApplicationId() != null) {
            application = applicationService.findById(applicationDataIe.getApplicationId());
        }
        setupApplicationDataViews();
    }

    @EventBusListenerMethod
    public void onGgmApplicationChangeEvent(GgmApplicationChangeEvent ggmApplicationChangeEvent) {
        application = (Optional<Application>) ggmApplicationChangeEvent.getSource();
        setupApplicationDataViews();
    }

    @EventBusListenerMethod
    public void onNodeSelectionChangeEvent(NodeSelectionChangeEvent nodeSelectionChangeEvent) {
        Optional<NodeBean> node = (Optional<NodeBean>)nodeSelectionChangeEvent.getSource();
        getView().setVisible(node.isPresent());
        if(node.isPresent()) {
            if(fistNode.isPresent() ) {
                if(secondNode.isPresent() == false
                        && coherentSpaceService.findNodeNeighbors(fistNode.get()).contains(node.get())) {
                    secondNode = node;
                    setupApplicationDataViews();
                    return;
                }
                if(fistNode.get().equals(node.get()) && secondNode.isPresent() == false) {
                    node = Optional.empty();
                }
            }
        }
        fistNode = node;
        secondNode = Optional.empty();
        setupApplicationDataViews();
    }


    private void setupApplicationDataViews() {
        applicationDataIe.setFirstNodeId(null);
        applicationDataIe.setSecondNodeId(null);
        applicationDataIe.setSyntaxId(null);
        semanticMapIe.setSemanticMapId(null);
        if(application.isPresent()) {
            if (fistNode.isPresent()) {
                Optional<DataMap> firstNodeDataMap = coherentSpaceService.findNodeDataMap(fistNode.get());
                if (firstNodeDataMap.isPresent()) {
                    applicationDataIe.setFirstNodeId(applicationDataService.getApplicationData(application.get().getId(),
                            firstNodeDataMap.get()).getId());
                }
            }
            if (secondNode.isPresent()) {
                Optional<DataMap> secondNodeDataMap = coherentSpaceService.findNodeDataMap(secondNode.get());
                if (secondNodeDataMap.isPresent()) {
                    applicationDataIe.setSecondNodeId(applicationDataService.getApplicationData(application.get().getId(),
                            secondNodeDataMap.get()).getId());
                    DataMap edgeDataMap = coherentSpaceService.findEdgeDataMap(new EdgeBean(fistNode.get(), secondNode.get()))
                            .orElseGet(() -> coherentSpaceService.findEdgeDataMap(new EdgeBean(secondNode.get(), fistNode.get())).get());
                    applicationDataIe.setSyntaxId(applicationDataService.getApplicationData(application.get().getId(), edgeDataMap).getId());
                }

            }
            semanticMapIe.setSemanticMapId(application.get().getSemanticGridId());
        }
        getView().getFirstNodeView().getPresenter().prepareModelAndView(null);
        getView().getSecondNodeView().getPresenter().prepareModelAndView(null);
        getView().getEdgeView().getPresenter().prepareModelAndView(null);
    }

}
