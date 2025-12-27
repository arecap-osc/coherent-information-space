package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;

import java.util.Locale;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application.node.NodeSelectionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmNetworkMediaLayerStyleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.node.GgmNodeMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.option.GgmLeftBarOptionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceMultiImageSettingsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.application.GgmApplicationAndNetworkComboboxAndButtonsView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftBarUpperSideView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "coherent-space-nodes", layout = GgmSystemPwaLayout.class)
public class GgmNodeRoute extends GgmSystemRouteLayout {

	@Autowired
	private TopBarView topBarView;

	@Autowired
	private GgmApplicationAndNetworkComboboxAndButtonsView applicationView;

	@Autowired
	private NodeSelectionView nodeSelectionView;

	@Autowired
    private LeftBarUpperSideView leftBarUpperSideView;

	@Autowired
    private GgmLeftBarOptionView ggmLeftBarOptionView;

    @Autowired
	private GgmNodeMultiImageView multiImageView;

	@Autowired
	private GgmNetworkMediaLayerStyleView mediaLayerStyleView;

	@Autowired
	private CoherentSpaceMultiImageSettingsView multiImageSettingsView;

	@Autowired
    private GgmNodeSelectionProcessor nodeSelectionProcessor;

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
		nodeSelectionView.setVisible(false);
		add(nodeSelectionView, multiImageView, mediaLayerStyleView);

		ggmLeftBarOptionView.getNodeEdgeSearchDialogView().getPresenter().setupPresenter(nodeSelectionProcessor,
				multiImageView.getPresenter());
		topBarView.getPresenter().setupPresenter(nodeSelectionProcessor, mediaLayerManager, multiImageView.getPresenter());
	}

	@Override
	protected String getRouteNameForBredCrumbAndTooltip() {
		return "msg.route.breadcrumb.noderoute";
	}

	@Override
	protected String getRouteIconPath() {
		return "/frontend/img/coherent_space_logo.png";
	}

}
