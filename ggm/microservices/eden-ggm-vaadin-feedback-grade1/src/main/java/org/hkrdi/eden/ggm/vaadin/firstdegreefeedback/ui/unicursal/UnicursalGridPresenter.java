package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.FeedbackApplicationService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.FeedbackDataMapService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service.IGgmService;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common.FeedbackSelection;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UnicursalGridPresenter implements IGgmPresenter<FeedbackDataMap>{
	
	private static final long serialVersionUID = 1;

	@Autowired
	private FeedbackApplicationService applicationService;
	
	@Autowired 
	private FeedbackDataMapService service;
	
	private IGgmView view;
	
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
	public IGgmService<FeedbackDataMap> getService() {
		return (IGgmService<FeedbackDataMap>) service;
	}

	@Override
	public void prepareModel(BeforeEnterEvent event) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public Long getInitialEntityId() {
//		feedbackSelection.setApplication(applicationService.getAllApplications().get(0));
//		UnicursalDataMap entity = service.findByApplicationIdAndRowAndColumn(feedbackSelection.getApplication().getId(), 
//				feedbackSelection.getRow(), feedbackSelection.getColumn());
//		if (entity == null) {
//			return null;
//		}
//		return entity.getId();
//	}
}
