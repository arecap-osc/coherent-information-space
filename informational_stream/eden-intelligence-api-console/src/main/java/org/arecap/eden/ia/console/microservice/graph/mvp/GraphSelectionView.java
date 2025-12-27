package org.arecap.eden.ia.console.microservice.graph.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.InformationalStreamGraphDisplayOptionView;
import org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.InformationalStreamGraphView;
import org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot.MandelbrotSetGraphDisplayOptionView;
import org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot.MandelbrotSetGraphView;
import org.arecap.eden.ia.console.mvp.component.horizontallayout.HorizontalLayoutFlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GraphSelectionView extends HorizontalLayoutFlowView<GraphSelectionPresenter> {

    @Autowired
    private MandelbrotSetGraphView mandelbrotSetGraphView;

    @Autowired
    private MandelbrotSetGraphDisplayOptionView mandelbrotSetGraphScaleView;


    @Autowired
    private InformationalStreamGraphView informationalStreamGraphView;

    @Autowired
    private InformationalStreamGraphDisplayOptionView informationalStreamGraphDisplayOptionView;

    @Override
    public void buildView() {
        setSizeFull();
        setSpacing(false);
        setPadding(false);
        mandelbrotSetGraphView.add(mandelbrotSetGraphScaleView);
        informationalStreamGraphView.add(informationalStreamGraphDisplayOptionView);
    }

    public void displayInformationalStreamGraphView() {
        if(mandelbrotSetGraphView.getParent().isPresent()) {
            remove(mandelbrotSetGraphView);
        }
        add(informationalStreamGraphView);
    }

    public void displayMandelbrotSetGraphView() {
        if(informationalStreamGraphView.getParent().isPresent()) {
            remove(informationalStreamGraphView);
        }
        add(mandelbrotSetGraphView);
    }

    public void displayNone(){
        if(mandelbrotSetGraphView.getParent().isPresent()) {
            remove(mandelbrotSetGraphView);
        }
        if(informationalStreamGraphView.getParent().isPresent()) {
            remove(informationalStreamGraphView);
        }
    }

}
