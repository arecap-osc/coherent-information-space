package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import org.hkrdi.eden.ggm.vaadin.console.microservice.media.ConfigurableMediaLayerStyleView;

public abstract class NetworkMediaLayerStyleView extends ConfigurableMediaLayerStyleView {

    @Override
    public abstract NetworkMediaLayerStylePresenter getPresenter();

    public abstract NetworkMediaRendererLayerStyleView getConfigurableMediaRendererStyleLayerView();

}
