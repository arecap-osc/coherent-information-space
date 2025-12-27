package org.arecap.eden.ia.console.microservice.layout;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.common.i18n.flow.InternationalizeViewEngine;
import org.arecap.eden.ia.console.featureflag.FeatureFlags;
import org.arecap.eden.ia.console.microservice.layout.template.NavigationModalOptionView;
import org.arecap.eden.ia.console.microservice.nl.feature.NlFeatureRoute;
import org.arecap.eden.ia.console.microservice.nl.signal.NlSignalRoute;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class RouterNavigationView extends VerticalLayout implements LocaleChangeObserver{

    private H4 title = new H4("route.breadcrumb.choose.h4");

    @Autowired
    private NavigationModalOptionView leftNavigationModalOptionView;

    @PostConstruct
    public void setup() {
        setSizeFull();
        getStyle().set("color", "#ccc");
        getStyle().set("position", "relative");
        leftNavigationModalOptionView.getStyle().set("margin", "0px");
        add(title);

        Anchor nlFeatureRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(NlFeatureRoute.class), "route.nlfeatureroute");
        addIfFeatureIsEnabled(nlFeatureRoute, FeatureFlags.NL_FEATURE_ROUTE);

        Anchor nlSignalRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(NlSignalRoute.class), "route.nlsignalroute");
        addIfFeatureIsEnabled(nlSignalRoute, FeatureFlags.NL_SIGNAL_ROUTE);
    }

    public void open() {
        leftNavigationModalOptionView.open(this);
    }

    private void addIfFeatureIsEnabled(Anchor anchor, FeatureFlags featureFlags) {
    	if (featureFlags.check()) {add(anchor);}
    }

	@Override
	public void localeChange(LocaleChangeEvent event) {
		InternationalizeViewEngine.internationalize(this, event.getLocale());
	}
}
