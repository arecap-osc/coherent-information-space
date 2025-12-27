package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1RoadClearSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1RoadSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1ToNode2RoadClearSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1ToNode2RoadSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node2RoadClearSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node2RoadSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadSelectedEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadSelectedNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.MarkedSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.RoadSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

public abstract class RoadStateSelectionManager implements StateSelectionManager, RoadSelectionProcessor {
    @Autowired
    private GgmRouteApplicationDataIe appDataIe;

    @Autowired
    private UIEventBus uIEventBus;

    private List<List<NodeBean>> roads = new ArrayList<>();

    private Map<String, RoadStatePerNetwork> states = new HashMap<>(); //network, state

    @Override
    public MarkedNodesMediaLayer getMarkedNodesMediaLayer(String network) {
        return getLayerByNetworkAndType(getLayerManager(), network, MarkedNodesMediaLayer.class).get();
    }

    @Override
    public MarkedEdgesMediaLayer getMarkedEdgesMediaLayer(String network) {
        return getLayerByNetworkAndType(getLayerManager(), network, MarkedEdgesMediaLayer.class).get();
    }

    public void clearSelectionRoadLayersForCurrentNetwork() {
        currentState().setSelectedEdges(new ArrayList<>());
        currentState().setSelectedNodes(new ArrayList<>());
        currentState().setSelectedNeighborsNodes(new ArrayList<>());

//		layerManager.getMediaRendererLayers().stream()
//			.filter(l-> l instanceof NetworkMediaLayer)
//			.filter(l -> ((NetworkMediaLayer)l).getNetwork().equals(getCurrentNetwork()))
//			.forEach(l-> {
//				l.reset();
//				l.setNeedRefresh(true);
//			});
    }

    public void clearAllRoadLayersForCurrentNetwork() {
        currentState().setRoad(new ArrayList<>());
        currentState().setSelectedEdges(new ArrayList<>());
        currentState().setSelectedNodes(new ArrayList<>());
        currentState().setSelectedNeighborsNodes(new ArrayList<>());
        currentState().setIncludeNodes(new ArrayList<>());
        currentState().setExcludeClusters(new ArrayList<>());
        currentState().setStartNode(null);
        currentState().setEndNode(null);

        getLayerManager().getMediaRendererLayers().stream()
                .filter(l -> l instanceof NetworkMediaLayer)
                .filter(l -> ((NetworkMediaLayer) l).getNetwork().equals(getCurrentNetwork()))
                .forEach(l -> {
                    l.reset();
                    l.setNeedRefresh(true);
                });
    }

