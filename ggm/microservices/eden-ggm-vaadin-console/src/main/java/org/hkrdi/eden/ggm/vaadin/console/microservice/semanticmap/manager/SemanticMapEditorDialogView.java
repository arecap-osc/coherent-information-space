package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.manager;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;


@UIScope
@SpringComponent
public class SemanticMapEditorDialogView extends Dialog implements FlowEntityView {
    @Autowired
    private SemanticMapEditorDialogPresenter presenter;

    private Grid<SemanticMap> semanticMapGrid = new Grid<>(SemanticMap.class);

    private TextField filter = new TextField();

    private TextField label = new TextField("textfield.label.label");

    private TextField brief = new TextField("textfield.brief.label");

    private TextField description = new TextField("textfield.description.label");

    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

    private Button saveButton = new Button("button.save.label", VaadinIcon.CHECK.create());

    private Button cancelButton = new Button("button.cancel.label");

    private Button deleteButton = new Button("button.delete.label", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);

    private VerticalLayout editorForm = new VerticalLayout(label, brief, description, actions);

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        setWidth("700px");
        buildEventListeners();
        buildViewWrapper();
    }

    private void buildViewWrapper() {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, semanticMapGrid, editorForm);
        editorForm.setVisible(false);
        semanticMapGrid.setColumns("id", "label", "brief", "description");
        semanticMapGrid.setHeightByRows(true);
        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("error");
    }

    private void buildEventListeners() {
        deleteButton.addClickListener(getPresenter()::handleDeleteEvent);
        cancelButton.addClickListener(getPresenter()::handleCancelEvent);
        addNewBtn.addClickListener(getPresenter()::handleAddNewEvent);
        filter.addValueChangeListener(getPresenter()::handleFilterValueChangeEvent);
        semanticMapGrid.addSelectionListener(getPresenter()::handleSemanticMapSelectionEvent);
    }

    public VerticalLayout getEditorForm() {
        return editorForm;
    }

    public Grid<SemanticMap> getSemanticMapGrid() {
        return semanticMapGrid;
    }

    @Override
    public SemanticMapEditorDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return saveButton;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }
}
