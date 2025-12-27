package org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.media.mvp.MediaRenderImageView;

@SpringComponent
@UIScope
public class MandelbrotSetGraphView extends MediaRenderImageView<MandelbrotSetGraphPresenter> {


    @ClientCallable
    public void onMouseWheel(double wheelDelta) {
        //N/A
    }

    @ClientCallable
    public void onGesturePinch(double scale) {
        //N/A
    }

}
