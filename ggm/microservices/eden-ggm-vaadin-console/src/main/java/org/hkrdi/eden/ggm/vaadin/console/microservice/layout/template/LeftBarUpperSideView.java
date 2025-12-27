package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class LeftBarUpperSideView extends VerticalLayout implements FlowView {

    @Autowired
    private LeftBarUpperSidePresenter presenter;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public LeftBarUpperSidePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        getStyle().set("position", "relative");
        setSpacing(false);
        setPadding(false);
        setWidthFull();
    }

}
