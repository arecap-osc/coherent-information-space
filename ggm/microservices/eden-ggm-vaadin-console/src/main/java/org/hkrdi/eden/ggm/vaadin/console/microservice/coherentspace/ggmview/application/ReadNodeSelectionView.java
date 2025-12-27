package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewOptionalPaneView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentPresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ReadNodeSelectionView extends LeftSideContentView {

    @Autowired
    private ReadNodeSelectionPresenter presenter;

    @Autowired
    private ReadNodeView readNodeView;

    private HorizontalLayout wrapper = new HorizontalLayout();

    @Override
    public void buildView() {
        super.buildView();
        getStyle().set("padding-right","10px");
        getStyle().set("padding-top","50px");

        wrapper.setSizeFull();
        wrapper.getStyle().set("overflow", "hidden");
        add(wrapper);
    }

    @Override
    public LeftSideContentPresenter getPresenter() {
        return presenter;
    }

    public void setReadNodeView(ReadNodeView readNodeView) {
        this.readNodeView = readNodeView;
    }

    public ReadNodeView getReadNodeView() {
        return readNodeView;
    }

    public HorizontalLayout getWrapper() {
        return wrapper;
    }

    public void setStyleForRoadFractoloniView(RoadFractoloniViewOptionalPaneView paneView) {
        getStyle().set("margin-left", (new Integer(paneView.getWidth().replace("px", "")) - 5) + "px");
    }
}
