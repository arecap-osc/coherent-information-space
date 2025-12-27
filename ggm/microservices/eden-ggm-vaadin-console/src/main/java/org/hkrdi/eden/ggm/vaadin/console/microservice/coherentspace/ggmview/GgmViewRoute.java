package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application.ReadEdgeSelectionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application.ReadNodeSelectionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media.GgmViewMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media.GgmViewMultiImageScaleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media.GgmViewMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.component.ConfirmDialog;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route(value = "coherent-space-view", layout = GgmSystemPwaLayout.class)
public class GgmViewRoute extends GgmSystemRouteLayout {

    @Autowired
    private GgmViewIe viewApplicationDataIe;

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private GgmViewMultiImageView multiImageView;

    @Autowired
    private GgmViewMultiImageScaleView multiImageScale;

    @Autowired
    private ReadNodeSelectionView readNodeSelectionView;

    @Autowired
    private ReadEdgeSelectionView readEdgeSelectionView;

    @Autowired
    private GgmViewStateSelectionProcessor ggmViewStateSelectionProcessor;

    @Autowired
    private GgmViewMediaLayerManager mediaLayerManager;

    @Override
    protected void buildRouteLayout() {
        topBarView.add(new Label(viewApplicationDataIe.getApplicationName()));
        multiImageView.add(multiImageScale);
        readNodeSelectionView.setVisible(false);
        readEdgeSelectionView.setVisible(false);
        topBarView.getPresenter().setupPresenter(ggmViewStateSelectionProcessor, mediaLayerManager,
                multiImageView.getPresenter());

        add(readNodeSelectionView, readEdgeSelectionView, multiImageView);

        ConfirmDialog dialog = new ConfirmDialog("msg.ggmview.dialog.title",
                "msg.ggmview.dialog.information", e -> {});
        dialog.open();


    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/road_logo.png";
    }

    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "msg.route.breadcrumb.ggmview";
    }

}
