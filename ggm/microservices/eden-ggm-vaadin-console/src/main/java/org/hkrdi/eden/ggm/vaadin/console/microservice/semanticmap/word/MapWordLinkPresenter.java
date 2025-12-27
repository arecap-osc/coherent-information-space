package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapLinkSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapWordSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapLinkRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapWordRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.ArrayList;
import java.util.Optional;

@SpringComponent
@UIScope
public class MapWordLinkPresenter extends DefaultFlowPresenter<MapWord, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapWordLinkPresenter.class);

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Autowired
    private MapWordRepositoryService service;

    @Autowired
    private MapLinkRepositoryService mapLinkRepositoryService;

    @Override
    public MapWordLinkView getView() {
        return (MapWordLinkView)super.getView();
    }

    @Override
    public MapWordRepositoryService getService() {
        return service;
    }

    public void onMapWordGridSelected(SelectionEvent<Grid<MapWord>, MapWord> gridMapWordSelectionEvent) {
        semanticMapIe.setSecondMapWordId(gridMapWordSelectionEvent.getFirstSelectedItem().map(MapWord::getId).orElse(null));
        LOGGER.info("Semantic Map Link selected");
        getUIEventBus().publish(this, new MapLinkSelectionChangeEvent(gridMapWordSelectionEvent.getFirstSelectedItem()));
    }

    @EventBusListenerMethod
    public void onMapWordSelected(MapWordSelectionChangeEvent mapWordSelectionChangeEvent) {
        Optional<MapWord> mapWord = (Optional<MapWord>) mapWordSelectionChangeEvent.getSource();
        if (mapWord.isPresent()) {
            loadMapWordGrid(mapWord.get());
            getView().setVisible(true);
        } else {
            getView().getMapWordGrid().setItems(new ArrayList<>());
            getView().setVisible(false);
        }
    }


    private void loadMapWordGrid(MapWord mapWord) {
        getView().getMapWordGrid().setItems(mapLinkRepositoryService
                .findAllByFromWord(mapWord.getId())
                .stream().map(mapLink -> mapLink.getFromWord().getId().compareTo(mapWord.getId()) == 0 ?
                        mapLink.getToWord() : mapLink.getFromWord()));
    }

}
