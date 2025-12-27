package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.GgmStateSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.ForceRefreshGgmMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.NeedRefreshUpdatedLayerEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.RefreshGgmMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ScreenProperties;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.Optional;

public abstract class GgmMultiImagePresenter extends MultiImagePresenter {

    @Autowired
    private GgmMediaLayerManager ggmMediaLayerManager;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private ScreenProperties screenProperties;

    @Override
    public GgmMediaLayerManager getMediaRendererLayerFactory() {
        return ggmMediaLayerManager;
    }

    @Override
    public GgmMultiImageView getView() {
        return (GgmMultiImageView) super.getView();
    }

    @Override
    public void onMouseHover(Double offsetX, Double offsetY) {
        if (getStateSelectionProcessor().hasNetwork() && getView().getNodeInformationView().isEnabled()) {
            Double penStroke = ggmMediaLayerManager.getNetworkPenStroke(getStateSelectionProcessor().getSelectionNetwork());
            Optional<NodeBean> node = coherentSpaceService
                    .findNetworkNodeByScreenCoordinate(getStateSelectionProcessor().getSelectionNetwork(), offsetX, offsetY,
                            screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                getView().getNodeInformationView().getPresenter().setNodeInformation(offsetX, offsetY, node.get());
                return;
            }

            getView().getNodeInformationView().getPresenter().keepVisibleIfMouseIsOver(offsetX, offsetY);
        }
    }

    public void doClickBusiness(Double screenX, Double screenY) {
        if (getStateSelectionProcessor().hasNetwork()) {
            Double penStroke = getMediaRendererLayerFactory()
                    .getNetworkPenStroke(getStateSelectionProcessor().getSelectionNetwork());
            Optional<NodeBean> node = getCoherentSpaceService()
                    .findNetworkNodeByScreenCoordinate(getStateSelectionProcessor().getSelectionNetwork(), screenX, screenY, screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                getStateSelectionProcessor().processNodeWithNeighboursSelection(node.get());
                composeOrRefreshImages();
                return;
            }
            Optional<EdgeBean> edge = getCoherentSpaceService()
                    .findNetworkEdgeByScreenCoordinate(getStateSelectionProcessor()
                            .getSelectionNetwork(), screenX, screenY, getScreenProperties(), penStroke);
            if (edge.isPresent()) {
                getStateSelectionProcessor().resetSelection();
                getStateSelectionProcessor().processNodeSelection(edge.get().getFromNode());
                getStateSelectionProcessor().processNodeSelection(edge.get().getToNode());
                composeOrRefreshImages();
                return;
            }
            getStateSelectionProcessor().resetSelection();
            composeOrRefreshImages();
        }
    }


    @Override
    public void doRightClickBusiness(Double screenX, Double screenY) {
        if (getStateSelectionProcessor().hasNetwork()) {
            Double penStroke = getMediaRendererLayerFactory()
                    .getNetworkPenStroke(getStateSelectionProcessor().getSelectionNetwork());
            Optional<NodeBean> node = coherentSpaceService
                    .findNetworkNodeByScreenCoordinate(getStateSelectionProcessor().getSelectionNetwork(),
                            screenX, screenY, screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                getStateSelectionProcessor().processNodeMarkerSelection(node.get());
                composeOrRefreshImages();
                return;
            }
            Optional<EdgeBean> edge = getCoherentSpaceService()
                    .findNetworkEdgeByScreenCoordinate(getStateSelectionProcessor()
                            .getSelectionNetwork(), screenX, screenY, getScreenProperties(), penStroke);
            if(edge.isPresent()) {
                getStateSelectionProcessor().processEdgeMarkerSelection(edge.get());
                composeOrRefreshImages();
                return;
            }

            composeOrRefreshImages();
        }
    }

    @EventBusListenerMethod
    public void onRefreshGgmMultiImageEvent(RefreshGgmMultiImageEvent event) {
        refreshAll();
    }

    @EventBusListenerMethod
    public void onForceRefreshGgmMultiImageEvent(ForceRefreshGgmMultiImageEvent event) {
        rerenderImages(true);
    }

    @EventBusListenerMethod
    public void onNeedRefreshUpdatedLayerEvent(NeedRefreshUpdatedLayerEvent event) {
        composeOrRefreshImages(false);
    }

    protected abstract GgmStateSelectionProcessor getStateSelectionProcessor();

    public CoherentSpaceService getCoherentSpaceService() {
        return coherentSpaceService;
    }

    @Override
    public ScreenProperties getScreenProperties() {
        return screenProperties;
    }
}