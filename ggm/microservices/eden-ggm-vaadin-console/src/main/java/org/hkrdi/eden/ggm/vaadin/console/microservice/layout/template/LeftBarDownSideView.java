package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class LeftBarDownSideView extends VerticalLayout implements FlowView {

    @Autowired
    private LeftBarDownSidePresenter presenter;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public LeftBarDownSidePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        getStyle().set("position", "relative");
        setSpacing(false);
        setPadding(false);
        setWidthFull();
        setHeightFull();
    }

}
