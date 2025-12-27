package org.hkrdi.eden.ggm.vaadin.console.microservice.common.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

public abstract class CustomSaveDialog extends Dialog{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSaveDialog.class);

    public void setConfirmationFunctionAndPrepareView(ComponentEventListener<ComponentEvent<Dialog>> saveListener) {
		getConfirmButton().addClickListener(e -> {
			if (checkFields()) {
				ComponentEvent<Dialog> event = getSaveEvent();
				if (event != null) {
					saveListener.onComponentEvent(getSaveEvent());
					this.close();
				}
			}
		});
		
		getCloseButton().addClickListener(e -> {
			this.close();
		});
	}
	
	public abstract Button getConfirmButton();
	
	public abstract Button getCloseButton();

	public ComponentEvent<Dialog> getSaveEvent() {
		return new ComponentEvent<Dialog>(this, true);
	}
	
	protected abstract boolean checkFields();    
}
