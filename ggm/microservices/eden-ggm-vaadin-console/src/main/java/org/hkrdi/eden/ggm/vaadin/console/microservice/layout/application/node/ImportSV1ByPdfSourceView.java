package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.common.UploadWithSecurityContext;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringComponent
@UIScope
public class ImportSV1ByPdfSourceView extends Dialog implements FlowView {

    private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload upload = new UploadWithSecurityContext(SecurityContextHolder.getContext(), buffer);

    private Button uploadFileButton = new Button(VaadinIcon.UPLOAD.create());

    @Autowired
    private ImportSV1ByPdfSourcePresenter presenter;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public ImportSV1ByPdfSourcePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        upload.setDropAllowed(true);
        upload.setUploadButton(uploadFileButton);
        upload.addSucceededListener(getPresenter()::fileUploadSucceeded);
        add(upload);
//        HorizontalLayout actionButtonsWrapper = new HorizontalLayout();
//        actionButtonsWrapper.add(doUploadButton, cancelButton);
//        add(actionButtonsWrapper);
//        doUploadButton.addClickListener(getPresenter()::doUpload);
//        cancelButton.addClickListener(e -> close());

    }

    public MultiFileMemoryBuffer getBuffer() {
        return buffer;
    }

    public Upload getUpload() {
        return upload;
    }

//    public Button getDoUploadButton() {
//        return doUploadButton;
//    }
}
