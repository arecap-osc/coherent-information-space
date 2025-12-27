package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.common.UploadWithSecurityContext;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.application.GgmApplicationAndNetworkComboboxAndButtonsPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@UIScope
@SpringComponent
public class ImportApplicationDialogView extends Dialog implements FlowView {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportApplicationDialogView.class);

	private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
	private Upload upload = new UploadWithSecurityContext(SecurityContextHolder.getContext(), buffer);

	private Button uploadFileButton = new Button(VaadinIcon.UPLOAD.create());
	private ComboBox<SemanticMap> semanticMapComboBox = new ComboBox<>();

	@Autowired
	private ImportApplicationDialogPresenter presenter;

	@Override
	public void onAttach(AttachEvent attachEvent) {
		FlowView.super.onAttach(attachEvent);
	}

	@Override
	public ImportApplicationDialogPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void buildView() {
		upload.setDropAllowed(true);
		upload.setUploadButton(uploadFileButton);
		upload.addSucceededListener(getPresenter()::fileUploadSucceeded);
		semanticMapComboBox.setItemLabelGenerator(SemanticMap::getLabel);
		semanticMapComboBox.setAllowCustomValue(false);
		add(upload, semanticMapComboBox);
	}

	public void open(ApplicationView applicationView) {
		LOGGER.info("User clicked the import button");
		getPresenter().setApplicationPresenter(applicationView.getPresenter());
		open();
	}

	public MultiFileMemoryBuffer getBuffer() {
		return buffer;
	}

	public Upload getUpload() {
		return upload;
	}

	public ComboBox<SemanticMap> getSemanticMapComboBox() {
		return semanticMapComboBox;
	}

}
