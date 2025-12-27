package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.route.user;

import javax.annotation.PostConstruct;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.route.feedbacks.DimenssionUnicursalFeedbackRoute;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.portalwindow.ModalMenu;
import com.vaadin.flow.spring.template.ComponentSetup;

@SpringComponent
@UIScope
public class RouteMenu extends VerticalLayout implements ComponentSetup {

    private H4 title = new H4("Alege");
    
    public RouteMenu() {
		super();
	}

	@Autowired
    private ModalMenu modalMenu;

    @PostConstruct
    public void setup() {
    	setHeightFull();
        setWidth("150px");
        getStyle().set("color", "#ccc");
        Anchor firstDegreeFeedbackRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(DimenssionUnicursalFeedbackRoute.class), "Feedback-uri de gradul 1");
        add(title, firstDegreeFeedbackRoute);
    }

    public void open() {
        modalMenu.open(this);
    }

}
