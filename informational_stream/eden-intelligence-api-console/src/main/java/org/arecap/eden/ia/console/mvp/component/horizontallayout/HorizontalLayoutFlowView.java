package org.arecap.eden.ia.console.mvp.component.horizontallayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.arecap.eden.ia.console.mvp.FlowPresenter;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HorizontalLayoutFlowView<P extends FlowPresenter> extends HorizontalLayout implements FlowView<P> {

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
