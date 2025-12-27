package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.GgmViewStateSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ScreenProperties;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class GgmViewMultiImagePresenter extends MultiImagePresenter {

    @Autowired
    private GgmViewMediaLayerManager mediaLayerManager;

    @Autowired
    private GgmViewStateSelectionProcessor stateSelectionProcessor;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private GgmViewIe viewApplicationDataIe;

    @Autowired
    private ScreenProperties screenProperties;

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Override
    public MediaRenderLayerFactory getMediaRendererLayerFactory() {
        return mediaLayerManager;
    }

    @Override
    public GgmViewMultiImageView getView() {
        return (GgmViewMultiImageView) super.getView();
    }

    @Override
    public void doClickBusiness(Double screenX, Double screenY) {
        if (mediaLayerManager.getMediaRendererLayers().size() > 0) {
            Double penStroke = ((ConfigurableGraphics2dStyle) mediaLayerManager.getMediaLayers().get(0).getMediaRenderStyle()).getPenStroke();
            Optional<NodeBean> node = coherentSpaceService
                    .findNetworkNodeByScreenCoordinate(viewApplicationDataIe.getNetwork(), screenX, screenY, screenProperties, penStroke * 2.5);
            Long currentApplicationId = getCurrentApplicationId();
            if (node.isPresent()) {
                DataMap dataMap = coherentSpaceService.findNodeDataMap(node.get()).get();
                ApplicationData applicationData = applicationDataService.getApplicationData(currentApplicationId, dataMap);
                if (applicationData.getSemantic() != null && ("".equals(applicationData.getSemantic()) == false)) {
                    stateSelectionProcessor.processNodeWithNeighboursSelection(node.get());
                    composeOrRefreshImages();
                }
                return;
            }
            Optional<EdgeBean> edge = coherentSpaceService
                    .findNetworkEdgeByScreenCoordinate(viewApplicationDataIe.getNetwork(), screenX, screenY, screenProperties, penStroke);
            if (edge.isPresent()) {
                DataMap dataMap = coherentSpaceService.findEdgeDataMap(edge.get()).get();
                ApplicationData applicationData = applicationDataService.getApplicationData(currentApplicationId, dataMap);
                if (applicationData.getSyntax() != null && ("".equals(applicationData.getSyntax()) == false)) {
                    stateSelectionProcessor.processEdgeSelection(edge.get());
                    composeOrRefreshImages();
                }
                return;
            }
            stateSelectionProcessor.resetSelection();
            composeOrRefreshImages();
        }
    }

    @Override
    public void doRightClickBusiness(Double screenX, Double screenY) {
        if (mediaLayerManager.getMediaRendererLayers().size() > 0) {
            Double penStroke = ((ConfigurableGraphics2dStyle) mediaLayerManager.getMediaLayers().get(0).getMediaRenderStyle()).getPenStroke();
            Optional<NodeBean> node = coherentSpaceService
                    .findNetworkNodeByScreenCoordinate(viewApplicationDataIe.getNetwork(), screenX, screenY, screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                DataMap dataMap = coherentSpaceService.findNodeDataMap(node.get()).get();
                ApplicationData applicationData = applicationDataService.getApplicationData(getCurrentApplicationId(), dataMap);
                if (applicationData.getSemantic() != null && ("".equals(applicationData.getSemantic()) == false)) {
                    stateSelectionProcessor.processNodeMarkerSelection(node.get());
                    composeOrRefreshImages();
                }
                return;
            }
            Optional<EdgeBean> edge = coherentSpaceService.findNetworkEdgeByScreenCoordinate(mediaLayerManager.getCurrentNetwork(),
                    screenX, screenY, getScreenProperties(), penStroke);
            if (edge.isPresent()) {
                DataMap fromDataMap = coherentSpaceService.findNodeDataMap(edge.get().getFromNode()).get();
                ApplicationData fromApplicationData = applicationDataService.getApplicationData(getCurrentApplicationId(), fromDataMap);
                DataMap toDataMap = coherentSpaceService.findNodeDataMap(edge.get().getToNode()).get();
                ApplicationData toApplicationData = applicationDataService.getApplicationData(getCurrentApplicationId(), toDataMap);

                if (fromApplicationData.getSemantic() != null && ("".equals(fromApplicationData.getSemantic()) == false) &&
                        toApplicationData.getSemantic() != null && ("".equals(toApplicationData.getSemantic()) == false)) {
                    stateSelectionProcessor.processEdgeMarkerSelection(edge.get());
                    composeOrRefreshImages();
                    return;
                }

            }
            stateSelectionProcessor.resetSelection();
            composeOrRefreshImages();
        }
    }

    @Override
    public void onMouseHover(Double offsetX, Double offsetY) {
        if (viewApplicationDataIe.getNetwork() != null) {
            Double penStroke = mediaLayerManager.getNetworkPenStroke(viewApplicationDataIe.getNetwork());
            Optional<NodeBean> node = coherentSpaceService
                    .findNetworkNodeByScreenCoordinate(viewApplicationDataIe.getNetwork(), offsetX, offsetY,
                            screenProperties, penStroke * 2.5);
            if (node.isPresent()) {
                getView().getNodeInformationView().getPresenter().setNodeInformation(offsetX, offsetY, node.get());
                return;
            }

            getView().getNodeInformationView().getPresenter().keepVisibleIfMouseIsOver(offsetX, offsetY);
        }
    }

    private Long getCurrentApplicationId() {
        List<Application> applicationList = applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase(viewApplicationDataIe.getApplicationName());
        if (applicationList.size() > 0) {
            return applicationList.get(0).getId();
        }
        return null;
    }
}
