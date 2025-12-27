package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;
import java.util.List;

public class GgmNeighborNodeMediaLayer extends GgmSelectionMediaRendererLayer {


	private List<NodeBean> nodes;


	public GgmNeighborNodeMediaLayer(String network, ConfigurableGraphics2dStyle mediaRenderStyle) {
		super(network, mediaRenderStyle);
	}

	public List<NodeBean> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeBean> nodes) {
		this.nodes = nodes;
	}

	@Override
	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
		if(nodes != null && nodes.size() > 0) {
			Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(ColorFactory.web("#666666"), getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
			CoherentSpaceGraphics2DUtils
					.drawPoints(graphics2d, nodes, mediaRendererTransform, mediaRendererStyle, 2.2);

			CoherentSpaceGraphics2DUtils
					.drawPoints(graphics2d, nodes, mediaRendererTransform, getMediaRenderStyle(), 1.8);
		}
	}

}
