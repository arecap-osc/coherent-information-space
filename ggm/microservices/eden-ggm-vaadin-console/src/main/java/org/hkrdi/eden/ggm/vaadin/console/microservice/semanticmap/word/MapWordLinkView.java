package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//TODO
@SpringComponent
@UIScope
public class MapWordLinkView extends VerticalLayout implements FlowView {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapWordLinkView.class);

    @Autowired
    private MapWordLinkPresenter presenter;


    private Grid<MapWord> mapWordGrid = new Grid<>();

    @Override
    public void buildView() {
        customizeView();
        setupMapWordGrid();
    }

    private void customizeView() {
        setVisible(false);
        setWidthFull();
        setPadding(false);
        getStyle().set("margin", "0px");
    }

    private void setupMapWordGrid() {
        mapWordGrid.setSizeFull();
        mapWordGrid.setHeightByRows(true);
        mapWordGrid.addColumn(MapWord::getLetter).setHeader("Litera");
        mapWordGrid.addColumn(MapWord::getWord).setHeader("Semantica");
        mapWordGrid.addThemeNames("column-dividers", "row-stripes");
        mapWordGrid.addSelectionListener(getPresenter()::onMapWordGridSelected);
        add(mapWordGrid);
    }

    public Grid<MapWord> getMapWordGrid() {
        return mapWordGrid;
    }

    @Override
    public MapWordLinkPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

}
