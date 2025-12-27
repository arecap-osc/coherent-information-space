package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp;

import java.io.Serializable;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.IGgmEntity;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.IGgmService;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;

public interface IGgmPresenter<E extends IGgmEntity> extends Serializable {

	default void prepareModelAndView(BeforeEnterEvent event) {
		prepareModel(event);
		
		getView().beforePrepareView(event);
		getView().prepareView();
		
		afterPrepareModel(event);
		
		getView().afterPrepareView();
	}
	
	void setView(IGgmView view);

	IGgmView getView();
	
	default IGgmService<E> getService(){
		return null;
	}
	
	default void setService(IGgmService<E> service) {
	}

	default  void beforeLeavingView(BeforeLeaveEvent event) {
	}	

	default void prepareModel(BeforeEnterEvent event) {
	}

	default  void afterPrepareModel(BeforeEnterEvent event) {
	}
	
}
