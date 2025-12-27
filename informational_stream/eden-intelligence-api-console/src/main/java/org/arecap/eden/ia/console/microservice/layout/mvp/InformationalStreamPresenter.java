package org.arecap.eden.ia.console.microservice.layout.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.informationalstream.support.service.InformationalStreamService;
import org.arecap.eden.ia.console.microservice.event.InformationalStreamFactoryRefreshedEvent;
import org.arecap.eden.ia.console.microservice.event.InformationalStreamSelectionChangedEvent;
import org.arecap.eden.ia.console.microservice.layout.UiI18nUtil;
import org.arecap.eden.ia.console.mvp.DefaultFlowPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.EventObject;
import java.util.Optional;

@SpringComponent
@UIScope
public class InformationalStreamPresenter
        extends DefaultFlowPresenter<InformationalStreamView> {

    @Autowired
    private InformationalStreamService informationalStreamService;

    @Override
    public void afterPrepareModel(EventObject event) {
        setInformationalStreamItems();
    }

    @EventBusListenerMethod
    public void onInformationalStreamFactoryRefreshedEvent(InformationalStreamFactoryRefreshedEvent isFactoryRefreshedEvent) {
        setInformationalStreamItems();
    }

    public void publishInformationalStreamSelection(Optional<InformationalStream> informationalStream) {
        if(informationalStream.isPresent()) {
            UiI18nUtil.setLocale(informationalStream.get().getLocale());
        }
        getUIEventBus().publish(this,
                new InformationalStreamSelectionChangedEvent(informationalStream));
    }

    private void setInformationalStreamItems() {
        getUIEventBus().publish(this,
                new InformationalStreamSelectionChangedEvent(Optional.empty()));
        getView().setItems(informationalStreamService.findAll());
    }


}