    @Override
    public void processNodeOrEdgeSelection(NodeBean clickedNode, EdgeBean clickedEdge) {
        boolean isNodeSelected = false;
        boolean isEdgeSelected = false;

        //reset and refresh just what is needed
        getLayerManager().getMediaRendererLayers().stream().forEach(layer -> layer.setNeedRefresh(false));

        if (clickedNode != null) {
            isNodeSelected = true;
        }
        if (clickedEdge != null && clickedNode == null) {
            isEdgeSelected = true;
        }

        //edge selected
        if (isEdgeSelected) {
            //deselect same edge
            if (currentState().getSelectedEdges().size() == 1 &&
                    currentState().getSelectedEdges().get(0).equals(clickedEdge)) {
                currentState().getSelectedEdges().clear();
                currentState().getSelectedNodes().clear();
            } else {
                currentState().getSelectedEdges().clear();
                currentState().getSelectedEdges().add(clickedEdge);

                currentState().getSelectedNodes().clear();
                currentState().getSelectedNodes().add(clickedEdge.getFromNode());
                currentState().getSelectedNodes().add(clickedEdge.getToNode());
            }
        } else {
            currentState().getSelectedEdges().clear();
        }

        //node selected
        if (isNodeSelected) {
            //deselect current node
            if (currentState().getSelectedNodes().size() == 1 &&
                    currentState().getSelectedNodes().get(0).equals(clickedNode)) {
                currentState().getSelectedNodes().clear();
            } else {
                currentState().getSelectedNodes().clear();
                currentState().getSelectedNodes().add(clickedNode);
            }
        }

        //paint edge
//        	Notification.show("Edge selected: "+getgetLayerManager()().getCurrentNetwork()+" from "+clickedEdge.getFromNodeTextField().getAddressIndex()+"->"+clickedEdge.getToNodeTextField().getAddressIndex());
        //paint selection
        RoadSelectedEdgesMediaLayer selectionEdgesLayer = getLayerByNetworkAndType(getLayerManager(), getCurrentNetwork(), RoadSelectedEdgesMediaLayer.class).get();
        selectionEdgesLayer.getSelectedNodes().clear();
        selectionEdgesLayer.setNeedRefresh(true);

        if (currentState().getSelectedEdges() != null && !currentState().getSelectedEdges().isEmpty()) {
            selectionEdgesLayer.getSelectedNodes().add(currentState().getSelectedEdges().get(0).getFromNode().getAddressIndex());
            selectionEdgesLayer.getSelectedNodes().add(currentState().getSelectedEdges().get(0).getToNode().getAddressIndex());

            uIEventBus.publish(this, new Node1ToNode2RoadSelectedEvent(
                    new EdgeBean(currentState().getSelectedEdges().get(0).getFromNode(), currentState().getSelectedEdges().get(0).getToNode())
            ));
        } else {
            uIEventBus.publish(this, new Node1ToNode2RoadClearSelectedEvent(new Object()));
        }
        //

        //paint node selection
        RoadSelectedNodesMediaLayer selectionNodeLayer = getLayerByNetworkAndType(getLayerManager(), getCurrentNetwork(), RoadSelectedNodesMediaLayer.class).get();
        selectionNodeLayer.getSelectedIndex().clear();
        selectionNodeLayer.setNeedRefresh(true);

        if (currentState().getSelectedNodes() != null && !currentState().getSelectedNodes().isEmpty()) {
            selectionNodeLayer.getSelectedIndex()
                    .addAll(currentState().getSelectedNodes().stream().map(nb -> nb.getAddressIndex()).collect(Collectors.toList()));
        }

        if (currentState().getSelectedNodes().size() == 2) {
            uIEventBus.publish(this, new Node1RoadSelectedEvent(new NodeBean(currentState().getSelectedNodes().get(0).getNetwork(), currentState().getSelectedNodes().get(0).getAddressIndex())));
            uIEventBus.publish(this, new Node2RoadSelectedEvent(new NodeBean(currentState().getSelectedNodes().get(1).getNetwork(), currentState().getSelectedNodes().get(1).getAddressIndex())));
        } else if (currentState().getSelectedNodes().size() == 1) {
            uIEventBus.publish(this, new Node1RoadSelectedEvent(new NodeBean(currentState().getSelectedNodes().get(0).getNetwork(), currentState().getSelectedNodes().get(0).getAddressIndex())));
            uIEventBus.publish(this, new Node2RoadClearSelectedEvent(new Object()));
        } else {
            uIEventBus.publish(this, new Node1RoadClearSelectedEvent(new Object()));
            uIEventBus.publish(this, new Node2RoadClearSelectedEvent(new Object()));
        }
        //
    }

    public abstract void mapFormFieldsToCurrentState(NodeBean fromNode, NodeBean toNode, List<NodeBean> includeNodes, List<ClusterBean> excludeClusters, List<NodeBean> road);

    public abstract void mapCurrentStateToMediaLayers();

    public abstract RoadLayerManager getLayerManager();

    public RoadStatePerNetwork currentState() {
        if (states.get(getCurrentNetwork()) == null) {
            states.put(getCurrentNetwork(), new RoadStatePerNetwork());
        }
        return states.get(getCurrentNetwork());
    }

    public List<List<NodeBean>> getRoads() {
        return roads;
    }

    public void setRoads(List<List<NodeBean>> roads) {
        this.roads = roads;
    }

    @Override
    public Long getCurrentApplicationId() {
        return appDataIe.getApplicationId();
    }

    @Override
    public String getCurrentNetwork() {
        return appDataIe.getNetwork();
    }

}