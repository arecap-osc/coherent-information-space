package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.phrase;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.event.PhraseChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.EventObject;
import java.util.Optional;

@SpringComponent
@UIScope
public class PhrasePresenter extends ApplicationDataPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhrasePresenter.class);

    @Override
    public PhraseView getView() {
        return (PhraseView)super.getView();
    }

    @Override
    public void prepareModel(EventObject event) {
        super.prepareModel(event);
        if (getInitialEntityId() != null) {
            getView().setFromNodeText(getEntity().getSemantic());
            Optional<DataMap> toSemanticDataMap = getCoherentSpaceService()
                    .findNodeDataMap(new NodeBean(getEntity().getNetwork(), getEntity().getToAddressIndex()));
            if (toSemanticDataMap.isPresent()) {
                ApplicationData toSemantic = getService()
                        .getApplicationData(getEntity().getApplication().getId(), toSemanticDataMap.get());
                getView().setToNodeText(toSemantic.getSemantic());
            }
        }
    }

    @Override
    public Long getInitialEntityId() {
        return getApplicationDataIe().getSyntaxId();
    }

    @EventBusListenerMethod
    public void onFromNodeChange(PhraseChangeEvent.FromSemanticChange fromSemanticChange) {
        getView().setFromNodeText((String) fromSemanticChange.getSource());
    }

    @EventBusListenerMethod
    public void onSyntaxChange(PhraseChangeEvent.SyntaxChange syntaxChange) {
        getView().setRouteText((String) syntaxChange.getSource());
    }

    @EventBusListenerMethod
    public void onToNodeChange(PhraseChangeEvent.ToSemanticChange toSemanticChange) {
        getView().setToNodeText((String) toSemanticChange.getSource());
    }

    @Override
    public void afterSave() {
        super.afterSave();
        LOGGER.info("Phrase with id " + getApplicationDataIe().getSyntaxId() + " was modified");
    }
}
