package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.common.AttachedDocGridComponent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ReadEdgeView extends VerticalLayout implements FlowView {

    @Autowired
    private ReadEdgePresenter presenter;

    private Label bredCrumb = new Label();

    private Label logicalFunction = new Label();

    private HorizontalLayout fakeUploadActionButtonLayout = new HorizontalLayout();

    private VerticalLayout attachDocLayout = new VerticalLayout();

    private TextArea semantic = new TextArea("Nota explicativa");

    @Override
    public void buildView() {
        setSizeFull();
        semantic.setReadOnly(true);
        attachDocLayout.setSizeFull();
        attachDocLayout.getStyle().set("overflow", "auto");
        attachDocLayout.setPadding(false);
        attachDocLayout.setSpacing(false);
        add(bredCrumb, logicalFunction, semantic, attachDocLayout);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public void prepareView() {
        setVisible(getPresenter().getInitialEntityId() != null);
        if(isVisible()) {
            ApplicationData appData = getPresenter().getEntity();
            DataMap dataMap = getPresenter().getDataMap();
            bredCrumb.setText(appData.getNetwork().replace('_', '/').replace("::", "/") + "/" + appData.getAddressIndex());
            logicalFunction.setText(dataMap.getTrivalentLogic() + " " + dataMap.getTrivalentLogicType().replace('_', ' '));
        }

    }

    @Override
    public void afterPrepareView() {
        if(getPresenter().getInitialEntityId() != null) {
            attachDocLayout.removeAll();
            AttachedDocGridComponent attachedDocGridComponent = new AttachedDocGridComponent(getPresenter().getEntity().getApplication().getId(),
                    getPresenter().getApplicationDataAsNodeWellsIds(), ApplicationData.class, fakeUploadActionButtonLayout);
            attachedDocGridComponent.setActive(false);
            attachDocLayout.add(attachedDocGridComponent);
        }
    }

    @Override
    public ReadEdgePresenter getPresenter() {
        return presenter;
    }
}
