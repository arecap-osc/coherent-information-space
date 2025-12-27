package org.arecap.eden.ia.console.mvp;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class DefaultFlowPresenter<V extends FlowView> implements FlowPresenter<V>{
	
	private static final long serialVersionUID = 8713397848603426939L;

	private V view;
	
	@Autowired
    private UIEventBus uIEventBus;
	
	@PostConstruct
    private void initEventBus() {
    	uIEventBus.subscribe(this);
    }
    
    @PreDestroy
    private void predestroyEventBus() {
    	uIEventBus.unsubscribe(this);
    }

	@Override
	public void setView(V view) {
		this.view = view;
	}

	@Override
	public V getView() {
		return view;
	}
	
	public UIEventBus getUIEventBus() {
		return uIEventBus;
	}
}
