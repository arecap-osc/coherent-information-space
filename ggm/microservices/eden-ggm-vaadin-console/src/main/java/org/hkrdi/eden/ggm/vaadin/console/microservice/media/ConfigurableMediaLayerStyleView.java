package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;

public abstract class ConfigurableMediaLayerStyleView extends HorizontalLayout implements FlowView {

    private VerticalLayout verticalWrapper = new VerticalLayout();

    private Grid<MediaLayer> mediaLayerGrid = new Grid<>();

    @Override
    public void buildView() {
        getConfigurableMediaRendererStyleLayerView().setConfigurableMediaLayerStyleView(this);
        buildViewWrappers();
        buildEventListeners();
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public abstract ConfigurableMediaLayerStylePresenter getPresenter();

    public abstract ConfigurableMediaRendererLayerStyleView getConfigurableMediaRendererStyleLayerView();

    private void buildViewWrappers() {
        getStyle().set("min-width", "400px");
        buildMediaLayerGrid();
        getConfigurableMediaRendererStyleLayerView().setEnabled(false);
        verticalWrapper.add(getConfigurableMediaRendererStyleLayerView(), mediaLayerGrid);
        add(verticalWrapper);
    }

    private void buildMediaLayerGrid() {
        mediaLayerGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        mediaLayerGrid.addColumn(new ComponentRenderer<>(
                item ->  {
                	HorizontalLayout hl = new HorizontalLayout(getPresenter().getGridColumnVisibleBtnLayout(item),
                    getPresenter().getGridColumnColorIconLayout(item),
                    getPresenter().getGridColumnTextLayout(item),
                    getPresenter().getGridColumnUpBtnLayout(item),
                    getPresenter().getGridColumnDownBtnLayout(item));
                	hl.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            		return hl;
            		},
                (component, item) -> {
                	HorizontalLayout hl = new HorizontalLayout(getPresenter().getGridColumnVisibleBtnLayout(item),
                    getPresenter().getGridColumnColorIconLayout(item),
                    getPresenter().getGridColumnTextLayout(item),
                    getPresenter().getGridColumnUpBtnLayout(item),
                    getPresenter().getGridColumnDownBtnLayout(item));
                	hl.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            		return hl;
                	}));
    } 

    private void buildEventListeners() {
        mediaLayerGrid.addSelectionListener(getPresenter()::handleMediaLayerSelectedEvent);

    }

    public Grid<MediaLayer> getMediaLayerGrid() {
        return mediaLayerGrid;
    }
}
