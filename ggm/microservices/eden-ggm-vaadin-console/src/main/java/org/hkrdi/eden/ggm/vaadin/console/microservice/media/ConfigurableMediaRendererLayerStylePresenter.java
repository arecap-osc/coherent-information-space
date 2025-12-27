package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerAware;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderStyleAware;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.html.Input;

public abstract class ConfigurableMediaRendererLayerStylePresenter extends DefaultFlowPresenter implements MediaLayerAware {

    private MediaLayer mediaLayer;

    private Boolean shouldRefresh = false;

    @Override
    public ConfigurableMediaRendererLayerStyleView getView() {
        return (ConfigurableMediaRendererLayerStyleView)super.getView();
    }

    public MediaLayer getMediaLayer() {
        return mediaLayer;
    }

    @Override
    public void setMediaLayer(MediaLayer mediaLayer) {
        this.mediaLayer = mediaLayer;
        shouldRefresh = false;
        ConfigurableGraphics2dStyle configuration = (ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle();
        getView().getColorInput().setValue(configuration.getAlphaColorAsStr());
        getView().getPenInput().setValue(String.valueOf(configuration.getPenStroke()));
        getView().getOpacityInput().setValue(String.valueOf(configuration.getOpacity()));
    }

    public void activateValueChangeListeners() {
        this.shouldRefresh = true;
    }

    public void handleColorValueChanged(AbstractField.ComponentValueChangeEvent<Input, String> event) {
        ((ConfigurableGraphics2dStyle)mediaLayer.getMediaRenderStyle())
                .setColor(ColorFactory.web(event.getValue()));
        if(shouldRefresh) {
            getView().getConfigurableMediaLayerStyleView().getPresenter().prepareModel(null);
        }
        refreshMediaRendererLayers();
    }

    public void handlePenValueChanged(AbstractField.ComponentValueChangeEvent<Input, String> event) {
        ((ConfigurableGraphics2dStyle)mediaLayer.getMediaRenderStyle())
                .setPenStroke(Double.parseDouble(event.getValue()));
        refreshMediaRendererLayers();

    }

    public void handleOpacityValueChanged(AbstractField.ComponentValueChangeEvent<Input, String> event) {
        ((ConfigurableGraphics2dStyle)mediaLayer.getMediaRenderStyle())
                .setOpacity(Double.parseDouble(event.getValue()));
        refreshMediaRendererLayers();
    }

    protected void refreshMediaRendererLayers() {
        if(shouldRefresh) {
            mediaLayer.getMediaRendererLayers().stream()
                    .forEach(this::refreshMediaRendererLayer);
            notifyMultiImageView();
        }
    }

    private void refreshMediaRendererLayer(MediaRendererLayer mediaRendererLayer) {
        if(MediaRenderStyleAware.class.isAssignableFrom(mediaRendererLayer.getClass())) {
            ((MediaRenderStyleAware) mediaRendererLayer).setMediaRenderStyle(mediaLayer.getMediaRenderStyle());
            mediaRendererLayer.setNeedRefresh(true);
        }
    }

    protected abstract void notifyMultiImageView();

}
