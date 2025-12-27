package org.hkrdi.eden.ggm.vaadin.console.microservice.common.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class SaveDialog extends Dialog{
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDialog.class);

    private Button save = new Button("button.save.label", VaadinIcon.CHECK.create());
    private Button cancel = new Button("button.cancel.label");

    private HorizontalLayout actions = new HorizontalLayout(save, cancel);

    private VerticalLayout editorForm = new VerticalLayout(actions);

    public SaveDialog(ComponentEventListener<ComponentEvent<Dialog>> saveListener) {
    	setConfirmationFunctionAndPrepareView(saveListener);
    }
	
    public void setConfirmationFunctionAndPrepareView(ComponentEventListener<ComponentEvent<Dialog>> saveListener) {
		setWidth("400px");
		add(editorForm);
		save.getElement().getThemeList().add("primary");

		cancel.addClickListener(e->{
			this.close();
		});
		save.addClickListener(e -> {
			if (checkFields()) {
				saveListener.onComponentEvent(getSaveEvent());
				this.close();
			}
		});
	}
	
	public ComponentEvent<Dialog> getSaveEvent() {
		return new ComponentEvent<Dialog>(this, true);
	}
	
	public VerticalLayout getEditorForm() {
		return editorForm;
	}

	protected abstract boolean checkFields();    
}
