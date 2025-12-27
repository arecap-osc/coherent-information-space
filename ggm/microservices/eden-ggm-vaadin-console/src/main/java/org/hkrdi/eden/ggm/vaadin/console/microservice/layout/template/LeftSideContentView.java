package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;

public abstract class LeftSideContentView extends HorizontalLayout implements FlowView {

    private Div resizer = new Div();

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    public abstract LeftSideContentPresenter getPresenter();

    @Override
    public void buildView() {
        setPadding(false);
        setSpacing(false);
        getStyle().set("position", "absolute");
        getStyle().set("background", "white");
        getStyle().set("z-index", "100");
        getStyle().set("top", "0px");
        getStyle().set("bottom", "0px");
        getStyle().set("border-right", "1px solid black");
        setupResizer();
        UI.getCurrent().getPage()
                .executeJavaScript("$0.addEventListener('mousedown', function(e) {\n" +
                                "      e.preventDefault();\n" +
                                "      window.addEventListener('mousemove', resize);\n" +
                                "      window.addEventListener('mouseup', stopResize);\n" +
                                "    }); " + getJsResizeFn() + getJsStopResizeFn(),
                        resizer.getElement(), this.getElement());
    }

    public void reset() {
        UI.getCurrent().getPage()
                .executeJavaScript("$0.style.width = null;", this.getElement());
    }

    private void setupResizer() {
        resizer.setWidth("20px");
        resizer.setHeight("20px");
        resizer.getStyle().set("position", "absolute");
        resizer.getStyle().set("background", "white");
        resizer.getStyle().set("border-radius", "50%");
        resizer.getStyle().set("border", "3px solid #4286f4");
        resizer.getStyle().set("cursor", "nwse-resize");
        resizer.getStyle().set("right", "-13px");
        resizer.getStyle().set("top", "50px");
        add(resizer);
    }


    private String getJsResizeFn() {
        return "function resize(e) {\n" +
                "   \t$1.style.width = e.pageX - $1.getBoundingClientRect().left + 'px'\n" +
                "}";
    }

    private String getJsStopResizeFn() {
        return "    function stopResize() {\n" +
                "       \twindow.removeEventListener('mousemove', resize);\n" +
                "       \twindow.removeEventListener('mouseup', stopResize);\n" +
                "    }";
    }

}
