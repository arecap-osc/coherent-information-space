package org.hkrdi.eden.ggm.vaadin.console.microservice.common.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class AttachedDocumentsDialogView extends Dialog implements FlowView {
    private Label label = new Label();

    private VerticalLayout attachmentsLayout = new VerticalLayout();

    @Autowired
    private AttachedDocumentsDialogPresenter presenter;

    @Override
    public void buildView() {
        setWidth("400px");
        setHeight("200px");
        attachmentsLayout.setWidthFull();
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        label.getStyle().set("font-weight", "bold");
        add(label, attachmentsLayout);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public AttachedDocumentsDialogPresenter getPresenter() {
        return presenter;
    }

    public void open(Optional<ApplicationData> applicationDataOpt) {
        super.open();
        if (applicationDataOpt.isPresent()) {
            getPresenter().setupAttachmentLayout(applicationDataOpt.get());
            return;
        }
    }

    public Label getLabel() {
        return label;
    }

    public VerticalLayout getAttachmentsLayout() {
        return attachmentsLayout;
    }

}
