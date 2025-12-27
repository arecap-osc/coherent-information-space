package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewStateSelectionProcessorManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class RoadFractoloniViewMultiImagePresenter extends RoadMultiImagePresenter {
	@Autowired
	private RoadFractoloniViewStateSelectionProcessorManager stateSelectionManager;

	@Autowired
	private GgmViewIe viewApplicationDataIe;

	@Autowired
	private ApplicationDataRepositoryService applicationDataService;

	@Override
	public RoadFractoloniViewStateSelectionProcessorManager getStateSelectionManager() {
		return stateSelectionManager;
	}

	@Override
	public RoadFractoloniViewLayerManager getMediaRendererLayerFactory() {
		return stateSelectionManager.getLayerManager();
	}

	@Override
	public MediaLayerFactory getMediaLayerFactory() {
		return stateSelectionManager.getLayerManager();
	}

	@Override
	public void doClickBusiness(Double screenX, Double screenY) {
		if (getMediaRendererLayerFactory().getMediaRendererLayers().size() > 0) {
			Double penStroke = ((ConfigurableGraphics2dStyle) getMediaRendererLayerFactory().getMediaLayers().get(0).getMediaRenderStyle()).getPenStroke();
			Optional<NodeBean> node = getCoherentSpaceService()
					.findNetworkNodeByScreenCoordinate(viewApplicationDataIe.getNetwork(), screenX, screenY, getScreenProperties(), penStroke * 2.5);
			Long currentApplicationId = getStateSelectionManager().getCurrentApplicationId();
			if (node.isPresent()) {
				DataMap dataMap = getCoherentSpaceService().findNodeDataMap(node.get()).get();
				ApplicationData applicationData = applicationDataService.getApplicationData(currentApplicationId, dataMap);
				if (applicationData.getSemantic() != null && ("".equals(applicationData.getSemantic()) == false)) {
					getStateSelectionManager().processNodeWithNeighboursSelection(node.get());
					composeOrRefreshImages();
				}
				return;
			}
			Optional<EdgeBean> edge = getCoherentSpaceService()
					.findNetworkEdgeByScreenCoordinate(viewApplicationDataIe.getNetwork(), screenX, screenY, getScreenProperties(), penStroke);
			if (edge.isPresent()) {
				DataMap dataMap = getCoherentSpaceService().findEdgeDataMap(edge.get()).get();
				ApplicationData applicationData = applicationDataService.getApplicationData(currentApplicationId, dataMap);
				if (applicationData.getSyntax() != null && ("".equals(applicationData.getSyntax()) == false)) {
					getStateSelectionManager().processEdgeSelection(edge.get());
					composeOrRefreshImages();
				}
				return;
			}
			getStateSelectionManager().resetSelection();
			composeOrRefreshImages();
		}
	}
}