package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.event.EdgeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.event.NodeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmFirstNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmNeighborNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmPathLineMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSecondNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media.GgmViewMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.GgmSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.MarkedSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class GgmViewStateSelectionProcessor implements GgmSelectionProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Optional<NodeBean> node = Optional.empty();


    private Optional<EdgeBean> edge = Optional.empty();

    private List<NodeBean> neighbours;

    @Autowired
    private GgmViewMediaLayerManager mediaLayerManager;

    @Autowired
    private GgmViewIe viewApplicationDataIe;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Autowired
    private EventBus.UIEventBus uiEventBus;

    public void processNodeSelection(NodeBean nodeBean) {
        resetSelectionOnMediaRendererLayers(viewApplicationDataIe.getNetwork());
        neighbours = new ArrayList<>();
        if (node.isPresent() && node.get().equals(nodeBean)) {
            LOGGER.info("User deselected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-view route");
            node = Optional.empty();
            resetSelection();
            return;
        }
        node = Optional.of(nodeBean);
        publishNodeSelection(nodeBean);
        if (node.isPresent()) {
            LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-view route");
            selectFistNode(node.get());
        }

    }

    public void processNodeWithNeighboursSelection(NodeBean nodeBean) {
        if (node.isPresent() && node.get().equals(nodeBean) == false
                && neighbours.contains(nodeBean)) {
            if (coherentSpaceService.findEdgeDataMap(new EdgeBean(node.get(), nodeBean)).isPresent()) {
                processEdgeSelection(new EdgeBean(node.get(), nodeBean));
            } else {
                processEdgeSelection(new EdgeBean(nodeBean, node.get()));
            }
        } else {
            processNodeSelection(nodeBean);
            if (node.isPresent()) {
                selectNodeNeighbours(nodeBean);
            }
        }
    }

    public void processEdgeSelection(EdgeBean edgeBean) {
        resetSelectionOnMediaRendererLayers(viewApplicationDataIe.getNetwork());
        neighbours = new ArrayList<>();
        if (edge.isPresent() && edge.get().equals(edgeBean)) {
            //se ajunge vreodata aici?
            LOGGER.info("User deselected edge composed by FROM node #" + edgeBean.getFromNode().getAddressIndex() +
                    " and TO node #" + edgeBean.getToNode().getAddressIndex() + " from network " + edgeBean.getFromNode().getNetwork() + " on coherent-space-view route");

            edge = Optional.empty();
            resetSelection();
            return;
        }
        edge = Optional.of(edgeBean);
        publishEdgeSelection(edgeBean);

        LOGGER.info("User selected edge composed by FROM node #" + edgeBean.getFromNode().getAddressIndex() +
                " and TO node #" + edgeBean.getToNode().getAddressIndex() + " from network " + edgeBean.getFromNode().getNetwork() + " on coherent-space-view route");
        selectEdge(edge.get());
    }

    public void resetSelection() {
        node = Optional.empty();
        edge = Optional.empty();
        resetSelectionOnMediaRendererLayers(viewApplicationDataIe.getNetwork());
        publishResetSelection();
    }

    private void selectFistNode(NodeBean node) {
        mediaLayerManager.getFirstNodeMediaLayer(node.getNetwork()).setNode(node);
    }

    private void selectNodeNeighbours(NodeBean node) {
        List<Application> application = applicationService
                .getAllApplicationsByBriefLabelStartsWithIgnoreCase(viewApplicationDataIe.getApplicationName());
        List<NodeBean> nodesWithData = new ArrayList<>();
        if (application.size() > 0) {
            nodesWithData.addAll(coherentSpaceService
                    .getInformationContainedNetworkNodeBeans(viewApplicationDataIe.getNetwork(), application.get(0).getId()));
        }
        neighbours = coherentSpaceService.findNodeNeighbors(node).stream()
                .filter(nodeBean -> nodesWithData.contains(nodeBean))
                .collect(Collectors.toList());
        mediaLayerManager.getNeighborNodeMediaLayer(node.getNetwork())
                .setNodes(neighbours);
    }

    private void selectEdge(EdgeBean edge) {
        mediaLayerManager.getFirstNodeMediaLayer(edge.getFromNode().getNetwork()).setNode(edge.getFromNode());
        mediaLayerManager.getSecondNodeMediaLayer(edge.getToNode().getNetwork()).setEndNodeIndex(edge.getToNode().getAddressIndex());
        mediaLayerManager.getPathLineMediaLayer(edge.getFromNode().getNetwork()).setEdge(edge);
    }

    private void publishNodeSelection(NodeBean node) {
        uiEventBus.publish(this, new NodeSelectionChangeEvent(Optional.of(node)));
        uiEventBus.publish(this, new EdgeSelectionChangeEvent(Optional.empty()));
    }

    private void publishEdgeSelection(EdgeBean edge) {
        uiEventBus.publish(this, new NodeSelectionChangeEvent(Optional.empty()));
        uiEventBus.publish(this, new EdgeSelectionChangeEvent(Optional.of(edge)));
    }

    private void publishResetSelection() {
        uiEventBus.publish(this, new NodeSelectionChangeEvent(Optional.empty()));
        uiEventBus.publish(this, new EdgeSelectionChangeEvent(Optional.empty()));
    }

    private void resetSelectionOnMediaRendererLayers(String network) {
        resetSelectionOnFirstNodeMediaLayer(network);
        resetSelectionOnSecondNodeMediaLayer(network);
        resetSelectionOnEdgeMediaLayer(network);
        resetSelectionOnNodeNeighboursMediaLayer(network);
    }

    private void resetSelectionOnFirstNodeMediaLayer(String network) {
        GgmFirstNodeMediaLayer firstNodeMediaLayer = mediaLayerManager.getFirstNodeMediaLayer(network);
        firstNodeMediaLayer.setNode(null);
        firstNodeMediaLayer.setNeedRefresh(true);
    }

    private void resetSelectionOnSecondNodeMediaLayer(String network) {
        GgmSecondNodeMediaLayer secondNodeMediaLayer = mediaLayerManager.getSecondNodeMediaLayer(network);
        secondNodeMediaLayer.setEndNodeIndex(null);
        secondNodeMediaLayer.setNeedRefresh(true);
    }

    private void resetSelectionOnEdgeMediaLayer(String network) {
        GgmPathLineMediaLayer pathLineMediaLayer = mediaLayerManager.getPathLineMediaLayer(network);
        pathLineMediaLayer.setEdge(null);
        pathLineMediaLayer.setNeedRefresh(true);
    }

    private void resetSelectionOnNodeNeighboursMediaLayer(String network) {
        GgmNeighborNodeMediaLayer neighborNodeMediaLayer = mediaLayerManager.getNeighborNodeMediaLayer(network);
        neighborNodeMediaLayer.setNodes(null);
        neighborNodeMediaLayer.setNeedRefresh(true);
    }

    @Override
    public MarkedNodesMediaLayer getMarkedNodesMediaLayer(String network) {
        return mediaLayerManager.getMarkedNodesMediaLayer(network);
    }

    @Override
    public MarkedEdgesMediaLayer getMarkedEdgesMediaLayer(String network) {
        return mediaLayerManager.getMarkedEdgesMediaLayer(network);
    }
}
