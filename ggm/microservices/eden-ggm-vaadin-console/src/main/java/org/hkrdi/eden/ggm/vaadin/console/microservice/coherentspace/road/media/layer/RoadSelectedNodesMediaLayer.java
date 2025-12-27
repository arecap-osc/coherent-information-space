package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.*;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.IndexSelectionMediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerName;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoadSelectedNodesMediaLayer implements IndexSelectionMediaRendererLayer, PNGMediaRendererDrawer, NetworkMediaLayerName, HasMediaRenderStyle, MediaRenderStyleAware {
	private String network;
	private List<Long> selectedIndex = new ArrayList<>();
	private Boolean needRefresh = false;
	private ConfigurableGraphics2dStyle graphics2dStyle;

	public RoadSelectedNodesMediaLayer() {
	}

	public RoadSelectedNodesMediaLayer(String network, ConfigurableGraphics2dStyle graphics2dStyle) {
		this.network = network;
		this.graphics2dStyle = graphics2dStyle;
	}

	@Override
	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@Override
	public List<Long> getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(List<Long> selectedIndex) {
		this.selectedIndex = selectedIndex;
	}


	@Override
	public Boolean isNeedRefresh() {
		return needRefresh;
	}

	@Override
	public void setNeedRefresh(Boolean needRefresh) {
		this.needRefresh = needRefresh;
	}

	@Override
	public void reset() {
		selectedIndex = new ArrayList<>();
	}

	@Override
	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
		List<NodeBean> nodeBeans = getSelectedIndex().stream().map(index->new NodeBean(network, index)).collect(Collectors.toList());

		Graphics2dStyle marginGraphics2dStyle = new Graphics2dStyle(ColorFactory.web("#666666"), graphics2dStyle.getOpacity(), graphics2dStyle.getPenStroke());
		CoherentSpaceGraphics2DUtils.drawPoints(graphics2d, nodeBeans, mediaRendererTransform, marginGraphics2dStyle, 2.2);

//		Graphics2dStyle fillGraphics2dStyle = new Graphics2dStyle(graphics2dStyle.getColor(), 0.1d, graphics2dStyle.getPenStroke());
		CoherentSpaceGraphics2DUtils.drawPoints(graphics2d, nodeBeans, mediaRendererTransform, graphics2dStyle , 1.8);
	}

	@Override
	public void setMediaRenderStyle(MediaRenderStyle mediaRenderStyle) {
		this.graphics2dStyle.setPenStroke(((ConfigurableGraphics2dStyle) mediaRenderStyle).getPenStroke());
		this.graphics2dStyle.setOpacity(((ConfigurableGraphics2dStyle) mediaRenderStyle).getOpacity());
	}

	@Override
	public ConfigurableGraphics2dStyle getMediaRenderStyle() {
		return graphics2dStyle;
	}
}
