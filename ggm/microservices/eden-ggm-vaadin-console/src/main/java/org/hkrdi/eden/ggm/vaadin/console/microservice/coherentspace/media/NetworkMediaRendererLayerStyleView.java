package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.ConfigurableMediaRendererLayerStyleView;

public abstract class NetworkMediaRendererLayerStyleView extends ConfigurableMediaRendererLayerStyleView {

    private Checkbox addressIndexCheckBox = new Checkbox("networkmediarenderer.checkbox.addressindex.label");
    private Checkbox trivalentLogicCheckBox = new Checkbox("networkmediarenderer.checkbox.trivalentlogic.label");
    private Checkbox clusterIndexCheckBox = new Checkbox("networkmediarenderer.checkbox.clusterindex.label");
    private Checkbox applicationDataCheckBox = new Checkbox("networkmediarenderer.checkbox.applicationdata.label");

    @Override
    public void buildView() {
        super.buildView();
        buildViewWrappers();
        buildEventListeners();
    }

    @Override
    public abstract NetworkMediaRendererLayerStylePresenter getPresenter();

    private void buildViewWrappers() {
        HorizontalLayout nodeCheckboxes = new HorizontalLayout(addressIndexCheckBox, trivalentLogicCheckBox);
        HorizontalLayout clusterCheckboxes = new HorizontalLayout(clusterIndexCheckBox, applicationDataCheckBox);
        add(nodeCheckboxes, clusterCheckboxes);
    }

    private void buildEventListeners() {
        addressIndexCheckBox.addValueChangeListener(getPresenter()::handleAddressIndexValueChanged);
        trivalentLogicCheckBox.addValueChangeListener(getPresenter()::handleTrivalentLogicValueChanged);
        clusterIndexCheckBox.addValueChangeListener(getPresenter()::handleClusterIndexValueChanged);
        applicationDataCheckBox.addValueChangeListener(getPresenter()::handleApplicationDataValueChanged);
    }

    public Checkbox getAddressIndexCheckBox() {
        return addressIndexCheckBox;
    }

    public Checkbox getTrivalentLogicCheckBox() {
        return trivalentLogicCheckBox;
    }

    public Checkbox getClusterIndexCheckBox() {
        return clusterIndexCheckBox;
    }

    public Checkbox getApplicationDataCheckBox() {
        return applicationDataCheckBox;
    }
}
