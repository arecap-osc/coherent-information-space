package org.arecap.eden.ia.console.mvp;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import org.arecap.eden.ia.console.common.i18n.flow.InternationalizeViewEngine;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.EventObject;

public interface FlowView<P extends FlowPresenter> extends BeforeEnterObserver,BeforeLeaveObserver, Serializable, LocaleChangeObserver{
	
	@PostConstruct
	default void init() {
		getPresenter().setView(this);
		buildView();
	}

	default void buildView() {
	}


	default void onAttach(AttachEvent attachEvent) {
		getPresenter().prepareModelAndView(attachEvent);
	}

	@Override
	default void beforeEnter(BeforeEnterEvent event) {
		//TODO default
//		getPresenter().prepareModelAndView(event);
	}

	@Override
	default void beforeLeave(BeforeLeaveEvent event) {
		getPresenter().beforeLeavingView(event);
	}
	
	P getPresenter();
		
	default void beforePrepareView(EventObject event){
	}
	
	default void prepareView() {
	}
	
	default void afterPrepareView() {
	}
	
	default void internationalize() {
		InternationalizeViewEngine.internationalize(this);
	}

	default void localeChange(LocaleChangeEvent event) {
		InternationalizeViewEngine.internationalize(this, event.getLocale());
	}
	
}
