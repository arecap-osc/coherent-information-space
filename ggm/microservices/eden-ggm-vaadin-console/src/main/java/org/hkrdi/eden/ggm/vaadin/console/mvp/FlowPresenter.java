package org.hkrdi.eden.ggm.vaadin.console.mvp;




import org.hkrdi.eden.ggm.vaadin.console.service.repository.RepositoryService;

import java.io.Serializable;
import java.util.EventObject;

public interface FlowPresenter<T, ID> extends Serializable {


	default void prepareModelAndView(EventObject event) {
		prepareModel(event);

		getView().beforePrepareView(event);
		getView().prepareView();
		afterPrepareModel(event);
		getView().afterPrepareView();
		getView().internationalize();
	}

	void setView(FlowView view);

	FlowView getView();

	default RepositoryService<T, ID> getService(){
		return null;
	}
	
	default void setService(RepositoryService<T, ID> service) {
	}

	default  void beforeLeavingView(EventObject event) {
	}	

	default void prepareModel(EventObject event) {
	}

	default  void afterPrepareModel(EventObject event) {
	}
	
}
