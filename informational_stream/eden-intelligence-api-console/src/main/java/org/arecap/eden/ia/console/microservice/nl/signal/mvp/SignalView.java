package org.arecap.eden.ia.console.microservice.nl.signal.mvp;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.common.i18n.I18NProviderStatic;
import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;
import org.arecap.eden.ia.console.informationalstream.support.bean.Signal;
import org.arecap.eden.ia.console.mvp.component.horizontallayout.HorizontalLayoutGridCrudFlowEntityView;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@SpringComponent
@UIScope
public class SignalView extends HorizontalLayoutGridCrudFlowEntityView<SignalPresenter, Signal> {

    private Grid<Signal> congruenceGrid = new Grid<>(Signal.class);

    private Grid<Signal> similarityGrid = new Grid<>(Signal.class);

    private H4 h4Congruence = new H4("signalView.h4Congruence");

    private H4 h4Similarity = new H4("signalView.h4Similarity");

    private TextField filter = new TextField();

    private ComboBox<StreamApplicationType> streamApplicationTypeFilter = new ComboBox<>("", StreamApplicationType.values());
    private ComboBox<Locale> i18nFilter = new ComboBox<>("", Locale.ENGLISH, new Locale("RO"));


    private ComboBox<StreamApplicationType> streamApplicationType = new ComboBox<>("textfield.streamApplicationType.label", StreamApplicationType.values());
    private TextField featurePart = new TextField("textfield.featurePart.label");
    private TextArea details = new TextArea("textarea.details.label");

    private ComboBox<Signal> i18nComboBox = new ComboBox<>("I18n");

    private ComboBox<Signal> congruenceComboBox = new ComboBox<>("combobox.signal.congruence.label");

    private ComboBox<Signal> similarityComboBox = new ComboBox<>("combobox.signal.similarity.label");



    private VerticalLayout editorFields = new VerticalLayout(streamApplicationType, featurePart, details, i18nComboBox, congruenceComboBox,
            similarityComboBox);


    @Override
    public void localeChange(LocaleChangeEvent event) {
        super.localeChange(event);
        refreshCombos();
        getPresenter().setLocale(event.getLocale());
    }

    private void refreshCombos() {
        streamApplicationTypeFilter.getDataProvider().refreshAll();
        streamApplicationType.getDataProvider().refreshAll();
    }

    @Override
    public void buildView() {
        buildViewWrapper();
        buildEventListeners();
    }

    private void buildViewWrapper() {
        setSizeFull();
        getStyle().set("overflow", "auto");
        setEditorFormVisibility(false);
        getSaveButton().getElement().getThemeList().add("primary");

        getEditorForm().addComponentAsFirst(editorFields);
        getEditorForm().add(h4Congruence, congruenceGrid, h4Similarity, similarityGrid);

        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        getCrudGrid().setColumns("id", "featurePart");
        getCrudGrid().getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        getCrudGrid().addColumn(new ComponentRenderer<>(
                signal -> new Label(I18NProviderStatic
                        .getTranslation(signal.getStreamApplicationType().name(), signal.getLocale()))
        ));
        getCrudGrid().addColumn("locale");

        congruenceGrid.setColumns("id", "featurePart");
        congruenceGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        congruenceGrid.addColumn(new ComponentRenderer<>(
                signal -> new Label(I18NProviderStatic
                        .getTranslation(signal.getStreamApplicationType().name(), signal.getLocale()))
        ));
        congruenceGrid.addColumn("locale");
        congruenceGrid.setHeightByRows(true);
        similarityGrid.setColumns("id", "featurePart");
        similarityGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        similarityGrid.addColumn(new ComponentRenderer<>(
                signal -> new Label(I18NProviderStatic
                        .getTranslation(signal.getStreamApplicationType().name(), signal.getLocale()))
        ));
        similarityGrid.addColumn("locale");
        similarityGrid.setHeightByRows(true);

        streamApplicationTypeFilter.setAllowCustomValue(false);
        streamApplicationTypeFilter.setItemLabelGenerator(sat -> I18NProviderStatic.getTranslation(sat.name()));
        streamApplicationTypeFilter.getStyle().set("min-width","250px");

        HorizontalLayout gridActionsAddons = new HorizontalLayout(filter, streamApplicationTypeFilter, i18nFilter);
        getGridActions().addComponentAsFirst(gridActionsAddons);

        streamApplicationType.setAllowCustomValue(false);
        streamApplicationType.setItemLabelGenerator(sat -> I18NProviderStatic.getTranslation(sat.name()));
        streamApplicationType.setWidthFull();
        featurePart.setWidthFull();
        details.setWidthFull();

        i18nComboBox.setItemLabelGenerator(Signal::getFeaturePart);
        i18nComboBox.setAllowCustomValue(false);
        i18nComboBox.setWidthFull();

        congruenceComboBox.setItemLabelGenerator(Signal::getFeaturePart);
        congruenceComboBox.setAllowCustomValue(false);
        congruenceComboBox.setWidthFull();

        similarityComboBox.setItemLabelGenerator(Signal::getFeaturePart);
        similarityComboBox.setAllowCustomValue(false);
        similarityComboBox.setWidthFull();


    }

