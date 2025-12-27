package org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot;


import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.media.MediaRenderLayerFactory;
import org.arecap.eden.ia.console.media.mvp.MediaRenderImagePresenter;
import org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot.media.MandelbrotSetGraphMedia;

@SpringComponent
@UIScope
public class MandelbrotSetGraphPresenter extends MediaRenderImagePresenter<MandelbrotSetGraphView> {


    private MandelbrotSetGraphMedia mandelbrotSetGraphMedia = new MandelbrotSetGraphMedia();

    @Override
    public MediaRenderLayerFactory getMediaRendererLayerFactory() {
        return mandelbrotSetGraphMedia;
    }

}
