package org.arecap.eden.ia.console.microservice.event;

import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;

import java.util.EventObject;
import java.util.Optional;

public class FeatureSelectionChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public FeatureSelectionChangedEvent(Optional<Feature> source) {
        super(source);
    }

}
