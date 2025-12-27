package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaRendererLayerStylePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaRendererLayerStyleView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmNetworkMediaRendererLayerStyleView extends NetworkMediaRendererLayerStyleView {

    @Autowired
    private GgmNetworkMediaRendererLayerStylePresenter presenter;

    @Override
    public NetworkMediaRendererLayerStylePresenter getPresenter() {
        return presenter;
    }

}
