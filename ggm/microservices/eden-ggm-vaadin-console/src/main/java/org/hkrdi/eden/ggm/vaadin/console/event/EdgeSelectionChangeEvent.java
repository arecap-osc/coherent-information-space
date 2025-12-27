package org.hkrdi.eden.ggm.vaadin.console.event;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;

import java.util.EventObject;
import java.util.Optional;

public class EdgeSelectionChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public EdgeSelectionChangeEvent(Optional<EdgeBean> source) {
        super(source);
    }
}
