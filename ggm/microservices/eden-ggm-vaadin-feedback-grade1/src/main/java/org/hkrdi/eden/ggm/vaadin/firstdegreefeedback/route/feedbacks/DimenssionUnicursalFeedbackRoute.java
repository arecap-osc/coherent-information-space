package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.route.feedbacks;


import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.MainLayout;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.dimensions.DimenssionUnicursalView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "first-degree-feedback", layout = MainLayout.class)
public class DimenssionUnicursalFeedbackRoute extends HorizontalLayout{// implements ViewMvc {
	
	@Autowired
	public DimenssionUnicursalFeedbackRoute(DimenssionUnicursalView f){
		add(f);
	}
}
