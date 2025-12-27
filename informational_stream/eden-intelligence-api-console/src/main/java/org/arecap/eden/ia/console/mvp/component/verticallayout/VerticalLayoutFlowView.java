package org.arecap.eden.ia.console.mvp.component.verticallayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.arecap.eden.ia.console.mvp.FlowPresenter;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class VerticalLayoutFlowView<P extends FlowPresenter> extends VerticalLayout implements FlowView<P> {

    @Autowired
    private P presenter;


    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

}
