package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;


import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.ForceRefreshGgmMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStylePresenter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmNetworkMediaLayerStylePresenter extends NetworkMediaLayerStylePresenter {

    @Autowired
    private CoherentSpaceNetworkMediaLayerManager mediaLayerFactory;
    
    @Override
    protected MediaLayerFactory getMediaLayerFactory() {
        return mediaLayerFactory;
    }

    @Override
    protected void notifyMultiImageView() {
    	getUIEventBus().publish(this, new ForceRefreshGgmMultiImageEvent());
    }

}
