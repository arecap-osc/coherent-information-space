package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;

/**
 * Component used to display images
 * Compose Images from MediaRendererLayer.
 * Consumes screen events (click, dbl click, drag, scale)
 * 
 * @author abo
 *
 */
public abstract class MultiImageScaleView extends HorizontalLayout implements FlowView {

	private Button scaleUpBtn = new Button(VaadinIcon.PLUS.create());

	private Button scaleDownBtn = new Button(VaadinIcon.MINUS.create());

	@Override
	public void buildView() {
		buildViewWrapper();
		mapDomEvents();
		buildEventListeners();
	}
	
	@Override
	public abstract MultiImageScalePresenter getPresenter();

	public abstract MultiImageView getMultiImageView();

	private void buildViewWrapper() {
		scaleUpBtn.getStyle().set("border-radius","50%").set("background-color", "white");//#D3D3D3");
		scaleUpBtn.getStyle().set("color", "red");
		scaleDownBtn.getStyle().set("border-radius","50%").set("background-color", "white");//"transparent");
		scaleDownBtn.getStyle().set("color", "red");

		getStyle().set("position", "absolute");
		getStyle().set("bottom", "20px");
		getStyle().set("right", "20px");
		getStyle().set("background-color", "transparent");//"white");
		getStyle().set("z-index", "1000");
		add(scaleUpBtn, scaleDownBtn);
	}

	private void buildEventListeners() {

		scaleUpBtn.addClickListener(getPresenter()::handleScaleUp);
		scaleDownBtn.addClickListener(getPresenter()::handleScaleDown);
	}

	private void mapDomEvents() {
		getElement()
				.addEventListener("click", e-> {})
				.addEventData("event.stopPropagation()");
		getElement()
				.addEventListener("mousedown", e-> {})
				.addEventData("event.stopPropagation()");
		getElement()
				.addEventListener("mousemove", e -> {})
				.addEventData("event.stopPropagation()");
	}

	@Override
	public void onAttach(AttachEvent attachEvent) {
		FlowView.super.onAttach(attachEvent);
	}
}
