package org.arecap.eden.ia.console.microservice.layout.template.mediaconsole.mvp;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


@SpringComponent
@UIScope
public class MenuBarView extends HorizontalLayout implements FlowView<MenuBarPresenter> {

    private HorizontalLayout logoWrapper = new HorizontalLayout();

    @Autowired
    private MenuBarPresenter menuBarPresenter;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public MenuBarPresenter getPresenter() {
        return menuBarPresenter;
    }

    private Image action = new Image();

    private Image logo = new Image();

    @Override
    public void buildView() {
        setWidthFull();
        setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        setHeight("50px");
        setPadding(false);
        setSpacing(false);
        action.setHeightFull();
        action.setWidth("50px");
        action.getStyle().set("object-fit", "contain");

        getStyle().set("border-bottom", "1px solid black");
        logo.setHeightFull();
        logo.setWidth("100px");
        logo.getStyle().set("object-fit", "contain");
        logoWrapper.setSizeFull();
        logoWrapper.add(logo, action);
        logoWrapper.setJustifyContentMode(JustifyContentMode.END);
        logoWrapper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        logoWrapper.getStyle().set("padding-right", "56px");
        add(logoWrapper);

    }

    public void setLogoFeature(Boolean logoFeature, String logoPath) {
        logo.setVisible(logoFeature);
        if(logoFeature && Optional.ofNullable(logoPath).isPresent()){
            logo.setSrc(logoPath);
        }
        setLogoWrapperFeature();
    }

    public void setActionFeature(Boolean actionFeature, String actionPath) {
        action.setVisible(actionFeature);
        if(actionFeature && Optional.ofNullable(actionFeature).isPresent()) {
            action.setSrc(actionPath);
        }
        setLogoWrapperFeature();
    }

    private void setLogoWrapperFeature() {
        logoWrapper.setVisible(logo.isVisible() || action.isVisible());
    }

}
