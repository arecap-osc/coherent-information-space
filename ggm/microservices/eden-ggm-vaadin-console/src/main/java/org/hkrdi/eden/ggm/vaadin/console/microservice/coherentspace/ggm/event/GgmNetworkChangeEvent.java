package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event;

import java.util.EventObject;
import java.util.Optional;

public class GgmNetworkChangeEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param network The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public GgmNetworkChangeEvent(Optional<String> network) {
        super(network);
    }

}
