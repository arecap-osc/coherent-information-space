package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import java.util.concurrent.atomic.AtomicInteger;

import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ScreenProperties;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Image;

/**
 * Component used to display images
 * Compose Images from MediaRendererLayer.
 * Consumes screen events (click, dbl click, drag, scale)
 *
 * @author abo
 */
public abstract class MultiImagePresenter extends DefaultFlowPresenter<Image, String> {

    @Autowired
    private ImageComposer imageComposer;

    @Autowired
    private ScreenProperties screenProperties;

    private boolean hasImages = false;

    @Override
    public MultiImageView getView() {
        return (MultiImageView)super.getView();
    }

    public abstract MediaRenderLayerFactory getMediaRendererLayerFactory();

    public void composeOrRefreshImages() {
        composeOrRefreshImages(false);
    }

    public void composeOrRefreshImages(Boolean forceRefresh) {
        //TODO on layer order changed should forceRefresh ,
        // or better to be implemented on zIndex change on image from imagesMap in ImageComposer
        if (hasImages) {
            imageComposer.refreshImages(screenProperties);
            return;
        }

        rerenderImages(forceRefresh);
        hasImages = true;
    }

    public void rerenderImages(Boolean forceRefresh) {
        final AtomicInteger zIndex = new AtomicInteger(0);
        imageComposer.getImages(getMediaRendererLayerFactory(), screenProperties, forceRefresh).stream().forEach(image -> {
            image.setSizeFull();
            image.getStyle().set("position", "absolute");
            image.getStyle().set("z-index", zIndex.getAndIncrement() + "");
            getView().add(image);
        });
    }

    public void forceRefreshAllImages() {
        composeOrRefreshImages(true);
    }

    public ImageComposer getImageComposer() {
        return imageComposer;
    }

    public void setImageComposer(ImageComposer imageComposer) {
        this.imageComposer = imageComposer;
    }

    public ScreenProperties getScreenProperties() {
        return screenProperties;
    }

    public void setScreenProperties(ScreenProperties screenProperties) {
        this.screenProperties = screenProperties;
    }

    public void dragScreenCenter(Double screenX, Double screenY) {
        screenProperties.setCenterY(GeometryUtil.round(screenY, 2));
        screenProperties.setCenterX(GeometryUtil.round(screenX, 2));
        refreshAll();
    }

    public void refreshAll() {
        getMediaRendererLayerFactory().getMediaRendererLayers().stream()
                .forEach(mediaRendererLayer -> mediaRendererLayer.setNeedRefresh(true));
        composeOrRefreshImages();
    }

    public void scale(Double scale) {
        screenProperties.setScale(scale);
        if (screenProperties.getScale().compareTo(0.2d) < 0) {
            screenProperties.setScale(0.2d);
        }
        refreshAll();
    }

    public void setScreenBounds(Double width, Double height) {
        screenProperties.setWidth(width);
        screenProperties.setHeight(height);
        refreshAll();
    }

    public void mouseWheel(double wheelDelta) {
        if (Math.abs(wheelDelta) > 15d) {
            scale(GeometryUtil.round(screenProperties.getScale() + (wheelDelta < 0 ? -0.1 : 0.1), 2));
        }
    }

    public void gesturePinch(double scale) {
        scale(GeometryUtil.round(screenProperties.getScale() + (scale > 0 ? -0.1 : 0.1), 2));
    }

    public void onMouseStop(double clientX, double clientY) {
        dragScreenCenter(clientX, clientY);
    }

    public void handleClick(double clientX, double clientY) {
        doClickBusiness(clientX, clientY);
    }

    public void handleRightClick(double clientX, double clientY) {
        doRightClickBusiness(clientX, clientY);
    }

    public void doRightClickBusiness(Double screenX, Double screeenY) {
        System.out.println("RightClick(" + screenX + ", " + screeenY + ")");
    }

    public void doClickBusiness(Double screenX, Double screeenY) {
    }

    public void handleMouseHover(double offsetX, double offsetY) {
        onMouseHover(offsetX, offsetY);
    }

    public void onMouseHover(Double offsetX, Double offsetY) {

    }
}
