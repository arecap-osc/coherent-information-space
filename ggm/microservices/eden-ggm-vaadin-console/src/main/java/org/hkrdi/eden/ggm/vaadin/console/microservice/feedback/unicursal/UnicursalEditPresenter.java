package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import java.util.EventObject;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UnicursalEditPresenter extends DefaultFlowEntityPresenter<UnicursalDataMap, Long>{
	
	private static final long serialVersionUID = 1189016102279737849L;

	@Autowired 
	private UnicursalDataMapService service;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Override
	public UnicursalDataMapService getService() {
		return service;
	}

	@Override
	public Long getInitialEntityId() {
		UnicursalDataMap entity = service.findByApplicationIdAndRowAndColumn(feedbackSelection.getApplication().getId(), 
				feedbackSelection.getRow(), feedbackSelection.getColumn());
		if (entity == null) {
			return null;
		}
		feedbackSelection.setUnicursalDataMap(entity);
		return entity.getId();
	}

	@Override
	public Class<UnicursalDataMap> getEntityType() {
		return UnicursalDataMap.class;
	}

	@Override
	public UnicursalEditView getView() {
		return (UnicursalEditView)super.getView();
	}

	
	@Override
	public void afterPrepareModel(EventObject event) {
		feedbackSelection.setUnicursalDataMap(getEntity());
	}

	@Override
	public void afterSave() {
		feedbackSelection.setUnicursalDataMap(getEntity());
	}

	
}
