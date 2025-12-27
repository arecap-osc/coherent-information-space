package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;

/**
 * Component used to display images
 * Compose Images from MediaRendererLayer.
 * Consumes screen events (click, dbl click, drag, scale)
 * 
 * @author abo
 *
 */
public abstract class MultiImageScalePresenter extends DefaultFlowPresenter<Image, String> {

	@Override
	public MultiImageScaleView getView() {
		return (MultiImageScaleView)super.getView();
	}

	public void handleScaleUp(ClickEvent<Button> buttonClickEvent) {
		getView().getMultiImageView().getPresenter()
				.scale(getView().getMultiImageView().getPresenter().getScreenProperties().getScale() + 0.2D);
	}

	public void handleScaleDown(ClickEvent<Button> buttonClickEvent) {
		getView().getMultiImageView().getPresenter()
				.scale(getView().getMultiImageView().getPresenter().getScreenProperties().getScale() - 0.2D);
	}

}