    private void buildEventListeners() {
        filter.addValueChangeListener(this::searchTextChangedFired);
        i18nComboBox.addValueChangeListener(this::i18nValueChanged);
        congruenceComboBox.addValueChangeListener(this::congruenceValueChanged);
        similarityComboBox.addValueChangeListener(this::similarityValueChanged);
        streamApplicationTypeFilter.addValueChangeListener(this::streamApplicationTypeFilterValueChanged);
        i18nFilter.addValueChangeListener(this::i18nFilterValueChanged);
    }

    private void i18nFilterValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Locale>, Locale> comboBoxLocaleComponentValueChangeEvent) {
        setItems(getPresenter().getItems());
    }

    private void streamApplicationTypeFilterValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<StreamApplicationType>, StreamApplicationType> comboBoxStreamApplicationTypeComponentValueChangeEvent) {
        setItems(getPresenter().getItems());
    }

    private void searchTextChangedFired(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        setItems(getPresenter().getItems());
    }

    private void similarityValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Signal>, Signal> comboBoxSignalComponentValueChangeEvent) {
        getPresenter().setSimilarityBean(Optional.ofNullable(comboBoxSignalComponentValueChangeEvent.getValue()));
    }

    private void congruenceValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Signal>, Signal> comboBoxSignalComponentValueChangeEvent) {
        getPresenter().setCongruenceBean(Optional.ofNullable(comboBoxSignalComponentValueChangeEvent.getValue()));
    }

    private void i18nValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Signal>, Signal> comboBoxSignalComponentValueChangeEvent) {
        getPresenter().setI8nBean(Optional.ofNullable(comboBoxSignalComponentValueChangeEvent.getValue()));

    }

    public void setI18nComboItems(Collection<Signal> collection) {
        i18nComboBox.setItems(collection);
    }

    public void setI18nValue(Optional<Signal> signal) {
        i18nComboBox.clear();
        if(signal.isPresent()) {
            i18nComboBox.setValue(signal.get());
        }
    }

    public void setCongruenceComboItems(Collection<Signal> collection) {
        congruenceComboBox.setItems(collection);
    }

    public void setCongruenceItems(Collection<Signal> collection) {
        congruenceGrid.setItems(collection);
    }

    public void setCongruenceValue(Optional<Signal> signal) {
        congruenceComboBox.clear();
        if(signal.isPresent()) {
            congruenceComboBox.setValue(signal.get());
        }
    }

    public void setSimilarityComboItems(Collection<Signal> collection) {
        similarityComboBox.setItems(collection);
    }

    public void setSimilarityItems(Collection<Signal> collection) {
        similarityGrid.setItems(collection);
    }


    public void setSimilarityValue(Optional<Signal> signal) {
        similarityComboBox.clear();
        if(signal.isPresent()) {
            similarityComboBox.setValue(signal.get());
        }
    }

    @Override
    public void setEditorFormVisibility(Boolean visible) {
        super.setEditorFormVisibility(visible);
        congruenceGrid.setVisible(visible);
        similarityGrid.setVisible(visible);
    }

    public String getSearchText() {
        return filter.getValue().trim();
    }

    public Optional<StreamApplicationType> getStreamApplicationTypeFilter() {
        return streamApplicationTypeFilter.getOptionalValue();
    }

    public Optional<Locale> getI18nFilter() {
        return i18nFilter.getOptionalValue();
    }

}
