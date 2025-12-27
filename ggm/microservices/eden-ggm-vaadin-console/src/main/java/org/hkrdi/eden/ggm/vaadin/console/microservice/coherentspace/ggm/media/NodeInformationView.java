package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class NodeInformationView extends HorizontalLayout implements FlowView {
    @Autowired
    private NodeInformationPresenter presenter;

    private TextArea textArea = new TextArea();

    @Override
    public void buildView() {
        setEnabled(false);
        setVisible(false);
        setWidth("250px");
        setHeight("80px");
        getStyle().set("position", "absolute");
        getStyle().set("overflow", "hidden");
        getStyle().set("background-color", "#ffe49c");
        getStyle().set("z-index", "1100");

        textArea.setReadOnly(true);
        textArea.setSizeFull();
        add(textArea);
    }

    @Override
    public NodeInformationPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    public TextArea getTextArea() {
        return textArea;
    }
}
