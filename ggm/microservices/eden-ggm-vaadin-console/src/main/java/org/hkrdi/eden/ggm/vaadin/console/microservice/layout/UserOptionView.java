package org.hkrdi.eden.ggm.vaadin.console.microservice.layout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringComponent
@UIScope
public class UserOptionView extends VerticalLayout implements FlowView {

    @Autowired
    private UserOptionPresenter presenter;

    private Button routeBtn;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public UserOptionPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
//        setSpacing(false);
        setPadding(false);
//        setHeightFull();
        setId("UserOptionView");
        this.setAlignItems(Alignment.END);
        getStyle().set("position", "absolute");
        getStyle().set("bottom", "10px");
        Icon btnRouteIcon = VaadinIcon.GRID_SMALL.create();
        btnRouteIcon.setSize("35px");
        btnRouteIcon.setColor("white");
        routeBtn = new Button(btnRouteIcon);
        routeBtn.setWidthFull();
        routeBtn.addClickListener(getPresenter()::handleOpenRouteNavigationButtonClick);
        add(routeBtn);
        Icon btnNotificationIcon = VaadinIcon.BELL.create();
        btnNotificationIcon.setSize("35px");
        btnNotificationIcon.setColor("white");
        Button notificationBtn = new Button(btnNotificationIcon);
        notificationBtn.setWidthFull();
        add(notificationBtn);

        add(buildUserProfile());
    }

    public void disableRouteButton() {
        routeBtn.setEnabled(false);
        routeBtn.setVisible(false);
    }

    private Component buildUserProfile(){
        TokenAuthentication token = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();

        Image btnUserIcon = new Image(token.getClaims().get("picture").asString(), "");
        btnUserIcon.setWidth("35px");
        btnUserIcon.setHeight("35px");
        btnUserIcon.getStyle().set("border-radius", "50%");
        //        btnUserIcon.setColor("#cccccc");
        Button userBtn = new Button(btnUserIcon);

        userBtn.getElement().setProperty("title", token.getClaims().get("name").asString()+" "+
                ((token.getClaims().get("email")!=null)?token.getClaims().get("email").asString():
                        ((token.getClaims().get("sub")!= null)?token.getClaims().get("sub").asString():"")));
        userBtn.setWidthFull();

        userBtn.addClickListener(l->{
            UI.getCurrent().getPage().executeJavaScript("window.open(\"/logout\", \"_self\");");
        });

        return userBtn;
    }

}
