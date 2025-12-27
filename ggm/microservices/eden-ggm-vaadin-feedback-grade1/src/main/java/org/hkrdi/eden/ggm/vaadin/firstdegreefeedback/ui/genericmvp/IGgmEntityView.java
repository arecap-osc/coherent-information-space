package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp;

import com.vaadin.flow.component.button.Button;

public interface IGgmEntityView extends IGgmView{
	@Override
	IGgmEntityPresenter getPresenter();
	
	@Override
	default void afterPrepareView() {
		getSaveButton().addClickListener(e ->{
			getPresenter().save();
		});
	}

	Button getSaveButton();
}
