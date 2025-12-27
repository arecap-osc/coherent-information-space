package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback;


import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal.UnicursalMainView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftBarUpperSideView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "first-degree-feedback/unicursal_edit", layout= GgmSystemPwaLayout.class)
public class UnicursalFeedbackRoute extends GgmSystemRouteLayout {

	@Autowired
	private UnicursalMainView view;

//	@Autowired
//	private TopBarView topBarView;
	
	@Autowired
	private LeftBarUpperSideView leftBarUpperSideView;

	@Override
	protected void buildRouteLayout() {
//		leftBarUpperSideView.add(imageLogoView);
//		topBarView.addComponentAtIndex (0, imageLogoView);
		setSizeFull();
		add(view);
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
