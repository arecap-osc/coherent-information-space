package org.arecap.eden.ia.console.media;

import java.util.List;

public interface MediaLayer extends HasMediaRenderStyle {

    String getName();

    Boolean isVisible();

    void setVisible(Boolean visible);

    List<MediaRendererLayer> getMediaRendererLayers();

}
