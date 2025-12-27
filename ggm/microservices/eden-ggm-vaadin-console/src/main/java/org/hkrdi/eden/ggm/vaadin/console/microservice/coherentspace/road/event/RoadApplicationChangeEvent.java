package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;

import java.util.EventObject;
import java.util.Optional;

public class RoadApplicationChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RoadApplicationChangeEvent(Optional<Application> source) {
        super(source);
    }


}
