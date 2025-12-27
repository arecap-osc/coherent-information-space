package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.appmanager;

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
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class FeedbackApplicationEditorDialogView extends Dialog implements FlowEntityView {
    @Autowired
    private FeedbackApplicationEditorDialogPresenter presenter;

    private Grid<FeedbackApplication> applicationGrid = new Grid<>(FeedbackApplication.class);

    private TextField filter = new TextField();

    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

    private TextField label = new TextField("textfield.label.label");
    private TextField brief = new TextField("textfield.brief.label");
    private TextField description = new TextField("textfield.description.label");

    private Button save = new Button("button.save.label", VaadinIcon.CHECK.create());
    private Button cancel = new Button("button.cancel.label");
    private Button delete = new Button("button.delete.label", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private VerticalLayout feedbackApplicationEditorForm = new VerticalLayout(label, brief, description, actions);

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        setWidth("700px");
        buildViewWrapper();
        buildEventListeners();
    }

    private void buildViewWrapper() {
        feedbackApplicationEditorForm.setVisible(false);

        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        applicationGrid.setColumns("id", "label", "brief", "description");
        applicationGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        applicationGrid.setItems(getPresenter().getService().getAllApplications());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, applicationGrid, feedbackApplicationEditorForm);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
    }

    private void buildEventListeners() {
        addNewBtn.addClickListener(getPresenter()::handleAddNewEvent);
        filter.addValueChangeListener(getPresenter()::handleFilterValueChangeEvent);
        delete.addClickListener(getPresenter()::handleDeleteEvent);
        cancel.addClickListener(getPresenter()::handleCancelEvent);
        applicationGrid.addSelectionListener(getPresenter()::handleApplicationSelectionEvent);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public FeedbackApplicationEditorDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

    public VerticalLayout getFeedbackApplicationEditorForm() {
        return feedbackApplicationEditorForm;
    }

    public Grid<FeedbackApplication> getApplicationGrid() {
        return applicationGrid;
    }
}
