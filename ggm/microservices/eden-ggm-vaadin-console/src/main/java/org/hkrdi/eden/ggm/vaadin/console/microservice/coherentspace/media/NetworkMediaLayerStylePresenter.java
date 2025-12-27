package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import org.hkrdi.eden.ggm.vaadin.console.microservice.media.ConfigurableMediaLayerStylePresenter;

public abstract class NetworkMediaLayerStylePresenter extends ConfigurableMediaLayerStylePresenter {

    @Override
    public NetworkMediaLayerStyleView getView() {
        return (NetworkMediaLayerStyleView) super.getView();
    }

}
