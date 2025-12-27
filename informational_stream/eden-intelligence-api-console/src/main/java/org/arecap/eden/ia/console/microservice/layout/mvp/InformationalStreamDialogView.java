package org.arecap.eden.ia.console.microservice.layout.mvp;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@SpringComponent
@UIScope
public class InformationalStreamDialogView extends Dialog
        implements FlowEntityView<InformationalStreamDialogPresenter, VerticalLayout>, HasItems<InformationalStream> {

    @Autowired
    private InformationalStreamDialogPresenter presenter;

    private Grid<InformationalStream> informationalStreamGrid = new Grid<>(InformationalStream.class);

    private TextField filter = new TextField();

    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

    private TextField name = new TextField("textfield.name.label");
    private TextArea functionalDescription = new TextArea("textarea.functionaldescription.label");
    private ComboBox<Locale> locale = new ComboBox<>("combobox.locale.label", Locale.ENGLISH, new Locale("RO"));
    private ComboBox<InformationalStream> i18nComboBox = new ComboBox<>();


    private Button save = new Button("button.save.label", VaadinIcon.CHECK.create());
    private Button cancel = new Button("button.cancel.label");
    private Button delete = new Button("button.delete.label", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private VerticalLayout editorForm = new VerticalLayout(name, functionalDescription, locale, i18nComboBox, actions);

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        buildViewWrapper();
        buildEventListeners();
    }

    private void buildEventListeners() {
        addNewBtn.addClickListener(this::addNewBtnFired);
        filter.addValueChangeListener(this::searchTextChangedFired);
        cancel.addClickListener(this::cancelBtnFired);
        delete.addClickListener(this::deleteBtnFired);
        informationalStreamGrid.addSelectionListener(this::gridSelectionChangedFired);
        locale.addValueChangeListener(this::localeValueChanged);
        i18nComboBox.addValueChangeListener(this::i18nValueChanged);
    }

    private void deleteBtnFired(ClickEvent<Button> buttonClickEvent) {
        getPresenter().delete();
        setEditorFormVisibility(false);
    }

    private void localeValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Locale>, Locale> comboBoxLocaleComponentValueChangeEvent) {
        getPresenter().setLocale(Optional.ofNullable(comboBoxLocaleComponentValueChangeEvent.getValue()));
        setI18nVisibility(!comboBoxLocaleComponentValueChangeEvent.getHasValue().isEmpty());

    }

    private void i18nValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<InformationalStream>, InformationalStream> comboBoxInformationalStreamComponentValueChangeEvent) {
        getPresenter().setI8nBean(Optional.ofNullable(comboBoxInformationalStreamComponentValueChangeEvent.getValue()));
    }

    private void gridSelectionChangedFired(SelectionEvent<Grid<InformationalStream>, InformationalStream> gridInformationalStreamSelectionEvent) {
        getPresenter()
                .publishInformationalStreamSelection(gridInformationalStreamSelectionEvent.getFirstSelectedItem());
        setEditorFormVisibility(gridInformationalStreamSelectionEvent.getFirstSelectedItem().isPresent());
    }

    private void cancelBtnFired(ClickEvent<Button> buttonClickEvent) {
        setEditorFormVisibility(false);
    }

    private void searchTextChangedFired(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        getPresenter().filterViewItemsByText(textFieldStringComponentValueChangeEvent.getValue());
    }

    private void addNewBtnFired(ClickEvent<Button> buttonClickEvent) {
        getPresenter().handleAddNewEvent();
        setEditorFormVisibility(true);
    }

    private void buildViewWrapper() {
        editorForm.setVisible(false);
        save.getElement().getThemeList().add("primary");

        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        informationalStreamGrid.setColumns("id", "name", "locale");
        informationalStreamGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        locale.setAllowCustomValue(false);
        i18nComboBox.setItemLabelGenerator(is -> is.getName());
        i18nComboBox.setAllowCustomValue(false);

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, informationalStreamGrid, editorForm);

    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public InformationalStreamDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }


    @Override
    public void setItems(Collection<InformationalStream> collection) {
        informationalStreamGrid.setItems(collection);
    }

    public void setI18nItems(Collection<InformationalStream> collection) {
        i18nComboBox.setItems(collection);
    }

    public void setI18nValue(Optional<InformationalStream> informationalStream) {
        if(informationalStream.isPresent()) {
            i18nComboBox.setValue(informationalStream.get());
        }
    }

    @Override
    public VerticalLayout getEditorForm() {
        return editorForm;
    }

    public void setI18nVisibility(Boolean visible) {
        i18nComboBox.setVisible(visible);
    }
}
