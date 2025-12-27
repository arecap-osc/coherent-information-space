package org.arecap.eden.ia.console.mvp;


import java.io.Serializable;
import java.util.EventObject;

public interface FlowPresenter<V extends FlowView> extends Serializable {


	default void prepareModelAndView(EventObject event) {
		prepareModel(event);

		getView().beforePrepareView(event);
		getView().prepareView();
		afterPrepareModel(event);
		getView().afterPrepareView();
		getView().internationalize();
	}

	void setView(V view);

	V getView();

	default  void beforeLeavingView(EventObject event) {
	}	

	default void prepareModel(EventObject event) {
	}

	default  void afterPrepareModel(EventObject event) {
	}
	
}
