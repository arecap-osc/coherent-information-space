package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane;

import org.hkrdi.eden.ggm.vaadin.console.microservice.common.component.SaveDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;

public class RoadNameSaveDialog extends SaveDialog{

	private static final Logger LOGGER = LoggerFactory.getLogger(RoadNameSaveDialog.class);

    private TextField nameTextField = new TextField("Nume");
    private TextField groupNameTextField = new TextField("Grup");
    
    public RoadNameSaveDialog(ComponentEventListener<ComponentEvent<Dialog>> saveListener) {
    	super(saveListener);
    }

    public void open(String name, String groupName) {
    	this.nameTextField.setRequired(true);
    	this.groupNameTextField.setRequired(true);

    	nameTextField.setErrorMessage("Trebuie completata o valoare");
    	nameTextField.setWidthFull();
    	groupNameTextField.setErrorMessage("Trebuie completata o valoare");
    	groupNameTextField.setWidthFull();
    	
    	if (name != null) {
    		this.nameTextField.setValue(name);
    	}
    	if (groupName != null) {
    		this.groupNameTextField.setValue(groupName);
    	}
    	
    	getEditorForm().addComponentAsFirst(groupNameTextField);
    	getEditorForm().addComponentAsFirst(nameTextField); 

    	open();
    }

    protected boolean checkFields() {
    	nameTextField.setInvalid(false);
    	groupNameTextField.setInvalid(false);

    	if (nameTextField.getValue() == null || nameTextField.getValue().trim().equals("") ) {
    		nameTextField.setInvalid(true);
    		return false;
    	}else if (groupNameTextField.getValue() != null && groupNameTextField.getValue().trim().equals("") ) {
    		groupNameTextField.setInvalid(true);
    		return false;
    	}
    	return true;
    }
    
    @Override
    public ComponentEvent<Dialog> getSaveEvent() {
    	return new RoadNameSaveEvent(this, true, nameTextField.getValue(), groupNameTextField.getValue());
    }
    
    public static class RoadNameSaveEvent extends ComponentEvent<Dialog> {
    	private String name;
    	private String groupName;
    	
        public RoadNameSaveEvent(Dialog source, boolean fromClient, String name, String groupName) {
			super(source, fromClient);
			this.name = name;
			this.groupName = groupName;
		}

		public String getName() {
			return name;
		}

		public String getGroupName() {
			return groupName;
		}
    }
}
