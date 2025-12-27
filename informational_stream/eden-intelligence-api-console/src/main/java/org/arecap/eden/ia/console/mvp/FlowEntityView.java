package org.arecap.eden.ia.console.mvp;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.HasItems;

import javax.annotation.PostConstruct;

public interface FlowEntityView<P extends FlowEntityPresenter, T extends Component> extends FlowView<P> {

	@PostConstruct
	default void initSaveButton() {
		getSaveButton().addClickListener(this::saveBtnFired);
	}

	default void saveBtnFired(ClickEvent<Button> buttonClickEvent) {
		getPresenter().save();
		setEditorFormVisibility(false);
		if(HasItems.class.isAssignableFrom(this.getClass())) {
			((HasItems)this).setItems(getPresenter().getItems());
		}
	}

	Button getSaveButton();

	default T getEditorForm(){
		return (T) new VerticalLayout();
	}

	default void setEditorFormVisibility(Boolean visible){
		getEditorForm().setVisible(visible);
	}
}
