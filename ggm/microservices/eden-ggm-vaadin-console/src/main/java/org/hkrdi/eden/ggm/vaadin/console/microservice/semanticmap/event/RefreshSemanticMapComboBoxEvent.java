package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event;

import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;

import java.util.EventObject;
import java.util.Optional;

public class RefreshSemanticMapComboBoxEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RefreshSemanticMapComboBoxEvent(Optional<SemanticMap> source) {
        super(source);
    }
}
