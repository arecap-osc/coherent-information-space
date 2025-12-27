package org.arecap.eden.ia.console.microservice.layout.mvp;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;

@SpringComponent
@UIScope
public class InformationalStreamView extends HorizontalLayout
        implements FlowView<InformationalStreamPresenter>, HasItems<InformationalStream> {

    @Autowired
    private InformationalStreamPresenter presenter;

    private ComboBox<InformationalStream> informationalStreamComboBox = new ComboBox<>();

    private Button editButton = new Button(VaadinIcon.MENU.create());

    @Autowired
    private InformationalStreamDialogView neuralNetworkDialogView;

    @Override
    public void buildView() {
        informationalStreamComboBox.setItemLabelGenerator(InformationalStream::getName);
        editButton.addClickListener(e -> neuralNetworkDialogView.open());
        informationalStreamComboBox.addValueChangeListener(this::informationalStreamComboBoxValueChanged);
        add(informationalStreamComboBox, editButton);
    }

    private void informationalStreamComboBoxValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<InformationalStream>, InformationalStream> comboBoxInformationalStreamComponentValueChangeEvent) {
        getPresenter()
                .publishInformationalStreamSelection(Optional.ofNullable(comboBoxInformationalStreamComponentValueChangeEvent.getValue()));
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public InformationalStreamPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setItems(Collection<InformationalStream> collection) {
        informationalStreamComboBox.setItems(collection);
    }

}