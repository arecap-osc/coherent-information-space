package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.ConfigurableMediaRendererLayerStylePresenter;

public abstract class NetworkMediaRendererLayerStylePresenter extends ConfigurableMediaRendererLayerStylePresenter {

    @Override
    public NetworkMediaRendererLayerStyleView getView() {
        return (NetworkMediaRendererLayerStyleView) super.getView();
    }

    @Override
    public void setMediaLayer(MediaLayer mediaLayer) {
        super.setMediaLayer(mediaLayer);
        NetworkMediaLayerStyle configuration = (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle();
        getView().getAddressIndexCheckBox().setValue(configuration.isAddressIndexVisible());
        getView().getTrivalentLogicCheckBox().setValue(configuration.isTrivalentLogicVisible());
        getView().getClusterIndexCheckBox().setValue(configuration.isClusterIndexVisible());
        getView().getApplicationDataCheckBox().setValue(configuration.isApplicationDataVisible());
    }

    public void handleAddressIndexValueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
        ((NetworkMediaLayerStyle)getMediaLayer().getMediaRenderStyle())
                .setAddressIndexVisible(event.getValue());
        refreshMediaRendererLayers();
    }

    public void handleTrivalentLogicValueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
        ((NetworkMediaLayerStyle)getMediaLayer().getMediaRenderStyle())
                .setTrivalentLogicVisible(event.getValue());
        refreshMediaRendererLayers();
    }

    public void handleClusterIndexValueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
        ((NetworkMediaLayerStyle)getMediaLayer().getMediaRenderStyle())
                .setClusterIndexVisible(event.getValue());
        refreshMediaRendererLayers();
    }

    public void handleApplicationDataValueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
        ((NetworkMediaLayerStyle)getMediaLayer().getMediaRenderStyle())
                .setApplicationDataVisible(event.getValue());
        refreshMediaRendererLayers();
    }

}
