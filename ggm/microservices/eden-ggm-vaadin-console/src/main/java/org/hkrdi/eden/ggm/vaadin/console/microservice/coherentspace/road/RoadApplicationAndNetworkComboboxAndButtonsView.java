package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road;

import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadApplicationAndNetworkComboboxAndButtonsView extends ApplicationView {

    private Button importApplicationButton = new Button(VaadinIcon.UPLOAD.create());

    private ComboBox<String> coherentSpaceNetworkComboBox = new ComboBox<>();

    @Autowired
    private RoadApplicationAndNetworkComboboxAndButtonsPresenter presenter;

    @Override
    public RoadApplicationAndNetworkComboboxAndButtonsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        super.buildView();
        coherentSpaceNetworkComboBox.setWidth("250px");
        coherentSpaceNetworkComboBox.setEnabled(false);
        coherentSpaceNetworkComboBox.getElement().setAttribute("title", "tooltip.currentnetwork.combobox");
        importApplicationButton.getElement().setAttribute("title", "tooltip.importapplication.button");
        getWrapperLayout().add(importApplicationButton, coherentSpaceNetworkComboBox);
        importApplicationButton.addClickListener(getPresenter()::handleImportApplicationButtonClick);
        coherentSpaceNetworkComboBox.addValueChangeListener(getPresenter()::handleCoherentSpaceNetworkComboBoxValueChange);
    }

    @Override
    public void afterPrepareView() {
        super.afterPrepareView();
        if (coherentSpaceNetworkComboBox.getOptionalValue().isPresent() == false) {
            getPresenter().coherentSpaceNetworkComboBoxSetItemsAndSelectValue();
        }
    }

    public ComboBox<String> getCoherentSpaceNetworkComboBox() {
        return coherentSpaceNetworkComboBox;
    }
}
