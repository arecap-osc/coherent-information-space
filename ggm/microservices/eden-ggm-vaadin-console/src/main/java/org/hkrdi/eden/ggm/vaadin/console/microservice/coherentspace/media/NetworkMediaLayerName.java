package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;

public interface NetworkMediaLayerName extends NetworkMediaLayer, MediaRendererLayer {
	default String getName() {
		return getNetwork() + "_IMG_" + this;
	}
}
