package org.arecap.eden.ia.console.media.mvp;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import org.arecap.eden.ia.console.mvp.DefaultFlowPresenter;

/**
 * Component used to display images
 * Compose Images from MediaRendererLayer.
 * Consumes screen events (click, dbl click, drag, scale)
 * 
 * @author abo
 *
 */
public abstract class MediaRenderImageScalePresenter<V extends MediaRenderImageScaleView> extends DefaultFlowPresenter<V> {


	public void handleScaleUp(ClickEvent<Button> buttonClickEvent) {
		getView().getMediaRenderImageView().getPresenter()
				.scale(getView().getMediaRenderImageView().getPresenter().getScreenProperties().getScale() + 0.2D);
	}

	public void handleScaleDown(ClickEvent<Button> buttonClickEvent) {
		getView().getMediaRenderImageView().getPresenter()
				.scale(getView().getMediaRenderImageView().getPresenter().getScreenProperties().getScale() - 0.2D);
	}

}
