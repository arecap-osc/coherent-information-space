package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.common.SemanticMapImportService;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.RefreshSemanticMapComboBoxEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

import java.util.Optional;

@SpringComponent
@UIScope
public class SemanticMapImportPresenter implements FlowPresenter {
    private SemanticMapImportView view;

    @Autowired
    private SemanticMapImportService semanticMapImportService;

    @Autowired
    private UIEventBus uiEventBus;

    @Override
    public void setView(FlowView view) {
        this.view = (SemanticMapImportView) view;
    }

    @Override
    public SemanticMapImportView getView() {
        return view;
    }

    public void onCheckBoxValueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> checkboxBooleanComponentValueChangeEvent) {
        getView().getSpecificSemanticMapTextField().setEnabled(!getView().getSpecificSemanticMapTextField().isEnabled());
    }

    public void onImportButtonClick(ClickEvent<Button> buttonClickEvent) {
        String insertStatements = getView().getInsertStatementsTextArea().getValue();
        boolean success;
        if (getView().getSpecificSemanticMapTextField().isEnabled()) {
            success = semanticMapImportService.importSemanticMap(insertStatements, getView().getSpecificSemanticMapTextField().getValue());
            Notification.show(success ? "Importul s-a realizat cu succes" : "Importul a esuat");
        } else {
            success = semanticMapImportService.importSemanticMap(insertStatements);
            Notification.show(success ? "Importul s-a realizat cu succes" : "Importul a esuat");
        }

        getView().close();
        getView().getSpecificSemanticMapTextField().clear();
        getView().getSpecificSemanticMapTextField().setEnabled(false);
        getView().getSpecificSemanticMapCheckBox().setValue(false);
        getView().getInsertStatementsTextArea().clear();

        if (success) {
            uiEventBus.publish(this, new RefreshSemanticMapComboBoxEvent(Optional.empty()));
        }
    }
}
