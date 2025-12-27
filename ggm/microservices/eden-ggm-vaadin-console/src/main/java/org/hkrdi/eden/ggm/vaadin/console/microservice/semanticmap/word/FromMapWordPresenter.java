package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapWordSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.SemanticMapWordLetterRefreshEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapWordRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class FromMapWordPresenter extends DefaultFlowEntityPresenter<MapWord, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FromMapWordPresenter.class);

    @Autowired
    private MapWordRepositoryService service;

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Override
    public MapWordRepositoryService getService() {
        return service;
    }

    @Override
    public void setEntity(MapWord entity) {
    	super.setEntity(entity);

    	semanticMapIe.setFirstMapWordId(entity.getId());
    }

    @Override
    public Class<MapWord> getEntityType() {
        return MapWord.class;
    }

    @Override
    public Long getInitialEntityId() {
        return semanticMapIe.getFirstMapWordId();
    }

    @Override
    public void afterSave() {
    	getUIEventBus().publish(this, new SemanticMapWordLetterRefreshEvent(semanticMapIe.getSemanticMapId()));
        LOGGER.info("Semantic Map Word with id " + getEntity().getId() + " was modified");
    }

    public void onCancelButtonClickEvent(ClickEvent<Button> buttonClickEvent) {
        semanticMapIe.setFirstMapWordId(null);
        getUIEventBus().publish(this, new MapWordSelectionChangeEvent(Optional.empty()));
    }

}
