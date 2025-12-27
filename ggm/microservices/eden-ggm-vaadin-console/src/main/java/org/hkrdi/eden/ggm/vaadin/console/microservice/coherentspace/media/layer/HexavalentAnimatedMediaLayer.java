package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.*;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerName;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.cop.support.BeanUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HexavalentAnimatedMediaLayer implements GIFMediaRendererDrawer, NetworkMediaLayerName, MediaRenderStyleAware {

	private String network;
	private Boolean needRefresh = false;
	private List<EdgeBean> outerEdges = new ArrayList<>();
	private List<NodeBean> nodes = new ArrayList<>();
	private List<ClusterBean> clusters = new ArrayList<>();

	private ConfigurableGraphics2dStyle graphics2dStyle;

	public HexavalentAnimatedMediaLayer() {
		super();
	}
	
	public HexavalentAnimatedMediaLayer(String network) {
		super();
		setNetwork(network);	
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
		
		outerEdges.addAll(BeanUtil.getBean(CoherentSpaceService.class).findNetworkOuterEdges(network));
		nodes.addAll(BeanUtil.getBean(CoherentSpaceService.class).getNetworkNodes(network));
		clusters.addAll(BeanUtil.getBean(CoherentSpaceService.class).getNetworkClusters(network));

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
	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mrt, Integer frameNo) {
		Graphics2dStyle lightGray = new Graphics2dStyle(ColorFactory.LIGHTGRAY, graphics2dStyle.getPenStroke());
		CoherentSpaceGraphics2DUtils.drawLines(graphics2d, outerEdges, mrt, lightGray, frameParts(frameNo));
	}

	@Override
	public void setMediaRenderStyle(MediaRenderStyle mediaRenderStyle) {
		graphics2dStyle = (ConfigurableGraphics2dStyle) mediaRenderStyle;
	}

}
