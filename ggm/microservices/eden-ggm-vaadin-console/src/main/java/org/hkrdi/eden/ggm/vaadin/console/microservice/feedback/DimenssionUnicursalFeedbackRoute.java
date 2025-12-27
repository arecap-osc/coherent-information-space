package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback;


import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.appmanager.FeedbackApplicationManagerView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.dimensions.DimenssionUnicursalView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "first-degree-feedback", layout= GgmSystemPwaLayout.class)
public class DimenssionUnicursalFeedbackRoute extends GgmSystemRouteLayout {// implements ViewMvc {
	
	@Autowired
	private DimenssionUnicursalView f;

	@Autowired
	private FeedbackApplicationManagerView feedbackApplicationManager;

	@Autowired
	private TopBarView topBarView;
	
	@Override
	protected void buildRouteLayout() {
//		leftBarUpperSideView.add(imageLogoView);
		topBarView.add(feedbackApplicationManager);
		add(f);
//		getStyle().set("overflow", "auto");
//		getStyle().set("border-left", "1px solid black");
	}

	@Override
	protected String getRouteNameForBredCrumbAndTooltip() {
		return "msg.route.breadcrumb.firstdegreefeedback";
	}

	@Override
	protected String getRouteIconPath() {
		return "/frontend/img/dimenssion_01.png";
	}
	
}
