package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.dimensions;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.IGgmService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.UnicursalDataMapService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class DimenssionUnicursalPresenter implements IGgmPresenter{

	private static final long serialVersionUID = 1L;

	@Autowired
	private UnicursalDataMapService service;

	private IGgmView view;
	
	@Override
	public void prepareModel(BeforeEnterEvent event) {
	}

	@Override
	public void setView(IGgmView view) {
		this.view = view;
	}

	@Override
	public IGgmView getView() {
		return view;
	}

	@Override
	public IGgmService<?> getService() {
		return service;
	}
}
