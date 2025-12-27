package org.arecap.eden.ia.console.media.mvp;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.arecap.eden.ia.console.microservice.layout.JsUtil;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Component used to display images
 * Compose Images from MediaRendererLayer.
 * Consumes screen events (click, dbl click, drag, scale)
 *
 * @author abo
 */
@Tag("multi-image-view")
@HtmlImport("frontend://media/multi-image-view.html")
public abstract class MediaRenderImageView<P extends MediaRenderImagePresenter> extends PolymerTemplate<TemplateModel> implements FlowView<P> {

    @Autowired
    private P presenter;

    @Id("layout-wrapper-multi-image-view")
    private HorizontalLayout wrapper;

    @Override
    public void afterPrepareView() {
        JsUtil.execute("var plotterWidth = 0;\n" + "var plotterHeight = 0;" + "canvasWrapperSize();\n" +

                "function canvasWrapperSize() { \n"
                + "   \tif(plotterWidth != $0.offsetWidth || plotterHeight != $0.offsetHeight) {"
                + "       \t$0.$server.setCanvasWrapperSize($0.offsetWidth, $0.offsetHeight);\n"
                + "       \tplotterWidth = $0.offsetWidth;\n" + "       \tplotterHeight = $0.offsetHeight;\n" + "   \t}"
                + "   \tsetTimeout(canvasWrapperSize, 10	);\n" + "}\n", this.getElement());
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    public void add(Component... components) {
        wrapper.add(components);
    }

    @ClientCallable
    public void setCanvasWrapperSize(double width, double height) {
        getPresenter().setScreenBounds(width, height);
    }

    @ClientCallable
    public void onMouseStop(double clientX, double clientY) {
        System.out.println("onMouseStop(" + clientX + "," + clientY + ")");
        getPresenter().onMouseStop(clientX, clientY);
    }

    @ClientCallable
    public void onClick(double clientX, double clientY) {
        getPresenter().handleClick(clientX, clientY);
    }

    @ClientCallable
    public void onRightClick(double clientX, double clientY) {
        getPresenter().handleRightClick(clientX, clientY);
    }

    @ClientCallable
    public void onMouseWheel(double wheelDelta) {
        getPresenter().mouseWheel(wheelDelta);
    }

    @ClientCallable
    public void onGesturePinch(double scale) {
        getPresenter().gesturePinch(scale);
    }

    @ClientCallable
    public void onMouseHover(Double offsetX, Double offsetY) {
        getPresenter().handleMouseHover(offsetX, offsetY);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }
}
