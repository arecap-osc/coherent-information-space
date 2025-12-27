package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.event.NodeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class ReadNodeSelectionPresenter extends LeftSideContentPresenter  {

    @Autowired
    private GgmViewIe viewApplicationDataIe;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Override
    public ReadNodeSelectionView getView() {
        return (ReadNodeSelectionView) super.getView();
    }

    @EventBusListenerMethod
    public void onNodeSelectionChangeEvent(NodeSelectionChangeEvent nodeSelectionChangeEvent) {
        Optional<NodeBean> node = (Optional<NodeBean>)nodeSelectionChangeEvent.getSource();
        List<Application> application =  applicationService
                .getAllApplicationsByBriefLabelStartsWithIgnoreCase(viewApplicationDataIe.getApplicationName());
        getView().setVisible(node.isPresent());
        if(node.isPresent() && application.size() > 0) {
            getView().getReadNodeView().getPresenter().setApplicationDataId(null);
            getView().getWrapper().add(getView().getReadNodeView());
            Optional<DataMap> nodeDataMap = coherentSpaceService.findNodeDataMap(node.get());
            if (nodeDataMap.isPresent()) {
                getView().getReadNodeView()
                        .getPresenter().setApplicationDataId(applicationDataService
                        .getApplicationData(application.get(0).getId(), nodeDataMap.get()).getId());
            }
            getView().getReadNodeView().getPresenter().prepareModelAndView(nodeSelectionChangeEvent);
        }
    }

}
