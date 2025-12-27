package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaRendererLayerStylePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RefreshRoadMultiImageEvent;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadNetworkMediaRendererLayerStylePresenter extends NetworkMediaRendererLayerStylePresenter {
    @Override
    protected void notifyMultiImageView() {
    	getUIEventBus().publish(this, new RefreshRoadMultiImageEvent());
    }

}
