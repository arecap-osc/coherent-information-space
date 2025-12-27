package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ApplicationDialogView extends Dialog implements FlowEntityView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDialogView.class);

    @Autowired
    private ApplicationDialogPresenter presenter;

    private Grid<Application> applicationGrid = new Grid<>(Application.class);

    private TextField filter = new TextField();

    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

    private TextField label = new TextField("textfield.label.label");
    private TextField brief = new TextField("textfield.brief.label");
    private TextField description = new TextField("textfield.description.label");

    private ComboBox<SemanticMap> semanticMapComboBox = new ComboBox<>();
    
    private ComboBox<Application> relatedAppComboBox = new ComboBox<>();
    
    private ComboBox<String> languageComboBox = new ComboBox<>("", "en", "ro");

    private Button save = new Button("button.save.label", VaadinIcon.CHECK.create());
    private Button cancel = new Button("button.cancel.label");
    private Button delete = new Button("button.delete.label", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private VerticalLayout editorForm = new VerticalLayout(label, brief, description, languageComboBox, relatedAppComboBox, semanticMapComboBox, actions);

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        setWidth("700px");
        buildViewWrapper();
        buildEventListeners();
    }

    public void open(ApplicationView applicationView) {
        getPresenter().setApplicationPresenter(applicationView.getPresenter());
        open();
    }

    private void buildViewWrapper() {
        editorForm.setVisible(false);

        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

//        applicationGrid.setColumns("id", "brief.label", "brief.brief", "brief.description");
        applicationGrid.removeAllColumns();
        applicationGrid.addColumn(application -> application.getBrief() == null ? "" :
                (application.getBrief().getLabel() == null ? "" : application.getBrief().getLabel()))
                .setHeader("Aplicatie");
        applicationGrid
                .addColumn(application -> application.getSemanticMap() == null ? "" :
                        (application.getSemanticMap().getLabel() == null ? "" : application.getSemanticMap().getLabel()))
                .setHeader("Harta Semantica");

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, applicationGrid, editorForm);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        semanticMapComboBox.setItemLabelGenerator(SemanticMap::getLabel);
        semanticMapComboBox.setAllowCustomValue(false);
        
        relatedAppComboBox.setItemLabelGenerator(app->(app.getBrief().getBrief()!=null?app.getBrief().getBrief():""));
        relatedAppComboBox.setAllowCustomValue(false);
        
        languageComboBox.setItemLabelGenerator(lang->lang!=null?lang.toUpperCase():"");
        languageComboBox.setAllowCustomValue(false);
    }

    private void buildEventListeners() {
        addNewBtn.addClickListener(getPresenter()::handleAddNewEvent);
        delete.addClickListener(getPresenter()::handleDeleteEvent);
        cancel.addClickListener(getPresenter()::handleCancelEvent);
        filter.addValueChangeListener(getPresenter()::handleFilterValueChangeEvent);
        applicationGrid.addSelectionListener(getPresenter()::handleApplicationSelectionEvent);
        semanticMapComboBox.addValueChangeListener(getPresenter()::setApplicationSemanticGridId);
        
        relatedAppComboBox.addValueChangeListener(getPresenter()::setRelatedApplicationId);
        languageComboBox.addValueChangeListener(getPresenter()::setLanguage);
    }


    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public ApplicationDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

    public Grid<Application> getApplicationGrid() {
        return applicationGrid;
    }

    public ComboBox<SemanticMap> getSemanticMapComboBox() {
        return semanticMapComboBox;
    }

    public ComboBox<Application> getRelatedAppComboBox() {
		return relatedAppComboBox;
	}

	public ComboBox<String> getLanguageComboBox() {
		return languageComboBox;
	}

	public VerticalLayout getEditorForm() {
        return editorForm;
    }

    public TextField getLabel() {
        return label;
    }

    public TextField getBrief() {
        return brief;
    }

    public TextField getDescription() {
        return description;
    }
}
