package org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.media.mvp.MediaRenderImageScalePresenter;

@SpringComponent
@UIScope
public class MandelbrotSetGraphDisplayOptionPresenter extends MediaRenderImageScalePresenter<MandelbrotSetGraphDisplayOptionView> {


    public void handleScaleUp(ClickEvent<Button> buttonClickEvent) {
        getView().getMediaRenderImageView().getPresenter()
                .scale(getView().getMediaRenderImageView().getPresenter().getScreenProperties().getScale() - 0.4D);
    }

    public void handleScaleDown(ClickEvent<Button> buttonClickEvent) {
        getView().getMediaRenderImageView().getPresenter()
                .scale(getView().getMediaRenderImageView().getPresenter().getScreenProperties().getScale() + 0.4D);
    }

}
