package org.arecap.eden.ia.console.microservice.graph.mvp;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.mvp.component.horizontallayout.HorizontalLayoutFlowView;

import java.util.Optional;

@SpringComponent
@UIScope
public class GraphSelectorView extends HorizontalLayoutFlowView<GraphSelectorPresenter>  {


    public static String Mandelbrot_Set_Graph = "Mandelbrot set";

    public static String Informational_Stream_Graph = "Informational Stream graph";

    private ComboBox<String> graphSelector = new ComboBox<>("", Mandelbrot_Set_Graph, Informational_Stream_Graph);


    @Override
    public void buildView() {
        add(graphSelector);
        graphSelector.setAllowCustomValue(false);
        graphSelector.addValueChangeListener(this::graphSelectionChanged);
    }

    private void graphSelectionChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
        getPresenter().publishGraphSelection(Optional.ofNullable(comboBoxStringComponentValueChangeEvent.getValue()));
    }

}
