package org.hkrdi.eden.ggm.vaadin.console.mvp;

import com.vaadin.flow.component.button.Button;

public interface FlowEntityView extends FlowView {
	@Override
	FlowEntityPresenter getPresenter();
	
	@Override
	default void buildView() {
		getSaveButton().addClickListener(e -> {
				getPresenter().save();
		});
	}

	Button getSaveButton();

}
