package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm;

import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application.edge.EdgeSelectionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmNetworkMediaLayerStyleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.edge.GgmEdgeMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.option.GgmLeftBarOptionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceMultiImageSettingsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.application.GgmApplicationAndNetworkComboboxAndButtonsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftBarUpperSideView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "coherent-space-edges", layout = GgmSystemPwaLayout.class)
public class GgmEdgeRoute extends GgmSystemRouteLayout {

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private GgmApplicationAndNetworkComboboxAndButtonsView applicationView;

    @Autowired
    private EdgeSelectionView edgeSelectionView;

    @Autowired
    private LeftBarUpperSideView leftBarUpperSideView;

    @Autowired
    private GgmLeftBarOptionView ggmLeftBarOptionView;

    @Autowired
    private GgmEdgeMultiImageView multiImageView;

    @Autowired
    private GgmNetworkMediaLayerStyleView mediaLayerStyleView;

    @Autowired
    private CoherentSpaceMultiImageSettingsView multiImageSettingsView;

    @Autowired
    private GgmEdgeSelectionProcessor edgeSelectionProcessor;

    @Autowired
    private GgmMediaLayerManager mediaLayerManager;

    @Override
    protected void buildRouteLayout() {
        setSizeFull();
        topBarView.add(applicationView);
        leftBarUpperSideView.add(ggmLeftBarOptionView);
        multiImageSettingsView.setMultiImageView(multiImageView);
        multiImageSettingsView.setNetworkMediaLayerStyleView(mediaLayerStyleView);
        multiImageView.add(multiImageSettingsView);
        edgeSelectionView.setVisible(false);
        add(edgeSelectionView, multiImageView, mediaLayerStyleView);

        ggmLeftBarOptionView.getNodeEdgeSearchDialogView().getPresenter().setupPresenter(edgeSelectionProcessor,
                multiImageView.getPresenter());
        topBarView.getPresenter().setupPresenter(edgeSelectionProcessor, mediaLayerManager, multiImageView.getPresenter());
    }

    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "msg.route.breadcrumb.edgeroute";
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/coherent_space_logo.png";
    }

}
