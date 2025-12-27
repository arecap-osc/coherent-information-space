package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.ForceRefreshRoadMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.NodeRoadSelectionClickEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RefreshRoadMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.MarkedSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ScreenProperties;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.Optional;

public abstract class RoadMultiImagePresenter extends MultiImagePresenter {
    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private ScreenProperties screenProperties;

    public abstract StateSelectionManager getStateSelectionManager();

    public abstract MediaRenderLayerFactory getMediaRendererLayerFactory();

    public abstract MediaLayerFactory getMediaLayerFactory();

    @Override
    public RoadMultiImageView getView() {
        return (RoadMultiImageView) super.getView();
    }

    @Override
    public void doRightClickBusiness(Double screenX, Double screenY) {
        if (getStateSelectionManager().getCurrentNetwork() != null) {
            Double penStroke = getMediaLayerFactory().getNetworkPenStroke(getStateSelectionManager().getCurrentNetwork());
            Optional<NodeBean> node = coherentSpaceService.findNetworkNodeByScreenCoordinate(getStateSelectionManager().getCurrentNetwork(),
                    screenX, screenY, screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                ((MarkedSelectionProcessor) getStateSelectionManager()).processNodeMarkerSelection(node.get());
				composeOrRefreshImages();
                return;
            }
            Optional<EdgeBean> edge = coherentSpaceService.findNetworkEdgeByScreenCoordinate(getStateSelectionManager().getCurrentNetwork(),
                    screenX, screenY, screenProperties, penStroke);
			if (edge.isPresent()) {
				((MarkedSelectionProcessor) getStateSelectionManager()).processEdgeMarkerSelection(edge.get());
				composeOrRefreshImages();
				return;
			}

            composeOrRefreshImages();
        }
    }

    @Override
    public void doClickBusiness(Double screenX, Double screenY) {
        if (getStateSelectionManager().getCurrentNetwork() != null) {
//			Double penStroke = ((CoherentSpaceNetworkMediaLayer)mediaLayerFactory
//									.getMediaLayerByName(getStateSelectionManager().getCurrentNetwork())
//									.get().getMediaRenderStyle())
//								.getPenStroke();
            Double penStroke = getMediaLayerFactory().getNetworkPenStroke(getStateSelectionManager().getCurrentNetwork());
            Optional<NodeBean> node = coherentSpaceService.findNetworkNodeByScreenCoordinate(getStateSelectionManager().getCurrentNetwork(), screenX, screenY, screenProperties, penStroke * 2.5);

            if (node.isPresent()) {
//	        	stateSelectionManager.processNodeRoadSelection(node.get());
                getUIEventBus().publish(this, new NodeRoadSelectionClickEvent(node.get()));
            }
//
//			//on dblClick we clear start/end node and also the road
//			stateSelectionManager.paintRoad();	
//			
//			//repaint
//			this.composeOrRefreshImages();
        }
    }

    @Override
    public void onMouseHover(Double offsetX, Double offsetY) {
        if (getStateSelectionManager().getCurrentNetwork() != null && getView().getNodeInformationView().isEnabled()) {
            Double penStroke = getMediaLayerFactory().getNetworkPenStroke(getStateSelectionManager().getCurrentNetwork());
            Optional<NodeBean> node = coherentSpaceService
                    .findNetworkNodeByScreenCoordinate(getStateSelectionManager().getCurrentNetwork(), offsetX, offsetY,
                            screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                getView().getNodeInformationView().getPresenter().setNodeInformation(offsetX, offsetY, node.get());
                return;
            }

            getView().getNodeInformationView().getPresenter().keepVisibleIfMouseIsOver(offsetX, offsetY);
        }
    }

    @EventBusListenerMethod
    public void onRefreshRoadMultiImageEvent(RefreshRoadMultiImageEvent event) {
        refreshAll();
    }

    @EventBusListenerMethod
    public void onForceRefreshRoadMultiImageEvent(ForceRefreshRoadMultiImageEvent event) {
        rerenderImages(true);
    }

    public CoherentSpaceService getCoherentSpaceService() {
        return coherentSpaceService;
    }
}
