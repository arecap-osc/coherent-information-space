package org.arecap.eden.ia.console.media;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MediaRenderLayerFactory {
	List<MediaRendererLayer> getMediaRendererLayers();
	
	default List<String> getMediaRendererLayersNames() {
		return getMediaRendererLayers().stream().map(o -> o.getName()).collect(Collectors.toList());
	}
	
	default Optional<MediaRendererLayer> getMediaRendererLayer(String layerName) {
		return getMediaRendererLayers().stream().filter(layer -> layer.getName().equalsIgnoreCase(layerName)).findAny();
	}
}
