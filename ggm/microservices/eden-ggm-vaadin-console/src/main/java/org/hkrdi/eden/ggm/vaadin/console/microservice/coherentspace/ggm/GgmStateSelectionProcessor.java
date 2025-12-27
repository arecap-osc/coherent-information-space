package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.event.ClusterSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmNetworkChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmFirstNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmNeighborNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmPathLineMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSecondNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.GgmSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.MarkedSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class GgmStateSelectionProcessor  implements GgmSelectionProcessor {


    private Optional<EdgeBean> edge = Optional.empty();

    private List<NodeBean> neighbours;

    private Optional<Application> application = Optional.empty();

    private Optional<String> network = Optional.empty();

    @Autowired
    private GgmMediaLayerManager layerManager;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private UIEventBus uiEventBus;

    @PostConstruct
    private void initEventBus() {
        uiEventBus.subscribe(this);
    }

    @PreDestroy
    private void predestroyEventBus() {
        uiEventBus.unsubscribe(this);
    }

    protected UIEventBus getUiEventBus() {
        return uiEventBus;
    }

    public Optional<EdgeBean> getEdge() {
        return edge;
    }

    public void setEdge(Optional<EdgeBean> edge) {
        this.edge = edge;
    }

    public List<NodeBean> getNeighbours() {
        return neighbours;
    }

    public Optional<Application> getApplication() {
        return application;
    }

    public GgmMediaLayerManager getLayerManager() {
        return layerManager;
    }

    public CoherentSpaceService getCoherentSpaceService() {
        return coherentSpaceService;
    }

    @EventBusListenerMethod
    public void onApplicationChangeEvent(GgmApplicationChangeEvent ggmApplicationChangeEvent) {
        application = (Optional<Application>) ggmApplicationChangeEvent.getSource();
    }

    @EventBusListenerMethod
    public void onNetworkSelectionChangeEvent(GgmNetworkChangeEvent networkSelectionChangeEvent) {
        network = (Optional<String>) networkSelectionChangeEvent.getSource();
        publishResetSelection();
    }

    public boolean hasNetwork() {
        return network.isPresent();
    }

    public String getSelectionNetwork() {
        return hasNetwork() ? network.get() : null;
    }

    @Override
    public void processNodeWithNeighboursSelection(NodeBean nodeBean) {
        processNodeSelection(nodeBean);
        if(edge.isPresent() && edge.get().getToNode() == null) {
            selectNodeNeighbours(edge.get().getFromNode());
        }
    }

    @Override
    public abstract void processNodeSelection(NodeBean nodeBean);

    @Override
    public abstract void processEdgeSelection(EdgeBean edge);

    protected void selectFistNode(NodeBean node) {
        uiEventBus.publish(this, new ClusterSelectionChangeEvent(Optional.empty()));
        edge = Optional.of(new EdgeBean(node, null));
        layerManager.getFirstNodeMediaLayer(node.getNetwork()).setNode(node);
    }

    protected void selectSecondNode(NodeBean node) {
        edge.get().setToNode(node);
        layerManager.getSecondNodeMediaLayer(node.getNetwork()).setEndNodeIndex(node.getAddressIndex());
        layerManager.getPathLineMediaLayer(node.getNetwork()).setEdge(edge.get());
    }

    protected void selectNodeNeighbours(NodeBean node) {
        neighbours = coherentSpaceService.findNodeNeighbors(node).stream()
                .collect(Collectors.toList());
        layerManager.getNeighborNodeMediaLayer(node.getNetwork())
                .setNodes(neighbours);
    }

    protected void selectEdge(EdgeBean edge) {
        this.edge = Optional.of(edge);
        layerManager.getFirstNodeMediaLayer(edge.getFromNode().getNetwork()).setNode(edge.getFromNode());
        layerManager.getSecondNodeMediaLayer(edge.getToNode().getNetwork()).setEndNodeIndex(edge.getToNode().getAddressIndex());
        layerManager.getPathLineMediaLayer(edge.getFromNode().getNetwork()).setEdge(edge);
    }

    protected abstract void publishResetSelection();

    public void resetSelection() {
        edge = Optional.empty();
        publishResetSelection();
        if(hasNetwork()) {
            resetSelectionOnMediaRendererLayers(network.get());
        }
    }

    @Override
    public MarkedNodesMediaLayer getMarkedNodesMediaLayer(String network) {
        return layerManager.getMarkedNodesMediaLayer(network);
    }

    @Override
    public MarkedEdgesMediaLayer getMarkedEdgesMediaLayer(String network) {
        return layerManager.getMarkedEdgesMediaLayer(network);
    }

    protected void resetSelectionOnMediaRendererLayers(String network) {
        resetSelectionOnFirstNodeMediaLayer(network);
        resetSelectionOnSecondNodeMediaLayer(network);
        resetSelectionOnEdgeMediaLayer(network);
        resetSelectionOnNodeNeighboursMediaLayer(network);
    }

    protected void resetSelectionOnFirstNodeMediaLayer(String network) {
        GgmFirstNodeMediaLayer firstNodeMediaLayer = layerManager.getFirstNodeMediaLayer(network);
        firstNodeMediaLayer.setNode(null);
        firstNodeMediaLayer.setNeedRefresh(true);
    }

    protected void resetSelectionOnSecondNodeMediaLayer(String network) {
        GgmSecondNodeMediaLayer secondNodeMediaLayer = layerManager.getSecondNodeMediaLayer(network);
        secondNodeMediaLayer.setEndNodeIndex(null);
        secondNodeMediaLayer.setNeedRefresh(true);
    }

    protected void resetSelectionOnEdgeMediaLayer(String network) {
        GgmPathLineMediaLayer pathLineMediaLayer = layerManager.getPathLineMediaLayer(network);
        pathLineMediaLayer.setEdge(null);
        pathLineMediaLayer.setNeedRefresh(true);
    }

    protected void resetSelectionOnNodeNeighboursMediaLayer(String network) {
        GgmNeighborNodeMediaLayer neighborNodeMediaLayer = layerManager.getNeighborNodeMediaLayer(network);
        neighborNodeMediaLayer.setNodes(null);
        neighborNodeMediaLayer.setNeedRefresh(true);
    }
}
