package org.arecap.eden.ia.console.mvp.component.verticallayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.arecap.eden.ia.console.mvp.FlowEntityPresenter;
import org.arecap.eden.ia.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class VerticalLayoutFlowEntityView<P extends FlowEntityPresenter> extends VerticalLayout implements FlowEntityView<P, VerticalLayout> {

    @Autowired
    private P presenter;

    private Button save = new Button("button.save.label", VaadinIcon.CHECK.create());

    private VerticalLayout editorForm = new VerticalLayout();

    @PostConstruct
    public void init() {
        setSizeFull();
        getStyle().set("overflow", "auto");
        editorForm.setWidthFull();
        editorForm.getStyle().set("overflow", "auto");
        add(editorForm);
        FlowEntityView.super.init();
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    public VerticalLayout getEditorForm() {
        return editorForm;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

}
