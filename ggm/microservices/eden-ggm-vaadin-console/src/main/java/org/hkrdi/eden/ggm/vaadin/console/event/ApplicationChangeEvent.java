package org.hkrdi.eden.ggm.vaadin.console.event;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;

import java.util.EventObject;
import java.util.Optional;

@Deprecated
public class ApplicationChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationChangeEvent(Optional<Application> source) {
        super(source);
    }
}
