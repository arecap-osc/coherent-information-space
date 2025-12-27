package org.arecap.eden.ia.console.microservice.websites;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "")
public class NoianHomePage extends NoianLandingPage {

    private Image noian = new Image("frontend/img/Logo_Noian.png", "");

    private H1 h1 = new H1("Nosce te ipsum");

    @Override
    protected void buildLayout() {
        setBackgroundImage("url('frontend/img/noian_site_background.jpg')");
        setMenuBarJustifyContentMode(JustifyContentMode.CENTER);
        addMenuBar(h1);
        HorizontalLayout noianWrapper = new HorizontalLayout();
        noianWrapper.setSizeFull();
        noianWrapper.getStyle().set("padding-top", "42px");
        noian.setWidthFull();
        noian.setHeight("83%");
        noian.getStyle().set("object-fit", "contain");
        noianWrapper.add(noian);
        addContent(noianWrapper);
    }


    @Override
    protected void selectRoute() {
        selectRouteTab(getHomeRoute());
    }

}
