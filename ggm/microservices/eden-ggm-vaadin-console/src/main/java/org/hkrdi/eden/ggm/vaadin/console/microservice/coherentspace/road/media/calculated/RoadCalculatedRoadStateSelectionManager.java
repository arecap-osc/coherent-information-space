package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadEndNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadIncludeNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadPathLinesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.RoadStartNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadCalculatedRoadStateSelectionManager extends RoadStateSelectionManager{
	@Autowired
	private RoadCalculatedRoadLayerManager layerManager;
	
	public void mapFormFieldsToCurrentState(NodeBean fromNode, NodeBean toNode, List<NodeBean> includeNodes, List<ClusterBean> excludeClusters, List<NodeBean> road) {
    	if (getCurrentNetwork()!=null) {
	    	currentState().setStartNode(null);
	    	currentState().setEndNode(null);
	    	currentState().setIncludeNodes(new ArrayList<>());
	    	currentState().setExcludeClusters(new ArrayList<>());
	    	currentState().setRoad(new ArrayList<>());
	    	
	    	if(fromNode!=null) {
	    		currentState().setStartNode(fromNode);
	    	}
	
	    	if(toNode!=null) {
	    		currentState().setEndNode(toNode);
	    	}
	    	
	    	if (includeNodes!=null) {
	    		currentState().getIncludeNodes().addAll(includeNodes);
	    	}
	    	
	    	if (excludeClusters!=null) {
	    		currentState().getExcludeClusters().addAll(excludeClusters);
	    	}
	    	
	    	if (road!=null) {
	    		currentState().getRoad().addAll(road);
	    	}
    	}
    	
    	mapCurrentStateToMediaLayers();
	}
	
	public void mapCurrentStateToMediaLayers() {
		if (getCurrentNetwork()!=null) {
			//reset and refresh just what is needed
	    	layerManager.getMediaRendererLayers().stream().forEach(layer -> layer.setNeedRefresh(false));
	    	
	    	Optional<RoadStartNodeMediaLayer> optionalStartLayer = getLayerByNetworkAndType(layerManager, getCurrentNetwork(), RoadStartNodeMediaLayer.class);
	    	if (optionalStartLayer.isPresent()) {
				RoadStartNodeMediaLayer startLayer = optionalStartLayer.get();
				if (startLayer.getSelectedIndex()!=null && (
						(startLayer.getSelectedIndex().size() == 0 && currentState().getStartNode() != null) || 
						(startLayer.getSelectedIndex().size() == 1 && currentState().getStartNode() == null) ||
						(startLayer.getSelectedIndex().size() == 1 && startLayer.getSelectedIndex().get(0) == null) ||
						(startLayer.getSelectedIndex().size() == 1 && !startLayer.getSelectedIndex().get(0).equals(currentState().getStartNode().getAddressIndex()))
															)){
					if (currentState().getStartNode() != null){
						startLayer.setSelectedIndex(Arrays.asList(currentState().getStartNode().getAddressIndex()));
					}else {
						startLayer.setSelectedIndex(new ArrayList<>());
					}
					startLayer.setNeedRefresh(true);
				}
	    	}
	    	
			//
	    	Optional<RoadEndNodeMediaLayer> optionalEndLayer = getLayerByNetworkAndType(layerManager, getCurrentNetwork(), RoadEndNodeMediaLayer.class);
	    	if (optionalEndLayer.isPresent()) {
		    	RoadEndNodeMediaLayer endLayer = optionalEndLayer.get();
		    	if (endLayer.getSelectedIndex()!=null && (
						(endLayer.getSelectedIndex().size() == 0 && currentState().getEndNode() != null) || 
						(endLayer.getSelectedIndex().size() == 1 && currentState().getEndNode() == null) ||
						(endLayer.getSelectedIndex().size() == 1 && endLayer.getSelectedIndex().get(0) == null) ||
						(endLayer.getSelectedIndex().size() == 1 && !endLayer.getSelectedIndex().get(0).equals(currentState().getEndNode().getAddressIndex()))
															)){
					if (currentState().getEndNode() != null){
						endLayer.setSelectedIndex(Arrays.asList(currentState().getEndNode().getAddressIndex()));
					}else {
						endLayer.setSelectedIndex(new ArrayList<>());
					}
					endLayer.setNeedRefresh(true);
				}
	    	}
	    	
	    	//
	    	Optional<RoadIncludeNodesMediaLayer> optionalIncludeLayer = getLayerByNetworkAndType(layerManager, getCurrentNetwork(), RoadIncludeNodesMediaLayer.class);
	    	if (optionalIncludeLayer.isPresent()) {
	    		RoadIncludeNodesMediaLayer includeLayer = optionalIncludeLayer.get();
		    	if (includeLayer.getSelectedIndex()!=null && (
		    			(includeLayer.getSelectedIndex().size() != 0 && currentState().getIncludeNodes().size() == 0) ||
		    			(includeLayer.getSelectedIndex().size() == 0 && currentState().getIncludeNodes().size() != 0) ||
		    			(includeLayer.getSelectedIndex().size() != 0 && currentState().getIncludeNodes().size() != 0 && 
		    				!includeLayer.getSelectedIndex().equals(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()))))) {
		    			includeLayer.setSelectedIndex(currentState().getIncludeNodes().stream().map(node -> node.getAddressIndex()).collect(Collectors.toList()));
		    		includeLayer.setNeedRefresh(true);
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

	@Override
	public RoadCalculatedRoadLayerManager getLayerManager() {
		return layerManager;
	}
	
	public void setLayerManager(RoadCalculatedRoadLayerManager layerManager) {
		this.layerManager = layerManager;
	}
}