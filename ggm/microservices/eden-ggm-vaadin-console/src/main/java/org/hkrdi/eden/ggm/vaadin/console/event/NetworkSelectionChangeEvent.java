package org.hkrdi.eden.ggm.vaadin.console.event;

import java.util.EventObject;
import java.util.Optional;

@Deprecated
public class NetworkSelectionChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public NetworkSelectionChangeEvent(Optional<String> source) {
        super(source);
    }
}
