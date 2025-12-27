package org.hkrdi.eden.ggm.vaadin.console.event;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;

import java.util.EventObject;
import java.util.Optional;

public class ClusterSelectionChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ClusterSelectionChangeEvent(Optional<ClusterBean> source) {
        super(source);
    }
}
