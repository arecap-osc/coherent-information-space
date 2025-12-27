package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.Optional;

@SpringComponent
@UIScope
public class FirstNodePresenter extends ApplicationDataPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstNodePresenter.class);

    @Override
    public Long getInitialEntityId() {
        return getApplicationDataIe().getFirstNodeId();
    }

    @Override
    public void afterSave() {
        super.afterSave();
        LOGGER.info("First node with id " + getApplicationDataIe().getFirstNodeId() + " was modified");
    }

    @EventBusListenerMethod
    public void onInformationSavedEvent(InformationSavedEvent event) {
        if (getEntity() != null && getEntity().getId() != null) {
            Optional<ApplicationData> entity = getService().findById(getEntity().getId());
            if (entity.isPresent()) {
                setEntity(entity.get());
            }
        }
    }
}
