package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.route.user;

import javax.annotation.PostConstruct;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.portalwindow.StaticMenuBar;
import com.vaadin.flow.spring.template.ComponentSetup;
import com.vaadin.flow.spring.template.route.RouteComponent;

@RouteComponent
public class UserOption extends VerticalLayout implements ComponentSetup {

	@Autowired
	private StaticMenuBar staticMenuBar;

	@Autowired
    private RouteMenu routeMenu;

    @PostConstruct
    public void setup() {
        setPadding(false);
        Icon btnRouteIcon = VaadinIcon.GRID_SMALL.create();
        btnRouteIcon.setSize("35px");
        btnRouteIcon.setColor("white");
        Button routeBtn = new Button(btnRouteIcon);
        routeBtn.setWidthFull();
        routeBtn.addClickListener(l -> routeMenu.open());
        add(routeBtn);
        Icon btnNotificationIcon = VaadinIcon.BELL.create();
        btnNotificationIcon.setSize("35px");
        btnNotificationIcon.setColor("white");
        Button notificationBtn = new Button(btnNotificationIcon);
        notificationBtn.setWidthFull();
        add(notificationBtn);

        add(buildUserProfile());
        
        staticMenuBar.add(this);
    }

    private Component buildUserProfile(){
        TokenAuthentication token = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();

        Image btnUserIcon = new Image(token.getClaims().get("picture").asString(), "");
        btnUserIcon.setWidth("35px");
        btnUserIcon.setHeight("35px");
        btnUserIcon.getStyle().set("border-radius", "50%");
//        btnUserIcon.setColor("#cccccc");
        Button userBtn = new Button(btnUserIcon);

        userBtn.getElement().setProperty("title", token.getClaims().get("name").asString()+" "+token.getClaims().get("email").asString());
        userBtn.setWidthFull();

        userBtn.addClickListener(l->{
            UI.getCurrent().getPage().executeJavaScript("window.open(\"/logout\", \"_self\");");
        });

        return userBtn;
    }
}
