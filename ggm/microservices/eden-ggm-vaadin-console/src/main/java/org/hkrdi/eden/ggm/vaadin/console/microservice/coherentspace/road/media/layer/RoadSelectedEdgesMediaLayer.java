package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.*;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerName;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class RoadSelectedEdgesMediaLayer implements PNGMediaRendererDrawer, NetworkMediaLayerName, HasMediaRenderStyle, MediaRenderStyleAware {
	private String network;
	private Boolean needRefresh = false;
	private List<Long> selectedNodes = new ArrayList<>();
	private ConfigurableGraphics2dStyle graphics2dStyle;

	public RoadSelectedEdgesMediaLayer() {
	}

	public RoadSelectedEdgesMediaLayer(String network, Graphics2dStyle graphics2dStyle) {
		this.network = network;
		this.graphics2dStyle = graphics2dStyle;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@Override
	public Boolean isNeedRefresh() {
		return needRefresh;
	}
	
	@Override
	public void setNeedRefresh(Boolean needRefresh) {
		this.needRefresh = needRefresh;
	}
	
	public List<Long> getSelectedNodes() {
		return selectedNodes;
	}
	
	public void setSelectedNodes(List<Long> selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
	
	@Override
	public void reset() {
		selectedNodes = new ArrayList<>();
	}

	@Override
	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mrt) {
		List<EdgeBean> edgeBeans = new ArrayList<>();
		for (int i=0; i<selectedNodes.size()-1; i++) {
			edgeBeans.add(new EdgeBean(new NodeBean(network, selectedNodes.get(i)), new NodeBean(network, selectedNodes.get(i+1))));
		}
		
		CoherentSpaceGraphics2DUtils.drawLines(graphics2d, edgeBeans, mrt, graphics2dStyle);
	}

	@Override
	public void setMediaRenderStyle(MediaRenderStyle mediaRenderStyle) {
		this.graphics2dStyle.setPenStroke(((ConfigurableGraphics2dStyle) mediaRenderStyle).getPenStroke());
		this.graphics2dStyle.setOpacity(((ConfigurableGraphics2dStyle) mediaRenderStyle).getOpacity());
	}

	@Override
	public MediaRenderStyle getMediaRenderStyle() {
		return graphics2dStyle;
	}
}
