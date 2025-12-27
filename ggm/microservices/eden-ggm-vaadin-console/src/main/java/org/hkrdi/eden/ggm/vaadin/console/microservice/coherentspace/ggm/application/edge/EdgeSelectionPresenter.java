package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application.edge;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.event.EdgeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.EventObject;
import java.util.Optional;


@SpringComponent
@UIScope
public class EdgeSelectionPresenter extends LeftSideContentPresenter {

    private Optional<Application> application = Optional.empty();

    private Optional<EdgeBean> edgeBean = Optional.empty();

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
    public EdgeSelectionView getView() {
        return (EdgeSelectionView) super.getView();
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
    public void onEdgeSelectionChangeEvent(EdgeSelectionChangeEvent edgeSelectionChangeEvent) {
        edgeBean = (Optional<EdgeBean>)edgeSelectionChangeEvent.getSource();
        getView().setVisible(edgeBean.isPresent());
        if(edgeBean.isPresent()) {
            setupApplicationDataViews();
        }
    }

    private void setupApplicationDataViews() {
        applicationDataIe.setSyntaxId(null);
        semanticMapIe.setSemanticMapId(null);
        if(application.isPresent()) {
            if (edgeBean.isPresent()) {
                Optional<DataMap> edgeDataMap = coherentSpaceService.findEdgeDataMap(edgeBean.get());
                if (edgeDataMap.isPresent()) {
                    applicationDataIe.setSyntaxId(applicationDataService
                            .getApplicationData(application.get().getId(), edgeDataMap.get())
                            .getId());
                }

            }
            semanticMapIe.setSemanticMapId(application.get().getSemanticGridId());
        }
        getView().getEdgeView().getPresenter().prepareModelAndView(null);
        getView().getPhraseView().getPresenter().prepareModelAndView(null);
    }

}
