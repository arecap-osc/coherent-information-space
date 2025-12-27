package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;

import java.awt.*;
import java.util.Optional;

public class GgmPathLineMediaLayer extends GgmSelectionMediaRendererLayer {

	private Optional<EdgeBean> edge = Optional.empty();

	public GgmPathLineMediaLayer(String network, ConfigurableGraphics2dStyle graphics2dStyle) {
		super(network, graphics2dStyle);
	}

	public EdgeBean getEdge() {
		return edge.isPresent() ? edge.get() : null;
	}

	public void setEdge(EdgeBean edge) {
		this.edge = Optional.ofNullable(edge);
	}

	@Override
	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
		if(edge.isPresent()) {
			CoherentSpaceGraphics2DUtils
					.drawLine(graphics2d, edge.get(), mediaRendererTransform, getMediaRenderStyle());
		}
	}


}
