package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapLinkRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ToMapLinkPresenter extends DefaultFlowEntityPresenter<MapLink, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToMapLinkPresenter.class);

    @Autowired
    private MapLinkRepositoryService service;

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Override
    public MapLinkRepositoryService getService() {
        return service;
    }

    @Override
    public Class<MapLink> getEntityType() {
        return MapLink.class;
    }

    @Override
    public Long getInitialEntityId() {
        return semanticMapIe.getSecondMapLinkId();
    }

    @Override
    public void afterSave() {
        LOGGER.info("Semantic Map Link with id " + getEntity().getId() + " was modified");
    }
}
