package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.pagetitle.I18NPageTitle;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceMultiImageSettingsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadNetworkMediaLayerStyleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual.RoadManualMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual.RoadManualRoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.manual.RoadManualRoadOptionalPaneView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@I18NPageTitle(messageKey = "route.roadmanualroad.title")
@Route(value = "selected-road", layout = GgmSystemPwaLayout.class)
public class RoadManualRoadRoute extends GgmSystemRouteLayout {
    @Autowired
    private RoadManualMultiImageView multiImageView;

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private RoadApplicationAndNetworkComboboxAndButtonsView roadApplicationComboboxView;

    @Autowired
    private RoadNetworkMediaLayerStyleView mediaLayerStyleView;

    @Autowired
    private CoherentSpaceMultiImageSettingsView multiImageSettingsView;

    @Autowired
    private RoadManualRoadOptionalPaneView roadManualSelectionOtionalPaneView;

    @Autowired
    private RoadManualRoadStateSelectionManager roadManualRoadStateSelectionManager;

    @Override
    protected void buildRouteLayout() {
        topBarView.add(roadApplicationComboboxView);
        multiImageSettingsView.setMultiImageView(multiImageView);
        multiImageSettingsView.setNetworkMediaLayerStyleView(mediaLayerStyleView);
        multiImageView.add(multiImageSettingsView);
        add(roadManualSelectionOtionalPaneView, multiImageView, mediaLayerStyleView);
        topBarView.getPresenter().setupPresenter(roadManualRoadStateSelectionManager, roadManualRoadStateSelectionManager,
                multiImageView.getPresenter());
    }


    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "msg.route.breadcrumb.manualroad";
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/road_logo.png";
    }


}
