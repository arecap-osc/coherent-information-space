package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event;

import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;

import java.util.EventObject;
import java.util.Optional;

public class MapLinkSelectionChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MapLinkSelectionChangeEvent(Optional<MapWord> source) {
        super(source);
    }



}
