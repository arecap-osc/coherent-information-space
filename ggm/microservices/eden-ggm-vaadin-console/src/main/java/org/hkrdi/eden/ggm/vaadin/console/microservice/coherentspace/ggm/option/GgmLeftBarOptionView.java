package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.option;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node.ImportSV1ByPdfSourceView;
import org.hkrdi.eden.ggm.vaadin.console.featureflag.FeatureFlags;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.NodeEdgeSearchDialogView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class GgmLeftBarOptionView extends VerticalLayout implements FlowView {

    @Autowired
    private ImportSV1ByPdfSourceView importSV1ByPdfSourceView;

    @Autowired
    private NodeEdgeSearchDialogView nodeEdgeSearchDialogView;

    @Autowired
    private GgmLeftBarOptionPresenter presenter;

    private Button optionBtn = new Button();
    private Button searchBtn = new Button();

    @Override
    public void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    public GgmLeftBarOptionPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        setSpacing(false);
        setPadding(false);
        FeatureFlags.SV1_MASS_IMPORT_BUTTON.executeIfChecked(()->{
        	setupAndAddIconBtn(optionBtn, VaadinIcon.CLOUD_UPLOAD.create());
        	optionBtn.addClickListener(e -> importSV1ByPdfSourceView.open());
        });
        
        setupAndAddIconBtn(searchBtn, VaadinIcon.SEARCH.create());
        searchBtn.addClickListener(e -> nodeEdgeSearchDialogView.open());
    }

    private void setupAndAddIconBtn(Button btn, Icon icon) {
        icon.setSize("35px");
        btn.setIcon(icon);
        btn.setHeight("35px");
        btn.setWidthFull();
        add(btn);
    }

    public NodeEdgeSearchDialogView getNodeEdgeSearchDialogView() {
        return nodeEdgeSearchDialogView;
    }
}
