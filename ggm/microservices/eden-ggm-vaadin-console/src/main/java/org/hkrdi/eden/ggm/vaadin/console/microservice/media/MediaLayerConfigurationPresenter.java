package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;

import com.vaadin.flow.component.html.Image;

/**
 * Component used to configure layers order, color, etc... 
 * MediaRendererLayer Manager.
 * Sort Layers. Cache static layers (for background)
 * 
 * @author abo
 *
 */
public abstract class MediaLayerConfigurationPresenter extends DefaultFlowPresenter<Image, String> implements MediaRenderLayerFactory {
}
