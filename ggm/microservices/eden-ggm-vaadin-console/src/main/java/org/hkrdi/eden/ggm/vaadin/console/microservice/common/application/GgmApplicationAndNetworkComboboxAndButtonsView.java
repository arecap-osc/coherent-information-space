package org.hkrdi.eden.ggm.vaadin.console.microservice.common.application;

import org.hkrdi.eden.ggm.vaadin.console.common.DownloadWithSecurityContext;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ImportApplicationDialogView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class GgmApplicationAndNetworkComboboxAndButtonsView extends ApplicationView {


    private Button importApplicationButton = new Button(VaadinIcon.UPLOAD.create());

    private ComboBox<String> coherentSpaceNetworkComboBox = new ComboBox<>();

    private Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());

    private DownloadWithSecurityContext download = new DownloadWithSecurityContext();

    @Autowired
    private GgmApplicationAndNetworkComboboxAndButtonsPresenter presenter;

    @Autowired
    private ImportApplicationDialogView importApplicationDialogView;

    @Override
    public GgmApplicationAndNetworkComboboxAndButtonsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        super.buildView();
        coherentSpaceNetworkComboBox.setWidth("250px");
        importApplicationButton.getElement().setAttribute("title", "tooltip.importapplication.button");
        coherentSpaceNetworkComboBox.getElement().setAttribute("title", "tooltip.currentnetwork.combobox");

        getWrapperLayout().add(importApplicationButton, coherentSpaceNetworkComboBox);
        importApplicationButton.addClickListener(e -> importApplicationDialogView.open(this));
        coherentSpaceNetworkComboBox.addValueChangeListener(getPresenter()::handleCoherentSpaceNetworkComboBoxValueChange);
        downloadButton.getElement().setAttribute("title", "tooltip.exportapplication.button");
        download = new DownloadWithSecurityContext(downloadButton);
        add(download);
    }

    @Override
    public void afterPrepareView() {
        super.afterPrepareView();
        if (coherentSpaceNetworkComboBox.getOptionalValue().isPresent() == false) {
            getPresenter().coherentSpaceNetworkComboBoxSetItemsAndSelectValue();
        }
    }

    public ComboBox<String> getCoherentSpaceNetworkComboBox() {
        return coherentSpaceNetworkComboBox;
    }


    public DownloadWithSecurityContext getDownload() {
        return download;
    }

}
