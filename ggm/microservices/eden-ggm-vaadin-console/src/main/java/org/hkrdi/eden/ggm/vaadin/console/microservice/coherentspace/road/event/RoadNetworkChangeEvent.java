package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event;

import java.util.EventObject;

public class RoadNetworkChangeEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param network The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RoadNetworkChangeEvent(String network) {
        super(network);
    }

}
