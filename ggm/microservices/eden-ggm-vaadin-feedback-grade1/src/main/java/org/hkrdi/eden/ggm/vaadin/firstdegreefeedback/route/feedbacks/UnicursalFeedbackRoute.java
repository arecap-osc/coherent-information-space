package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.route.feedbacks;


import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.MainLayout;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal.UnicursalMainView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "first-degree-feedback_unicursal_edit", layout = MainLayout.class)
public class UnicursalFeedbackRoute extends HorizontalLayout{

	@Autowired
	public UnicursalFeedbackRoute(UnicursalMainView view){
		this.setWidthFull();
		add(view);
	}
}
