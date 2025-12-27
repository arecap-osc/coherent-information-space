package org.hkrdi.eden.ggm.vaadin.console.mvp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.StatusChangeEvent;

public abstract class DefaultFlowEntityPresenter<T, ID> implements FlowEntityPresenter<T, ID>{
	private static Logger LOGGER = LoggerFactory.getLogger(DefaultFlowEntityPresenter.class);
	
	private FlowView view;
	private ID entityId;	
	private BeanValidationBinder<T> binder;
	
	/**
	 * Used to store old values, before apply binder values. 
	 */
	private T entityValuesBeforeBinderUpdate = null;
	
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
    public FlowView getView() {
    	return view;
    }

    @Override
	public void setView(FlowView view) {
		this.view = view;
	}

    @Override
    public ID getInitialEntityId() {
        return entityId;
    }
    
    public UIEventBus getUIEventBus() {
    	return uIEventBus;
    }
    
    public void setEntityId(ID entityId) {
    	this.entityId = entityId;
    }

	@Override
	public BeanValidationBinder<T> getBinder() {
		return binder;
	}

	@Override
	public void setBinder(BeanValidationBinder<T> binder) {
		this.binder = binder;
	}
	
	@Override
	public void onFormStatusChange(StatusChangeEvent event) {
		onEntityFormStatusChange(event);
		entityValuesBeforeBinderUpdate = cloneEntity(getEntity());
		
	}

	public T getEntityValuesBeforeBinderUpdate() {
		return entityValuesBeforeBinderUpdate;
	}
	
	public void onEntityFormStatusChange(StatusChangeEvent event) {}
	
	public T cloneEntity(T entity) {
		return null;
	}

}
