package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.common.AttachedDocGridComponent;
import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.common.i18n.flow.InternationalizeViewEngine;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Tag("node-form")
@HtmlImport("frontend://vaadin/coherentspace/node-form.html")
public abstract class AbstractNodeView extends PolymerTemplate<TemplateModel> implements FlowEntityView {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNodeView.class);

    @Id("action-buttons")
    private HorizontalLayout actionButtonsLayout;

    @Id("save-btn")
    private Button button;

    @Id("semantic")
    private TextArea semantic;

    @Id("wellsComboBox")
    private ComboBox<ApplicationData> wellsComboBox;

    @Id("attachDoc")
    private VerticalLayout attachDocLayout;

    @Id("logicalFunction")
    private Label logicalFunction;

    @Id("currentNode")
    private Label currentNode;

    @Id("semanticDetails")
    private TextArea semanticDetails;

    @Id("semanticDisabled")
    private Label semanticDisabledLabel;

    @Override
    public Button getSaveButton() {
        return button;
    }

    @Override
    public abstract ApplicationDataPresenter getPresenter();

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        semantic.addValueChangeListener(e -> handleNodeTextValueChange());
        semantic.addKeyUpListener(e -> handleNodeTextValueChange());
        wellsComboBox.addValueChangeListener(getPresenter()::onNodeWellsComboBoxValueChanged);
        wellsComboBox.setAllowCustomValue(false);
        wellsComboBox.setItemLabelGenerator(appData -> getPresenter().wrapApplicationDataLabel(appData));
    }

    @Override
    public void prepareView() {
        setVisible(getPresenter().getInitialEntityId() != null);
        if (isVisible()) {
            ApplicationData applicationData = getPresenter().getEntity();
            DataMap dataMap = getPresenter().getDataMap();

            Set<ApplicationData> wellsNodes = new HashSet<>();
            wellsNodes.add(applicationData);
            wellsNodes.addAll(getPresenter().getApplicationDataAsNodeWells().stream()
                    .filter(appData -> getPresenter().wrapApplicationDataLabel(appData).equals(getPresenter().wrapApplicationDataLabel(applicationData)) == false)
                    .filter(DataMapFilterUtil.distinctByKey(appData -> getPresenter().wrapApplicationDataLabel(appData)))
                    .collect(Collectors.toSet()));

            wellsComboBox.setItems(wellsNodes);
            wellsComboBox.setValue(applicationData);

            logicalFunction.setText(dataMap.getTrivalentLogic() + " " + dataMap.getTrivalentLogicType().replace('_', ' '));
            currentNode.setText(applicationData.getNetwork().replace('_', '/').replace("::", "/") + "/" +
                    applicationData.getClusterIndex() + "/" + applicationData.getAddressIndex());
            handleNodeTextValueChange();
        }
    }

    @Override
    public void afterPrepareView() {
        semantic.setWidthFull();
        semanticDetails.setWidthFull();
        if (getPresenter().getInitialEntityId() != null) {
            getAttachDocLayout().setPadding(false);
            getAttachDocLayout().setWidthFull();
            getAttachDocLayout().removeAll();
            getAttachDocLayout().add(new AttachedDocGridComponent(getPresenter().getEntity().getApplication().getId(),
                    getPresenter().getApplicationDataAsNodeWellsIds(), ApplicationData.class, actionButtonsLayout));
        }
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    protected abstract void handleNodeTextValueChange();

    public TextArea getSemantic() {
        return semantic;
    }

    public HorizontalLayout getActionButtonsLayout() {
        return actionButtonsLayout;
    }

    public VerticalLayout getAttachDocLayout() {
        return attachDocLayout;
    }

    public TextArea getSemanticDetails() {
        return semanticDetails;
    }

    public void setSemantic(TextArea semantic) {
        this.semantic = semantic;
    }

    public Label getSemanticDisabledLabel() {
        return semanticDisabledLabel;
    }

    public Label getCurrentNode() {
        return currentNode;
    }

    public Label getLogicalFunction() {
        return logicalFunction;
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
    	FlowEntityView.super.localeChange(event);
    	button.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
    	semantic.setLabel(GgmI18NProviderStatic.getTranslation("msg.coherentspace.node.form.semantic"));
    	semanticDetails.setLabel(GgmI18NProviderStatic.getTranslation("msg.coherentspace.node.form.details"));
    }
}
