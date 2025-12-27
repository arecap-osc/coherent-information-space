package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.edge;

import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.vaadin.console.common.AttachedDocGridComponent;
import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical.FromMapLinkView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@SpringComponent
@UIScope
@Tag("route-form")
@HtmlImport("frontend://vaadin/coherentspace/route-form.html")
public class EdgeView extends PolymerTemplate<TemplateModel> implements FlowEntityView {

    @Autowired
    private EdgePresenter presenter;

    @Autowired
    private FromMapLinkView mapLinkView;

    @Id("wellsComboBox")
    private ComboBox<ApplicationData> wellsComboBox;

    @Id("logicalFunction")
    private Label logicalFunction;

    @Id("currentEdge")
    private Label currentEdge;

    @Id("syntax")
    private TextArea syntax;

    @Id("syntaxDetails")
    private TextArea syntaxDetails;

    @Id("attachDoc")
    private VerticalLayout attachDocLayout;

    @Id("action-buttons")
    private HorizontalLayout actionButtonsLayout;

    @Id("save-btn")
    private Button saveBtn;
    @Id("verb")
    private TextField verb;
    @Id("conjunction")
    private TextArea conjunction;
    @Id("verbalization")
    private TextArea verbalization;
    @Id("phrase")
    private TextArea phrase;
    @Id("verbDetails")
    private TextArea verbDetails;

    @Id("syntaxDisabled")
    private Label syntaxDisabledLabel;

    @Id("mapLinkFormLayout")
    private VerticalLayout mapLinkFormLayout;

    private ComboBox<MapLink> mapLinkComboBox = new ComboBox<>();

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        syntax.addValueChangeListener(e -> getPresenter().handleRouteTextValueChange());
        syntax.addKeyUpListener(e -> getPresenter().handleRouteTextValueChange());
        mapLinkComboBox.setItemLabelGenerator(mapLink -> "( " + mapLink.getFromWord().getLetter() +
                " ) -> ( " + mapLink.getToWord().getLetter() + " )");
        mapLinkComboBox.setAllowCustomValue(false);
        mapLinkComboBox.addValueChangeListener(getPresenter()::handleMapLinkComboBoxValueChange);
        mapLinkFormLayout.add(mapLinkComboBox, mapLinkView);

        wellsComboBox.setAllowCustomValue(false);
        wellsComboBox.setItemLabelGenerator(appData -> appData.getNetwork().replace('_', '/')
                .replace("::", "/") + "/" + appData.getClusterIndex() + "/" + appData.getAddressIndex() + "->" + appData.getToAddressIndex());
        wellsComboBox.addValueChangeListener(getPresenter()::onEdgeWellsComboBoxValueChanged);
    }

    @Override
    public void prepareView() {
        setVisible(getPresenter().getInitialEntityId() != null);
        if (isVisible()) {
            ApplicationData applicationData = getPresenter().getEntity();
            DataMap dataMap = getPresenter().getDataMap();

            Set<ApplicationData> wellsEdges = new HashSet<>();
            wellsEdges.add(applicationData);
            wellsEdges.addAll(getPresenter().getApplicationDataAsEdgeWells().stream()
                    .filter(appData -> getPresenter().wrapApplicationDataLabel(appData).equals(getPresenter().wrapApplicationDataLabel(applicationData)) == false)
                    .filter(DataMapFilterUtil.distinctByKey(appData -> appData.getNetwork().replace('_', '/')
                            .replace("::", "/") + "/" + appData.getClusterIndex() + "/" + appData.getAddressIndex() + "->" + appData.getToAddressIndex()))
                    .collect(Collectors.toSet()));
            wellsComboBox.setItems(wellsEdges);
            wellsComboBox.setValue(applicationData);

            logicalFunction.setText(dataMap.getTrivalentLogic() + " " + dataMap.getTrivalentLogicType().replace('_', ' ') +
                    "->" + dataMap.getToTrivalentLogic() + " " + dataMap.getToTrivalentLogicType().replace('_', ' '));
            currentEdge.setText(applicationData.getNetwork().replace('_', '/').replace("::", "/") + "/" +
                    applicationData.getAddressIndex() + "->" + applicationData.getToAddressIndex());
            getPresenter().handleRouteTextValueChange();
            getPresenter().showMapLinkFormLayoutIfMapLinkPresent();
        }
    }

    @Override
    public void afterPrepareView() {
        if (getPresenter().getInitialEntityId() != null) {
            getAttachDocLayout().setPadding(false);
            getAttachDocLayout().setWidthFull();
            getAttachDocLayout().removeAll();
            getAttachDocLayout().add(new AttachedDocGridComponent(presenter.getApplicationDataIe().getApplicationId(),
                    presenter.getApplicationDataAsEdgeWellsIds(), ApplicationData.class + "_Route", actionButtonsLayout));
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    @Override
    public EdgePresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return saveBtn;
    }

    public VerticalLayout getAttachDocLayout() {
        return attachDocLayout;
    }

    public ComboBox<ApplicationData> getWellsComboBox() {
        return wellsComboBox;
    }

    public Label getLogicalFunction() {
        return logicalFunction;
    }

    public TextArea getSyntax() {
        return syntax;
    }

    public TextArea getSyntaxDetails() {
        return syntaxDetails;
    }

    public HorizontalLayout getActionButtonsLayout() {
        return actionButtonsLayout;
    }

    public TextField getVerb() {
        return verb;
    }

    public TextArea getConjunction() {
        return conjunction;
    }

    public TextArea getVerbalization() {
        return verbalization;
    }

    public TextArea getPhrase() {
        return phrase;
    }

    public TextArea getVerbDetails() {
        return verbDetails;
    }

    public FromMapLinkView getMapLinkView() {
        return mapLinkView;
    }

    public VerticalLayout getMapLinkFormLayout() {
        return mapLinkFormLayout;
    }

    public Label getSyntaxDisabledLabel() {
        return syntaxDisabledLabel;
    }

    public ComboBox<MapLink> getMapLinkComboBox() {
        return mapLinkComboBox;
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
    	FlowEntityView.super.localeChange(event);
    	saveBtn.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));

    	syntax.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.syntax.label"));
        syntaxDetails.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.syntax.details"));
        verb.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.verb"));
        conjunction.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.conjunction"));
        verbalization.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.verbalization"));
        phrase.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.phrase"));
        verbDetails.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.verbdetails"));
    }
}
