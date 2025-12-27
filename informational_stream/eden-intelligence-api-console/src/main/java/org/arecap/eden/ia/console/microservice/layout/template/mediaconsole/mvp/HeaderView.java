package org.arecap.eden.ia.console.microservice.layout.template.mediaconsole.mvp;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.microservice.layout.JsUtil;
import org.arecap.eden.ia.console.microservice.layout.UiI18nUtil;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.arecap.eden.ia.console.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;
import java.util.Optional;

@SpringComponent
@UIScope
public class HeaderView extends HorizontalLayout implements FlowView<HeaderPresenter> {

    @Autowired
    private HeaderPresenter headerWrapperPresenter;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public HeaderPresenter getPresenter() {
        return headerWrapperPresenter;
    }

    private Image logo = new Image();

    private H6 storyTitle = new H6();

    private Tabs routes = new Tabs();

    private ComboBox<Locale> i18nSelector = new ComboBox<Locale>("", Locale.ENGLISH, new Locale("RO"));

    @Override
    public void buildView() {
        setWidthFull();
        setHeight("83px");
        setPadding(false);
        setSpacing(false);
        logo.setHeightFull();
        logo.setWidth("178px");
        logo.getStyle().set("object-fit", "contain");

        storyTitle.getStyle().set("border-left", "1px solid black");
        i18nSelector.getStyle().set("max-width", "100px");
        i18nSelector.getStyle().set("right", "0px");
        i18nSelector.setAllowCustomValue(false);
        i18nSelector.setValue(UI.getCurrent().getLocale());
        i18nSelector.addValueChangeListener(e -> UiI18nUtil.setLocale(e.getValue()));
        HorizontalLayout rightWrapper = new HorizontalLayout();
        rightWrapper.setSizeFull();
        rightWrapper.setJustifyContentMode(JustifyContentMode.END);
        rightWrapper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        rightWrapper.getStyle().set("padding-right", "5px");
        setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        rightWrapper.add(routes, i18nSelector, buildUserProfile());
        add(logo,storyTitle,rightWrapper);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        FlowView.super.localeChange(event);
        if(Optional.ofNullable(event.getLocale()).isPresent()){
            i18nSelector.setValue(event.getLocale());
        }
     }

    public void addRouteTab(Tab routeTab) {
        routes.add(routeTab);
    }

    public void selectRouteTab(Tab routeTab) {
        routes.setSelectedTab(routeTab);
    }

    public void setLogoFeature(Boolean logoFeature, String logoPath) {
        logo.setVisible(logoFeature);
        if(logoFeature && Optional.ofNullable(logoPath).isPresent()) {
            logo.setSrc(logoPath);
        }
    }

    public void setStoryTitleFeature(Boolean storyTitleFeature, String i18nStoryTitle) {
        storyTitle.setVisible(storyTitleFeature);
        if(storyTitleFeature && Optional.ofNullable(i18nStoryTitle).isPresent()) {
            storyTitle.setText(i18nStoryTitle);
        }
    }

    public void setRoutesFeature(Boolean routesFeature) {
        routes.setVisible(routesFeature);
    }

    public void setI18nSelectorFeature(Boolean i18nSelectorFeature) {
        i18nSelector.setVisible(i18nSelectorFeature);
    }

    private Component buildUserProfile(){

        if(SecurityContextHolder.getContext().getAuthentication().getClass().isAssignableFrom(TokenAuthentication.class)) {
            TokenAuthentication token = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
            if(token.isAuthenticated()) {
                Image btnUserIcon = new Image(token.getClaims().get("picture").asString(), "");
                btnUserIcon.setWidth("35px");
                btnUserIcon.setHeight("35px");
                btnUserIcon.getStyle().set("object-fit", "contain");
                btnUserIcon.getStyle().set("border-radius", "50%");
                //        btnUserIcon.setColor("#cccccc");

                btnUserIcon.getElement().setProperty("title", token.getClaims().get("name").asString() + " " + ((token.getClaims().get("email") != null) ? token.getClaims().get("email").asString() : ((token.getClaims().get("sub") != null) ? token.getClaims().get("sub").asString() : "")));

                btnUserIcon.addClickListener(l -> {
                    UI.getCurrent().getPage().executeJavaScript("window.open(\"/logout\", \"_self\");");
                });

                return btnUserIcon;
            }
        }
        Button signIn = new Button("button.signin.label");
        signIn.addClickListener(e -> JsUtil.execute("window.location='/login';"));
        return signIn;
    }


}
