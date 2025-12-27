package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UnicursalGridPresenter extends DefaultFlowPresenter<FeedbackDataMap, Long>{
	
	private static final long serialVersionUID = 1;
	
	@Autowired 
	private FeedbackDataMapService service;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Autowired
	private FeedbackDataMapEditView feedbackDataMapEditView;

	private Dialog dialogWithFeedbackDataMap = new Dialog();
	
	@Override
	public void setView(FlowView view) {
		super.setView(view);
		
		//prepare dialog
		feedbackDataMapEditView.setDialog(dialogWithFeedbackDataMap);
		
		dialogWithFeedbackDataMap.add(feedbackDataMapEditView);
		dialogWithFeedbackDataMap.setWidth("1100px");
		dialogWithFeedbackDataMap.setHeight("750px");
		
		dialogWithFeedbackDataMap.addOpenedChangeListener(l->{
			if (l.isOpened() == false) {
//				getView().getGrid().getDataProvider().refreshItem(service.findById(dataMap.getId()).get());
				setGridItems();
			}
		});
		//
	}

	@Override
	public UnicursalGridView getView() {
		return (UnicursalGridView)super.getView();
	}
	
	@Override
	public void prepareModel(EventObject event) {
		setGridItems();
	}
	
	private void setGridItems() {
		List<FeedbackDataMap> allFeedbackDataMap = service.findAllByApplicationIdAndRowAndColumn(
				feedbackSelection.getApplication().getId(), feedbackSelection.getRow(), feedbackSelection.getColumn());
		//TODO just one fix sunt multe erori de nul prin feedback astea in loc sa
		// le vezi pe ele te redirecteaza la login si apuci sa mai vezi nimic
		getView().getGrid().setItems(Optional.ofNullable(allFeedbackDataMap).orElseGet(() -> new ArrayList<>()));
		
		if (allFeedbackDataMap != null && allFeedbackDataMap.size()>2) {
			feedbackSelection.setMinBackFeedback(allFeedbackDataMap.get(0).getFeedbackPosition());
			feedbackSelection.setMaxNextFeedback(allFeedbackDataMap.get(allFeedbackDataMap.size()-1).getFeedbackPosition());
		}
	}
	
	public void onGridItemSelected(FeedbackDataMap dataMap) {
		if (dataMap == null) {return;}
		
		feedbackSelection.setFeedbackDataMap(dataMap);
		
		dialogWithFeedbackDataMap.open();
	}
}
