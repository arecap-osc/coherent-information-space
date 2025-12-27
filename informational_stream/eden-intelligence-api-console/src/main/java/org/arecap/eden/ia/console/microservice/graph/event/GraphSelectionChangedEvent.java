package org.arecap.eden.ia.console.microservice.graph.event;

import java.util.EventObject;
import java.util.Optional;

public class GraphSelectionChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public GraphSelectionChangedEvent(Optional<String> source) {
        super(source);
    }
}
