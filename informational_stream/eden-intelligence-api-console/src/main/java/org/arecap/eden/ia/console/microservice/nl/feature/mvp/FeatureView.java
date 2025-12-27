package org.arecap.eden.ia.console.microservice.nl.feature.mvp;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;
import org.arecap.eden.ia.console.mvp.component.verticallayout.VerticalLayoutGridCrudFlowEntityView;

import java.util.Collection;
import java.util.Optional;


@SpringComponent
@UIScope
public class FeatureView extends VerticalLayoutGridCrudFlowEntityView<FeaturePresenter, Feature> {

    private TextField filter = new TextField();

    private TextArea content = new TextArea("textarea.details.label");

    private ComboBox<Feature> i18nComboBox = new ComboBox<>("I18n");

    private VerticalLayout editorFields = new VerticalLayout(content, i18nComboBox);

    @Override
    public void buildView() {
        setEditorFormVisibility(false);
        getGridActions().addComponentAsFirst(filter);
        getEditorForm().addComponentAsFirst(editorFields);
        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        getCrudGrid().setColumns("id", "content");
        getCrudGrid().getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        content.setWidthFull();

        i18nComboBox.setItemLabelGenerator(Feature::getContent);
        i18nComboBox.setAllowCustomValue(false);
        i18nComboBox.setWidthFull();
        filter.addValueChangeListener(this::searchTextChangedFired);
        i18nComboBox.addValueChangeListener(this::i18nValueChanged);

    }

    private void i18nValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Feature>, Feature> comboBoxFeatureComponentValueChangeEvent) {
        getPresenter().setI8nBean(Optional.of(comboBoxFeatureComponentValueChangeEvent.getValue()));
    }

    private void searchTextChangedFired(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        setItems(getPresenter().getItems());
    }

    public void setI18nComboItems(Collection<Feature> collection) {
        i18nComboBox.setItems(collection);
    }

    public String getSearchText() {
        return filter.getValue().trim();
    }

}
