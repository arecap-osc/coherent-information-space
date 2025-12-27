package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

public interface IGgmView extends BeforeEnterObserver,BeforeLeaveObserver, Serializable{
	
	@PostConstruct
	default void init() {
		getPresenter().setView(this);
		buildView();
	}
	
	default void buildView() {
	}

	@Override
	default void beforeEnter(BeforeEnterEvent event) {
		getPresenter().prepareModelAndView(event);
	}

	@Override
	default void beforeLeave(BeforeLeaveEvent event) {
		getPresenter().beforeLeavingView(event);
	}
	
	IGgmPresenter<?> getPresenter();
		
	default void beforePrepareView(BeforeEnterEvent event){
	}
	
	default void prepareView() {
	}
	
	default void afterPrepareView() {
	}
}
