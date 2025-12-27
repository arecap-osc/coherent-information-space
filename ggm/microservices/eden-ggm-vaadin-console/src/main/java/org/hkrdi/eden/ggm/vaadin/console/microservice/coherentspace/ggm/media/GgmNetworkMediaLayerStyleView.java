package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStylePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaRendererLayerStyleView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmNetworkMediaLayerStyleView extends NetworkMediaLayerStyleView {

    @Autowired
    private GgmNetworkMediaLayerStylePresenter presenter;

    @Autowired
    private GgmNetworkMediaRendererLayerStyleView mediaRendererLayerStyleView;

    @Override
    public NetworkMediaLayerStylePresenter getPresenter() {
        return presenter;
    }

    @Override
    public NetworkMediaRendererLayerStyleView getConfigurableMediaRendererStyleLayerView() {
        return mediaRendererLayerStyleView;
    }

}
