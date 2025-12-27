package org.arecap.eden.ia.console.microservice.event;

import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;

import java.util.EventObject;
import java.util.Optional;

public class InformationalStreamFactoryRefreshedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public InformationalStreamFactoryRefreshedEvent(Optional<InformationalStream> source) {
        super(source);
    }
}
