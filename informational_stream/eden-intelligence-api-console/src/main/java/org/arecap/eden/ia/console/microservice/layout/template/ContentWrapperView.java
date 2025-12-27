package org.arecap.eden.ia.console.microservice.layout.template;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ContentWrapperView extends VerticalLayout implements FlowView<ContentWrapperPresenter> {

    @Autowired
    private ContentWrapperPresenter presenter;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public ContentWrapperPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        setSpacing(false);
        setPadding(false);
        getStyle().set("position", "relative");
        getStyle().set("overflow", "hidden");
        setSizeFull();
    }

}
