package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UnicursalMainPresenter implements IGgmPresenter{
	
	private static final long serialVersionUID = 1;

	private IGgmView view;
	
	@Override
	public IGgmView getView() {
		return view;
	}

	@Override
	public void setView(IGgmView view) {
		this.view = view;
	}
}
