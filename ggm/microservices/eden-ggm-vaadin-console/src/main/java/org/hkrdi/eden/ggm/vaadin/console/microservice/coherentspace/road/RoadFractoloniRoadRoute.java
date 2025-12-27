package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road;

import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceMultiImageSettingsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadNetworkMediaLayerStyleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.fractoloni.RoadFractoloniMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.fractoloni.RoadFractoloniRoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.fractoloni.RoadFractoloniRoadOptionalPaneView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "road-fractoloni", layout = GgmSystemPwaLayout.class)
public class RoadFractoloniRoadRoute extends GgmSystemRouteLayout {
    @Autowired
    private RoadFractoloniMultiImageView multiImageView;

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private RoadApplicationAndNetworkComboboxAndButtonsView roadApplicationComboboxView;

    @Autowired
    private RoadNetworkMediaLayerStyleView mediaLayerStyleView;

    @Autowired
    private CoherentSpaceMultiImageSettingsView multiImageSettingsView;

    @Autowired
    private RoadFractoloniRoadOptionalPaneView roadSelectionOtionalPaneView;

    @Autowired
    private RoadFractoloniRoadStateSelectionManager roadFractoloniRoadStateSelectionManager;

    @Override
    protected void buildRouteLayout() {
        topBarView.add(roadApplicationComboboxView);
        multiImageSettingsView.setMultiImageView(multiImageView);
        multiImageSettingsView.setNetworkMediaLayerStyleView(mediaLayerStyleView);
        multiImageView.add(multiImageSettingsView);
        add(roadSelectionOtionalPaneView, multiImageView, mediaLayerStyleView);
        topBarView.getPresenter().setupPresenter(roadFractoloniRoadStateSelectionManager, roadFractoloniRoadStateSelectionManager,
                multiImageView.getPresenter());
    }


    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "msg.route.breadcrumb.fractolons";
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/road_logo.png";
    }


}
