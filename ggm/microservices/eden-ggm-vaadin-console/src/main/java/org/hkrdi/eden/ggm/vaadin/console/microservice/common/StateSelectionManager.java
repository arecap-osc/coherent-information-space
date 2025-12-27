package org.hkrdi.eden.ggm.vaadin.console.microservice.common;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated.RoadCalculatedRoadLayerManager;

import java.util.Optional;

public interface StateSelectionManager {
	
	default <T  extends MediaRendererLayer> Optional<T> getLayerByNetworkAndType(MediaRenderLayerFactory layerManager, String network, Class<T> clazz) {
        return (Optional<T>) layerManager.getMediaRendererLayers().stream()
        		.filter(layer -> layer.getClass().equals(clazz))
                .filter(layer -> ((NetworkMediaLayer) layer).getNetwork().equals(network))
                .findFirst();
    }

    default <T  extends MediaRendererLayer> Optional<T> getLayerByType(MediaRenderLayerFactory layerManager, Class<T> clazz) {
        return (Optional<T>) layerManager.getMediaRendererLayers().stream().filter(layer -> layer.getClass().equals(clazz))
                .findFirst();
    }

    String getCurrentNetwork();

    Long getCurrentApplicationId();
}
