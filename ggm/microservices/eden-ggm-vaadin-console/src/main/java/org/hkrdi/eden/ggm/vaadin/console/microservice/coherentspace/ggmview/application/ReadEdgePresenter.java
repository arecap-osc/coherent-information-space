package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;

@SpringComponent
@UIScope
public class ReadEdgePresenter extends ApplicationDataPresenter {

    private Long applicationDataId;

    public void setApplicationDataId(Long applicationDataId) {
        this.applicationDataId = applicationDataId;
    }

    @Override
    public Long getInitialEntityId() {
        return applicationDataId;
    }
}
