package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class ImportNodeValueDialogView extends Dialog implements FlowEntityView {

    @Autowired
    private ImportNodeValueDialogPresenter presenter;

    private HorizontalLayout nodeFieldHL = new HorizontalLayout();
    
    private ComboBox<Etl> nodeField = new ComboBox<>();

    private TextArea semantic = new TextArea("msg.import.node.dialog.semantic");

    private Checkbox showAllDetails = new Checkbox("msg.import.node.dialog.showalldetails", false);
    
    private Button reset = new Button("button.dezalocate.label", VaadinIcon.DISC.create());
    
    private Button save = new Button("button.apply.label", VaadinIcon.DISC.create());

    private Button close = new Button( "button.close.label", VaadinIcon.CLOSE.create());

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        setCloseOnOutsideClick(true);
        setWidth("800px");
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        HorizontalLayout settingsLayout = new HorizontalLayout();
        settingsLayout.setWidthFull();
        HorizontalLayout actionLayout = new HorizontalLayout();
        actionLayout.setWidthFull();
        nodeFieldHL.setWidthFull();
        semantic.setWidthFull();
        settingsLayout.add(showAllDetails);
        mainLayout.add(settingsLayout, nodeFieldHL, semantic, actionLayout);
        actionLayout.add(save, reset, close);
        add(mainLayout);
        
        showAllDetails.addValueChangeListener(this::showAllDetailsValueChanged);
        
        close.addClickListener(e -> close());
        
        reset.addClickListener(e -> {
			getPresenter().reset();
		});
    }

    private String etlWhatToShowInCombo(Etl etl) {
    	if (etl.getDescription().trim().isEmpty()) {
    		return etl.getBrief();
    	}
    	
    	if (etl.getBrief().trim().isEmpty()) {
    		return etl.getDescription();
    	}
    	
    	if (!etl.getBrief().isEmpty() && !etl.getDescription().trim().isEmpty() && etl.getDescription().trim().contains(etl.getBrief().trim())) {
    		return etl.getDescription().trim().replace("\n", "\t");
    	}
    	
    	if (!etl.getDescription().isEmpty() &&!etl.getBrief().isEmpty() && etl.getBrief().trim().contains(etl.getDescription().trim())) {
    		return etl.getBrief().trim().replace("\n", "\t");
    	}
    	
    	if (!etl.getDescription().isEmpty() &&!etl.getBrief().isEmpty() && etl.getBrief().trim().equals(etl.getDescription().trim())) {
    		return etl.getDescription().trim().replace("\n", "\t");
    	}
    	
    	if (!etl.getBrief().trim().equals(etl.getDescription().trim())){
			return etl.getBrief().trim() +" / "+ etl.getDescription().trim();
		}
		return etl.getDescription();
    }
    
    private String getImportDescription(Etl etl) {
    	return etl.getId()+":"+etl.getImportDescription();
    }
    
    private void showAllDetailsValueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> valueChangeEvent) {
    	UI.getCurrent().getSession().setAttribute("showAllDetailsCoboboxValue", valueChangeEvent.getValue());
    	getPresenter().prepareModelAndView(valueChangeEvent);
    }
    
    private void nodeFieldValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Etl>, Etl> nodeFieldValueChangeEvent) {
    	if (showAllDetails.getValue()) {
    		semantic.setValue(nodeFieldValueChangeEvent.getValue().getDescription());
    	}else {
    		semantic.setValue(etlWhatToShowInCombo(nodeFieldValueChangeEvent.getValue()).replace("\t", "\n").replace(" / ", "\n"));
    	}
        
    }

    public Optional<Etl> getSelectedForNodeField() {
        return nodeField.getOptionalValue();
    }

    
    @Override
    public void prepareView() {
    	Boolean showAllValue = (Boolean) UI.getCurrent().getSession().getAttribute("showAllDetailsCoboboxValue");
    	if (showAllValue != null) {
    		showAllDetails.setValue(showAllValue);
    	}
    	nodeFieldHL.remove(nodeField);
    	nodeField = new ComboBox<>();
    	nodeFieldHL.add(nodeField);
    	nodeField.setWidthFull();
        nodeField.setItems(getPresenter().getNotImported());
        if (showAllDetails.getValue()) {
    		nodeField.setItemLabelGenerator(etl -> getImportDescription(etl));
    	}else {
    		nodeField.setItemLabelGenerator(etl->etlWhatToShowInCombo(etl));
    	}
        nodeField.addValueChangeListener(this::nodeFieldValueChanged);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public ImportNodeValueDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }
    
}
