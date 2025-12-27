package org.arecap.eden.ia.console.microservice.layout.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.informationalstream.support.service.InformationalStreamService;
import org.arecap.eden.ia.console.microservice.event.InformationalStreamFactoryRefreshedEvent;
import org.arecap.eden.ia.console.mvp.DefaultFlowEntityPresenter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EventObject;
import java.util.Locale;
import java.util.Optional;

@SpringComponent
@UIScope
public class InformationalStreamDialogPresenter extends DefaultFlowEntityPresenter<InformationalStreamDialogView, InformationalStream, Long, InformationalStreamService> {

    @Autowired
    private InformationalStreamService informationalStreamService;

    @Override
    public InformationalStreamService getService() {
        return informationalStreamService;
    }

    @Override
    public Class<InformationalStream> getEntityType() {
        return InformationalStream.class;
    }

    @Override
    public void afterPrepareModel(EventObject event) {
        getView().setItems(getService().findAll());
    }

    @Override
    public void afterSave() {
        getView().setEditorFormVisibility(false);
        getView().setItems(getService().findAll());
        getUIEventBus().publish(this, new InformationalStreamFactoryRefreshedEvent(Optional.of(getEntity())));
    }

    public void handleAddNewEvent() {
        setEntity(new InformationalStream());
    }

    public void filterViewItemsByText(String value) {
        getView().setItems(getService().getAllByNameStartsWithIgnoreCase(value));
    }

    public void publishInformationalStreamSelection(Optional<InformationalStream> selectedItem) {
        if(selectedItem.isPresent()) {
            setEntity(selectedItem.get());
            setI18nBean(getEntity().getLocale());
        }
    }

    public void setI8nBean(Optional<InformationalStream> value) {
        getEntity().setI18nId(value.isPresent() ? value.get().getId() : getEntity().getI18nId());
    }

    public void setLocale(Optional<Locale> value) {
        if(value.isPresent()) {
            getView().setI18nItems(getService().findAllByLocaleNot(value.get()));
            setI18nBean(value.get());
        }
    }

    private void setI18nBean(Locale locale) {
        if(getEntity() != null && getEntity().getId() != null) {
            getView().setI18nValue(getService().getI18nBeans(getEntity()).stream()
                    .filter(is -> !is.getLocale().equals(locale)).findFirst());
        }
    }

}


