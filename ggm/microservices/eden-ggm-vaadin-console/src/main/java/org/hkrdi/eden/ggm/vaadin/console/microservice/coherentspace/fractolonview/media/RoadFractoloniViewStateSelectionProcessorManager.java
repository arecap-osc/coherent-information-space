package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.event.EdgeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.event.NodeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmFirstNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmNeighborNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmPathLineMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSecondNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadFractolonEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadFractolonNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadPathLinesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.*;
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
public class RoadFractoloniViewStateSelectionProcessorManager extends RoadStateSelectionManager {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Optional<NodeBean> node = Optional.empty();


    private Optional<EdgeBean> edge = Optional.empty();

    private List<NodeBean> neighbours;

    private Long applicationId;

    @Autowired
    private RoadFractoloniViewLayerManager mediaLayerManager;

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
        getLayerByNetworkAndType(mediaLayerManager, node.getNetwork(), GgmFirstNodeMediaLayer.class).get().setNode(node);
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
        getLayerByNetworkAndType(mediaLayerManager, node.getNetwork(), GgmNeighborNodeMediaLayer.class).get().setNodes(neighbours);
    }

    private void selectEdge(EdgeBean edge) {
        getLayerByNetworkAndType(mediaLayerManager, edge.getFromNode().getNetwork(), GgmFirstNodeMediaLayer.class).get().setNode(edge.getFromNode());
        getLayerByNetworkAndType(mediaLayerManager, edge.getToNode().getNetwork(), GgmSecondNodeMediaLayer.class).get().setEndNodeIndex(edge.getToNode().getAddressIndex());
        getLayerByNetworkAndType(mediaLayerManager, edge.getFromNode().getNetwork(), GgmPathLineMediaLayer.class).get().setEdge(edge);
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

    public void mapFormFieldsToCurrentState(NodeBean fromNode, NodeBean toNode, List<NodeBean> fractolonNodes, List<ClusterBean> excludeClusters, List<NodeBean> road) {
        if (getCurrentNetwork() != null) {
            currentState().setIncludeNodes(new ArrayList<>());
            currentState().setExcludeClusters(new ArrayList<>());
            currentState().setRoad(new ArrayList<>());

            if (fractolonNodes != null) {
                currentState().setIncludeNodes(fractolonNodes);
            }
            if (road != null) {
                currentState().getRoad().addAll(road);
            }
        }

        mapCurrentStateToMediaLayers();
    }

    public void mapCurrentStateToMediaLayers() {
        if (getCurrentNetwork() != null) {
            //reset and refresh just what is needed
            mediaLayerManager.getMediaRendererLayers().stream().forEach(layer -> layer.setNeedRefresh(false));

            //fractolon's nodes
            Optional<RoadFractolonNodesMediaLayer> optionalFractolonNodesLayer = getLayerByNetworkAndType(mediaLayerManager, getCurrentNetwork(), RoadFractolonNodesMediaLayer.class);
            if (optionalFractolonNodesLayer.isPresent()) {
                RoadFractolonNodesMediaLayer fractolonNodesLayer = optionalFractolonNodesLayer.get();
                if (fractolonNodesLayer.getSelectedIndex() != null && (
                        (fractolonNodesLayer.getSelectedIndex().size() != 0 && currentState().getIncludeNodes().size() == 0) ||
                                (fractolonNodesLayer.getSelectedIndex().size() == 0 && currentState().getIncludeNodes().size() != 0) ||
                                (fractolonNodesLayer.getSelectedIndex().size() != 0 && currentState().getIncludeNodes().size() != 0 &&
                                        !fractolonNodesLayer.getSelectedIndex().equals(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()))))) {
                    fractolonNodesLayer.setSelectedIndex(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()));
                    fractolonNodesLayer.setNeedRefresh(true);
                }
            }

            //fractolon's edges
            Optional<RoadFractolonEdgesMediaLayer> optionalFractolonEdgesLayer = getLayerByNetworkAndType(mediaLayerManager, getCurrentNetwork(), RoadFractolonEdgesMediaLayer.class);
            if (optionalFractolonEdgesLayer.isPresent()) {
                RoadFractolonEdgesMediaLayer fractolonEdgesLayer = optionalFractolonEdgesLayer.get();
                if (fractolonEdgesLayer.getSelectedNodes() != null && (
                        (fractolonEdgesLayer.getSelectedNodes().size() != 0 && currentState().getRoad().size() == 0) ||
                                (fractolonEdgesLayer.getSelectedNodes().size() == 0 && currentState().getRoad().size() != 0) ||
                                (fractolonEdgesLayer.getSelectedNodes().size() != 0 && currentState().getRoad().size() != 0 &&
                                        !fractolonEdgesLayer.getSelectedNodes().equals(currentState().getRoad().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()))))) {
                    fractolonEdgesLayer.setSelectedNodes(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()));
                    fractolonEdgesLayer.setNeedRefresh(true);
                }
            }

            //
            Optional<RoadPathLinesMediaLayer> optionalRoadLayer = getLayerByNetworkAndType(mediaLayerManager, getCurrentNetwork(), RoadPathLinesMediaLayer.class);
            if (optionalRoadLayer.isPresent()) {
                RoadPathLinesMediaLayer roadLayer = optionalRoadLayer.get();
                if (roadLayer.getSelectedNodes() != null && (
                        (roadLayer.getSelectedNodes().size() != 0 && currentState().getRoad().size() == 0) ||
                                (roadLayer.getSelectedNodes().size() == 0 && currentState().getRoad().size() != 0) ||
                                (roadLayer.getSelectedNodes().size() != 0 && currentState().getRoad().size() != 0 &&
                                        !roadLayer.getSelectedNodes().equals(currentState().getRoad().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()))))) {
                    roadLayer.setSelectedNodes(currentState().getRoad().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()));
                    roadLayer.setNeedRefresh(true);
                }
            }

        }
    }

    @Override
    public RoadFractoloniViewLayerManager getLayerManager() {
        return mediaLayerManager;
    }

    private void resetSelectionOnFirstNodeMediaLayer(String network) {
        GgmFirstNodeMediaLayer firstNodeMediaLayer = getLayerByNetworkAndType(mediaLayerManager, network, GgmFirstNodeMediaLayer.class).get();
        firstNodeMediaLayer.setNode(null);
        firstNodeMediaLayer.setNeedRefresh(true);
    }

    private void resetSelectionOnSecondNodeMediaLayer(String network) {
        GgmSecondNodeMediaLayer secondNodeMediaLayer = getLayerByNetworkAndType(mediaLayerManager, network, GgmSecondNodeMediaLayer.class).get();
        secondNodeMediaLayer.setEndNodeIndex(null);
        secondNodeMediaLayer.setNeedRefresh(true);
    }

    private void resetSelectionOnEdgeMediaLayer(String network) {
        GgmPathLineMediaLayer pathLineMediaLayer = getLayerByNetworkAndType(mediaLayerManager, network, GgmPathLineMediaLayer.class).get();
        pathLineMediaLayer.setEdge(null);
        pathLineMediaLayer.setNeedRefresh(true);
    }

    private void resetSelectionOnNodeNeighboursMediaLayer(String network) {
        GgmNeighborNodeMediaLayer neighborNodeMediaLayer = getLayerByNetworkAndType(mediaLayerManager, network, GgmNeighborNodeMediaLayer.class).get();
        neighborNodeMediaLayer.setNodes(null);
        neighborNodeMediaLayer.setNeedRefresh(true);
    }

    @Override
    public MarkedNodesMediaLayer getMarkedNodesMediaLayer(String network) {
        return getLayerByNetworkAndType(mediaLayerManager, network, MarkedNodesMediaLayer.class).get();
    }

    @Override
    public MarkedEdgesMediaLayer getMarkedEdgesMediaLayer(String network) {
        return getLayerByNetworkAndType(mediaLayerManager, network, MarkedEdgesMediaLayer.class).get();
    }

    @Override
    public String getCurrentNetwork() {
        return viewApplicationDataIe.getNetwork();
    }

    @Override
    public Long getCurrentApplicationId() {
        return applicationId;
    }

    public void setCurrentNetwork(String network) {
        viewApplicationDataIe.setNetwork(network);
    }

    public void setApplicationAndNetwork() {
        setCurrentNetwork(viewApplicationDataIe.getNetwork());
        Optional<Application> application = applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase(
                viewApplicationDataIe.getApplicationName()).stream().findAny();
        if (application.isPresent() == false) {
            LOGGER.error("No application with name {} was found", viewApplicationDataIe.getApplicationName());
            return;
        }
        applicationId = application.get().getId();
    }
}
