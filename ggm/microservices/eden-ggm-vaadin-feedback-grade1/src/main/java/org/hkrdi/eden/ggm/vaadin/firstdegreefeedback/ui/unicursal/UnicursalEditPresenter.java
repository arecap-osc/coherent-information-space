package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.FeedbackApplicationService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.IGgmService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.UnicursalDataMapService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common.FeedbackSelection;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.StatusChangeEvent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UnicursalEditPresenter implements IGgmEntityPresenter<UnicursalDataMap>{
	
	private static final long serialVersionUID = 1189016102279737849L;

	@Autowired
	private FeedbackApplicationService applicationService;
	
	@Autowired 
	private UnicursalDataMapService service;
	
	private IGgmView view;
	
	private UnicursalDataMap entity;
	
	private BeanValidationBinder<UnicursalDataMap> binder;
	
	@Autowired
	private FeedbackSelection feedbackSelection;
	
	@Override
	public void setView(IGgmView view) {
		this.view = view;
	}

	@Override
	public IGgmView getView() {
		return view;
	}

	@Override
	public IGgmService<UnicursalDataMap> getService() {
		return service;
	}

	@Override
	public void setEntity(UnicursalDataMap entity) {
		this.entity = entity;
	}

	@Override
	public UnicursalDataMap getEntity() {
		return entity;
	}

	@Override
	public Long getInitialEntityId() {
		feedbackSelection.setApplication(applicationService.getAllApplications().get(0));
		UnicursalDataMap entity = service.findByApplicationIdAndRowAndColumn(feedbackSelection.getApplication().getId(), 
				feedbackSelection.getRow(), feedbackSelection.getColumn());
		if (entity == null) {
			return null;
		}
		return entity.getId();
	}

	@Override
	public void onFormStatusChange(StatusChangeEvent event) {
//		IGgmEntityPresenter.super.onFormStatusChange(event);
//		save();
	}

	@Override
	public BeanValidationBinder<UnicursalDataMap> getBinder() {
		return binder;
	}

	@Override
	public void setBinder(BeanValidationBinder<UnicursalDataMap> binder) {
		this.binder = binder;
	}

	@Override
	public Class<UnicursalDataMap> getEntityType() {
		return UnicursalDataMap.class;
	}

}
