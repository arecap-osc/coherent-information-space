package org.arecap.eden.ia.console.microservice.nl.feature.mvp;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.FeatureStream;
import org.arecap.eden.ia.console.informationalstream.support.bean.Signal;
import org.arecap.eden.ia.console.mvp.component.verticallayout.VerticalLayoutGridCrudFlowEntityView;

import java.util.Collection;

@SpringComponent
@UIScope
public class FeatureStreamView extends VerticalLayoutGridCrudFlowEntityView<FeatureStreamPresenter, FeatureStream> {

    private ComboBox<Signal> upstream = new ComboBox<>("combobox.feturestream.upstream.label");

    private ComboBox<Signal> downstream = new ComboBox<>("combobox.feturestream.downstream.label");

    private VerticalLayout editorFields = new VerticalLayout(upstream, downstream);

    @Override
    public void buildView() {
        setEditorFormVisibility(false);
        getEditorForm().addComponentAsFirst(editorFields);
        getCrudGrid().setColumns("upstream.featurePart", "upstream.streamApplicationType", "downstream.featurePart", "downstream.streamApplicationType", "topology");
        upstream.setItemLabelGenerator(item -> item.getFeaturePart());
        downstream.setItemLabelGenerator(item -> item.getFeaturePart());
    }

    public void setUpstreamItems(Collection<Signal> collection) {
        upstream.setItems(collection);
    }

    public void setDownstreamItems(Collection<Signal> collection) {
        downstream.setItems(collection);
    }

}
