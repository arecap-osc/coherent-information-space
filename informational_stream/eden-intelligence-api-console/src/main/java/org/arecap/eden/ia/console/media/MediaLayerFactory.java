package org.arecap.eden.ia.console.media;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MediaLayerFactory {

    List<MediaLayer> getMediaLayers();

    default List<String> getMediaLayersNames() {
        return getMediaLayers().stream().map(o -> o.getName()).collect(Collectors.toList());
    }

    default Optional<MediaLayer> getMediaLayerByName(String layerName) {
        return getMediaLayers().stream().filter(layer -> layer.getName().equalsIgnoreCase(layerName)).findAny();
    }

    default double getNetworkPenStroke(String network) {
        return getMediaLayerByName(network)
                .map(mediaLayer -> (ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle())
                .get().getPenStroke();
    }

//    default Boolean isVisible(String name) {
//        Optional<MediaLayer> mediaLayer = getMediaLayer(name);
//        return mediaLayer.isPresent() && mediaLayer.get().isVisible();
//    }
//
//    default List<MediaRendererLayer> getMediaRendererLayers(String name){
//        Optional<MediaLayer> mediaLayer = getMediaLayer(name);
//        return mediaLayer.isPresent() ? mediaLayer.get().getMediaRendererLayers() : new ArrayList<>();
//    }



}
