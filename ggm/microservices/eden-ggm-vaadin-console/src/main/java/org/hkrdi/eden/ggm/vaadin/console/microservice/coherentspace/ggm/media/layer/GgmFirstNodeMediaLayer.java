package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;

public class GgmFirstNodeMediaLayer extends GgmSelectionMediaRendererLayer {

	private NodeBean node;

	public GgmFirstNodeMediaLayer(String network, ConfigurableGraphics2dStyle mediaRendererStyle) {
		super(network, mediaRendererStyle);
	}

    public NodeBean getNode() {
        return node;
    }

    public void setNode(NodeBean node) {
        this.node = node;
    }

	@Override
	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
		if (node!=null) {
			Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(ColorFactory.web("#666666"), getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
			CoherentSpaceGraphics2DUtils
					.drawPoint(graphics2d, new NodeBean(getNetwork(), node.getAddressIndex()),
							mediaRendererTransform, mediaRendererStyle, 2.2);

			CoherentSpaceGraphics2DUtils
					.drawPoint(graphics2d, new NodeBean(getNetwork(), node.getAddressIndex()),
							mediaRendererTransform, getMediaRenderStyle(), 1.8);
		}
	}

}
