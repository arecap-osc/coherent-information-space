package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import java.util.EventObject;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class FeedbackDataMapEditPresenter extends DefaultFlowEntityPresenter<FeedbackDataMap, Long>{
	
	private static final long serialVersionUID = 1189016102279737849L;

	@Autowired 
	private FeedbackDataMapService service;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Override
	public FeedbackDataMapService getService() {
		return service;
	}
	
	@Override
	public Long getInitialEntityId() {
		FeedbackDataMap entity = feedbackSelection.getFeedbackDataMap();
		if (entity == null) {
			return null;
		}
		return entity.getId();
	}
	
	@Override
	public Class<FeedbackDataMap> getEntityType() {
		return FeedbackDataMap.class;
	}
	
	@Override
	public FeedbackDataMapEditView getView() {
		return (FeedbackDataMapEditView)super.getView();
	}
	
	public void onCancel() {
		getView().getDialog().close();
	}
	
	public boolean hasNext() {
		return getEntity().getFeedbackPosition()<feedbackSelection.getMaxNextFeedback();
	}
	
	public boolean hasPrevious() {
		return getEntity().getFeedbackPosition()>feedbackSelection.getMinBackFeedback();
	}
	
	public void onNext() {
		if (hasNext()) {
			FeedbackDataMap nextFeedbackDataMap = service.findAllByApplicationIdAndRowAndColumnAndFeedbackPosition(
					feedbackSelection.getApplication().getId(), feedbackSelection.getRow(), feedbackSelection.getColumn(), 
					getEntity().getFeedbackPosition()+1).get();

			replaceFeedbackDataMap(nextFeedbackDataMap);
		}
	}
	
	public void onPrevious() {
		if (hasPrevious()) {
			FeedbackDataMap previousFeedbackDataMap = service.findAllByApplicationIdAndRowAndColumnAndFeedbackPosition(
					feedbackSelection.getApplication().getId(), feedbackSelection.getRow(), feedbackSelection.getColumn(), 
					getEntity().getFeedbackPosition()-1).get();

			replaceFeedbackDataMap(previousFeedbackDataMap);
		}
	}
	
	private void replaceFeedbackDataMap(FeedbackDataMap feedbackDataMap) {
		feedbackSelection.setFeedbackDataMap(feedbackDataMap);
		
		this.prepareModel(
				new EventObject(new Object()));
		
		getView().afterPrepareView();
	}
	
}
