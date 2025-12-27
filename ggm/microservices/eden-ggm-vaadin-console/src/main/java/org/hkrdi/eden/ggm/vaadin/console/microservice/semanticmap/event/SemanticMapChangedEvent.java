package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event;

import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;

import java.util.EventObject;

public class SemanticMapChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public SemanticMapChangedEvent(SemanticMap source) {
        super(source);
    }
}
