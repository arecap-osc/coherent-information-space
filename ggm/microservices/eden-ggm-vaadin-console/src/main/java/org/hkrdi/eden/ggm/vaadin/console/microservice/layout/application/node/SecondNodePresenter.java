package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class SecondNodePresenter extends ApplicationDataPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecondNodePresenter.class);

    @Override
    public Long getInitialEntityId() {
        return getApplicationDataIe().getSecondNodeId();
    }

    @Override
    public void afterSave() {
        super.afterSave();
        LOGGER.info("Second node with id " + getApplicationDataIe().getSecondNodeId() + " was modified");
    }

}
