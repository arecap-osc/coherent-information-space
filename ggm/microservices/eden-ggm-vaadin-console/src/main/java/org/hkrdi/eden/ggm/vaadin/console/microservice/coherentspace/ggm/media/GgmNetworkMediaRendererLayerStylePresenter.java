package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.RefreshGgmMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaRendererLayerStylePresenter;

@SpringComponent
@UIScope
public class GgmNetworkMediaRendererLayerStylePresenter extends NetworkMediaRendererLayerStylePresenter {
    @Override
    protected void notifyMultiImageView() {
    	getUIEventBus().publish(this, new RefreshGgmMultiImageEvent());
    }

}
