package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road;

import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceMultiImageSettingsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadNetworkMediaLayerStyleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated.RoadCalculatedMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated.RoadCalculatedRoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.calculated.RoadCalcualtedRoadOptionalPaneView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "computed-road", layout = GgmSystemPwaLayout.class)
public class RoadRoute extends GgmSystemRouteLayout {
    @Autowired
    private RoadCalculatedMultiImageView multiImageView;

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private RoadApplicationAndNetworkComboboxAndButtonsView roadApplicationComboboxView;

    @Autowired
    private RoadNetworkMediaLayerStyleView mediaLayerStyleView;

    @Autowired
    private CoherentSpaceMultiImageSettingsView multiImageSettingsView;

    @Autowired
    private RoadCalcualtedRoadOptionalPaneView roadOtionalPaneView;

    @Autowired
    private RoadCalculatedRoadStateSelectionManager roadCalculatedRoadStateSelectionManager;

    @Override
    protected void buildRouteLayout() {
        topBarView.add(roadApplicationComboboxView);
        multiImageSettingsView.setMultiImageView(multiImageView);
        multiImageSettingsView.setNetworkMediaLayerStyleView(mediaLayerStyleView);
        multiImageView.add(multiImageSettingsView);
        add(roadOtionalPaneView, multiImageView, mediaLayerStyleView);
        topBarView.getPresenter().setupPresenter(roadCalculatedRoadStateSelectionManager, roadCalculatedRoadStateSelectionManager,
                multiImageView.getPresenter());
    }

    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "msg.route.breadcrumb.automaticroad";
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/road_logo.png";
    }


}
