package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.fractoloni;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadFractolonEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadFractolonNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadPathLinesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadFractoloniRoadStateSelectionManager extends RoadStateSelectionManager{
	@Autowired
	private RoadFractoloniRoadLayerManager layerManager;
	
	@Autowired
    private CoherentSpaceService coherentSpaceService;
	
	public void mapFormFieldsToCurrentState(NodeBean fromNode, NodeBean toNode, List<NodeBean> fractolonNodes, List<ClusterBean> excludeClusters, List<NodeBean> road) {
    	if (getCurrentNetwork()!=null) {
	    	currentState().setIncludeNodes(new ArrayList<>());
	    	currentState().setExcludeClusters(new ArrayList<>());
	    	currentState().setRoad(new ArrayList<>());
	    	
	    	if (fractolonNodes!=null) {
		    	currentState().setIncludeNodes(fractolonNodes);
	    	}    	
	    	if (road != null) {
	    		currentState().getRoad().addAll(road);
	    	}

	    	//	    	if (excludeClusters!=null) {
//	    		currentState().getExcludeClusters().addAll(excludeClusters);
//	    	}
//	    	
//	    	if (road!=null) {
//	    		currentState().getRoad().addAll(road);
//	    	}
    	}
    	
    	mapCurrentStateToMediaLayers();
	}
	
	public void mapCurrentStateToMediaLayers() {
		if (getCurrentNetwork()!=null) {
			//reset and refresh just what is needed
	    	layerManager.getMediaRendererLayers().stream().forEach(layer -> layer.setNeedRefresh(false));
	    	
	    	
	    	//fractolon's nodes
	    	Optional<RoadFractolonNodesMediaLayer> optionalFractolonNodesLayer = getLayerByNetworkAndType(layerManager, getCurrentNetwork(), RoadFractolonNodesMediaLayer.class);
	    	if (optionalFractolonNodesLayer.isPresent()) {
	    		RoadFractolonNodesMediaLayer fractolonNodesLayer = optionalFractolonNodesLayer.get();
		    	if (fractolonNodesLayer.getSelectedIndex()!=null && (
		    			(fractolonNodesLayer.getSelectedIndex().size() != 0 && currentState().getIncludeNodes().size() == 0) ||
		    			(fractolonNodesLayer.getSelectedIndex().size() == 0 && currentState().getIncludeNodes().size() != 0) ||
		    			(fractolonNodesLayer.getSelectedIndex().size() != 0 && currentState().getIncludeNodes().size() != 0 && 
		    				!fractolonNodesLayer.getSelectedIndex().equals(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()))))) {
		    			fractolonNodesLayer.setSelectedIndex(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()));
		    		fractolonNodesLayer.setNeedRefresh(true);
		    	}
			}
	    	
	    	//fractolon's edges
	    	Optional<RoadFractolonEdgesMediaLayer> optionalFractolonEdgesLayer = getLayerByNetworkAndType(layerManager, getCurrentNetwork(), RoadFractolonEdgesMediaLayer.class);
	    	if (optionalFractolonEdgesLayer.isPresent()) {
	    		RoadFractolonEdgesMediaLayer fractolonEdgesLayer = optionalFractolonEdgesLayer.get();
		    	if (fractolonEdgesLayer.getSelectedNodes()!=null && (
		    			(fractolonEdgesLayer.getSelectedNodes().size() != 0 && currentState().getRoad().size() == 0) ||
		    			(fractolonEdgesLayer.getSelectedNodes().size() == 0 && currentState().getRoad().size() != 0) ||
		    			(fractolonEdgesLayer.getSelectedNodes().size() != 0 && currentState().getRoad().size() != 0 && 
		    				!fractolonEdgesLayer.getSelectedNodes().equals(currentState().getRoad().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()))))) {
		    		fractolonEdgesLayer.setSelectedNodes(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()));
		    		fractolonEdgesLayer.setNeedRefresh(true);
		    	}
			}
	    	
	    	//
	    	Optional<RoadPathLinesMediaLayer> optionalRoadLayer = getLayerByNetworkAndType(layerManager, getCurrentNetwork(), RoadPathLinesMediaLayer.class);
	    	if (optionalRoadLayer.isPresent()) {
	    		RoadPathLinesMediaLayer roadLayer = optionalRoadLayer.get();
		    	if (roadLayer.getSelectedNodes()!=null && (
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
	
//	@Override
//	public void mapFormFieldsToCurrentState(NodeBean fromNode, NodeBean toNode, List<NodeBean> includeNodes, List<ClusterBean> excludeClusters, List<NodeBean> road) {
//    	if (getCurrentNetwork()!=null) {
//	    	currentState().setIncludeNodes(new ArrayList<>());
//	    	currentState().setRoad(new ArrayList<>());
//	    	
//	    	if (includeNodes!=null) {
//	    		currentState().getIncludeNodes().addAll(includeNodes);
//	    	}
//	    	
//	    	if (road!=null) {
//	    		currentState().getRoad().addAll(road);
//	    	}
//    	}
//    	
//    	mapCurrentStateToMediaLayers();
//	}

	@Override
	public RoadFractoloniRoadLayerManager getLayerManager() {
		return layerManager;
	}
	
	public void setLayerManager(RoadFractoloniRoadLayerManager layerManager) {
		this.layerManager = layerManager;
	}
}