package org.arecap.eden.ia.console.microservice.layout.template;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.arecap.eden.ia.console.common.i18n.flow.InternationalizeViewEngine;
import org.arecap.eden.ia.console.microservice.layout.UserOptionView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class ConsoleRouteLayout extends HorizontalLayout implements LocaleChangeObserver {

	@Autowired
	private UserOptionView userOptionView;

	@Autowired
	private LeftBarDownSideView leftBarDownSideView;

	@Autowired
	private LeftBarUpperSideView leftBarUpperSideView;

	@Autowired
	private TopBarView topBarView;

	private Label label;

	@PostConstruct
	protected void setup() {
		setSizeFull();
		setPadding(false);
		setSpacing(false);
		//		getStyle().set("padding", "0px 0px 0px 0px");
		leftBarDownSideView.add(userOptionView);

		leftBarUpperSideView.add(generateRouteIcon(getRouteIconPath()));

		topBarView.addComponentAsFirst(generateRouteNameLabel(getRouteNameForBredCrumbAndTooltip()));

		buildRouteLayout();
	}

	protected abstract void buildRouteLayout();

	protected abstract String getRouteIconPath();

	protected abstract String getRouteNameForBredCrumbAndTooltip();

	private HorizontalLayout generateRouteIcon(String iconPath) {
		//      getStyle().set("background-color", "#f2f2f2");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(false);
		hl.setPadding(false);
		Image logo = new Image(iconPath, getRouteNameForBredCrumbAndTooltip());
		logo.setWidth("40px");
		logo.setHeight("40px");
		logo.getStyle().set("margin-top", "10px");
		logo.getStyle().set("margin-left", "10px");
		logo.getStyle().set("margin-bottom", "10px");
		logo.getStyle().set("border-radius", "50%");

		logo.getElement().setAttribute("title", getRouteNameForBredCrumbAndTooltip());
		hl.add(logo);
		return hl;
	}

	private Component generateRouteNameLabel(String text) {
		//    	label.getStyle().set("font-weight", "bold")
		// ;
		label = new Label(text);
		label.getStyle().set("padding-left", "10px");
		label.getStyle().set("padding-right", "0px");
		label.getStyle().set("white-space", "nowrap");

		HorizontalLayout hl = new HorizontalLayout();
		//    	hl.setSpacing(false);
		//    	hl.setPadding(false);

		hl.add(label);

		Label label2 = new Label(" | ");
		//    	label.getStyle().set("font-weight", "bold");
		label2.getStyle().set("padding-left", "0px");
		label2.getStyle().set("padding-right", "5px");
		label2.getStyle().set("white-space", "nowrap");
		hl.add(label2);

		hl.setVerticalComponentAlignment(Alignment.CENTER, label);
		return hl;
	}

	public void localeChange(LocaleChangeEvent event) {
		if(label != null) {
			InternationalizeViewEngine.internationalize(label, event.getLocale());
		}
	}

}