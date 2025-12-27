package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.util.ArrayList;
import java.util.List;

public class RoadStatePerNetwork{
    private NodeBean startNode;
    private NodeBean endNode;
    private List<NodeBean> includeNodes = new ArrayList<>();    
    private List<ClusterBean> excludeClusters = new ArrayList<>();
    private List<NodeBean> road = new ArrayList<>();
    private List<List<NodeBean>> roads = new ArrayList<>();
    
    private List<NodeBean> selectedNodes = new ArrayList<>();
    private List<EdgeBean> selectedEdges = new ArrayList<>();
    private List<NodeBean> selectedNeighborsNodes = new ArrayList<>();
    
	public NodeBean getStartNode() {
		return startNode;
	}

	public void setStartNode(NodeBean startNode) {
		this.startNode = startNode;
	}

	public NodeBean getEndNode() {
		return endNode;
	}

	public void setEndNode(NodeBean endNode) {
		this.endNode = endNode;
	}

	public List<NodeBean> getIncludeNodes() {
		return includeNodes;
	}

	public void setIncludeNodes(List<NodeBean> includeNodes) {
		this.includeNodes = includeNodes;
	}

	public List<ClusterBean> getExcludeClusters() {
		return excludeClusters;
	}

	public void setExcludeClusters(List<ClusterBean> excludeClusters) {
		this.excludeClusters = excludeClusters;
	}

	public List<NodeBean> getRoad() {
		return road;
	}

	public void setRoad(List<NodeBean> road) {
		this.road = road;
	}

	public List<NodeBean> getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(List<NodeBean> selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public List<List<NodeBean>> getRoads() {
		return roads;
	}
	
	public void setRoads(List<List<NodeBean>> roads) {
		this.roads = roads;
	}

	public List<EdgeBean> getSelectedEdges() {
		return selectedEdges;
	}

	public void setSelectedEdges(List<EdgeBean> selectedEdges) {
		this.selectedEdges = selectedEdges;
	}

	public List<NodeBean> getSelectedNeighborsNodes() {
		return selectedNeighborsNodes;
	}

	public void setSelectedNeighborsNodes(List<NodeBean> selectedNeighborsNodes) {
		this.selectedNeighborsNodes = selectedNeighborsNodes;
	}
}