package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.event;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;

import java.util.EventObject;
import java.util.Optional;

public class RefreshFeedbackApplicationComboBoxEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RefreshFeedbackApplicationComboBoxEvent(Optional<FeedbackApplication> source) {
        super(source);
    }
}
