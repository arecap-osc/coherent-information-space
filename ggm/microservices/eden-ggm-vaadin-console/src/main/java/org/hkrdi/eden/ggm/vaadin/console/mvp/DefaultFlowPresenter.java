package org.hkrdi.eden.ggm.vaadin.console.mvp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

public class DefaultFlowPresenter<T, ID> implements FlowPresenter<T, ID>{
	
	private static final long serialVersionUID = 8713397848603426939L;

	private FlowView view;
	
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
	public void setView(FlowView view) {
		this.view = view;
	}

	@Override
	public FlowView getView() {
		return view;
	}
	
	public UIEventBus getUIEventBus() {
		return uIEventBus;
	}
}
