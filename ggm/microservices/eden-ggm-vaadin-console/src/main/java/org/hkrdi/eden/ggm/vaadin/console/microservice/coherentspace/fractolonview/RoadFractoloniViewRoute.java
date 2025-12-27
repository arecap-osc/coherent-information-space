package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewMultiImageScaleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewOptionalPaneView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewStateSelectionProcessorManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application.ReadEdgeSelectionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application.ReadNodeSelectionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.component.ConfirmDialog;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "road-fractoloni-view", layout = GgmSystemPwaLayout.class)
public class RoadFractoloniViewRoute extends GgmSystemRouteLayout {
    @Autowired
    private RoadFractoloniViewMultiImageView multiImageView;

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private RoadFractoloniViewMultiImageScaleView multiImageSettingsView;

    @Autowired
    private RoadFractoloniViewOptionalPaneView roadFractoloniViewOptionalPaneView;

    @Autowired
    private RoadFractoloniViewStateSelectionProcessorManager roadFractoloniRoadStateSelectionManager;

    @Autowired
    private ReadNodeSelectionView readNodeSelectionView;

    @Autowired
    private ReadEdgeSelectionView readEdgeSelectionView;

    @Autowired
    private GgmViewIe ggmViewIe;

    @Override
    protected void buildRouteLayout() {
        roadFractoloniRoadStateSelectionManager.setApplicationAndNetwork();
        topBarView.add(new Label(ggmViewIe.getApplicationName()));
        multiImageView.add(multiImageSettingsView);
        add(roadFractoloniViewOptionalPaneView, multiImageView);
        topBarView.getPresenter().setupPresenter(roadFractoloniRoadStateSelectionManager, roadFractoloniRoadStateSelectionManager,
                multiImageView.getPresenter());

        readNodeSelectionView.setVisible(false);
        readEdgeSelectionView.setVisible(false);
        readNodeSelectionView.setStyleForRoadFractoloniView(roadFractoloniViewOptionalPaneView);
        readEdgeSelectionView.setStyleForRoadFractoloniView(roadFractoloniViewOptionalPaneView);
        add(readNodeSelectionView, readEdgeSelectionView);

        ConfirmDialog dialog = new ConfirmDialog("msg.ggmview.dialog.title",
                "msg.ggmview.dialog.information", e -> {
        });
        dialog.open();
    }


    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "msg.route.breadcrumb.ggmview";
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/road_logo.png";
    }


}
