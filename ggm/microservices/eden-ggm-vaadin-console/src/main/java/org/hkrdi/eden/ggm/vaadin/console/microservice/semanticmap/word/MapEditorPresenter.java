package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentPresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapLinkSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapWordSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical.FromMapLinkView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical.ToMapLinkView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapLinkRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@SpringComponent
@UIScope
public class MapEditorPresenter extends LeftSideContentPresenter {

    private MapEditorView view;

    @Autowired
    private MapLinkRepositoryService mapLinkRepositoryService;

    @Autowired
    private FromMapWordView fromMapWordFormView;

    @Autowired
    private ToMapWordView toMapWordFormView;

    @Autowired
    private FromMapLinkView fromMapLinkView;

    @Autowired
    private ToMapLinkView toMapLinkView;

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Autowired
    private EventBus.UIEventBus uIEventBus;

    @PostConstruct
    private void initEventBus() {
        uIEventBus.subscribe(this);
    }

    @PreDestroy
    private void predestroyEventBus() {
        uIEventBus.unsubscribe(this);
    }

    @Override
    public void setView(FlowView view) {
        this.view = (MapEditorView) view;
    }

    @Override
    public MapEditorView getView() {
        return view;
    }

    public FromMapWordView getFromMapWordFormView() {
        return fromMapWordFormView;
    }

    @EventBusListenerMethod
    public void onMapWordSelectionChangeEvent(MapWordSelectionChangeEvent mapWordSelectionChangeEvent) {
        display();
    }

    @EventBusListenerMethod
    public void onMapLinkSelectionChangeEvent(MapLinkSelectionChangeEvent mapLinkSelectionChangeEvent) {
        if (semanticMapIe.getFirstMapWordId() == null || semanticMapIe.getSecondMapWordId() == null) {
            display();
            return;
        }
        Optional<MapLink> firstMapLinkOptional = mapLinkRepositoryService
                .findByFromWordIdAndToWordId(semanticMapIe.getFirstMapWordId(), semanticMapIe.getSecondMapWordId());

        MapLink firstMapLink;
        if (firstMapLinkOptional.isPresent()) {
            firstMapLink = firstMapLinkOptional.get();
        } else {
            firstMapLink = mapLinkRepositoryService.saveEmptyMapLinkBetweenMapWords(semanticMapIe.getFirstMapWordId(),
                    semanticMapIe.getSecondMapWordId());
        }

        semanticMapIe.setFirstMapLinkId(firstMapLink.getId());

        Optional<MapLink> secondMapLinkOptional = mapLinkRepositoryService
                .findByFromWordIdAndToWordId(semanticMapIe.getSecondMapWordId(), semanticMapIe.getFirstMapWordId());

        MapLink secondMapLink;
        if (secondMapLinkOptional.isPresent()) {
            secondMapLink = secondMapLinkOptional.get();
        } else {
            secondMapLink = mapLinkRepositoryService.saveEmptyMapLinkBetweenMapWords(semanticMapIe.getSecondMapWordId(),
                    semanticMapIe.getFirstMapWordId());
        }
        semanticMapIe.setSecondMapLinkId(secondMapLink.getId());
        display();
    }


    private void display() {
        getView().setVisible(semanticMapIe.getFirstMapWordId() != null);
        if(getView().isVisible()) {
            fromMapWordFormView.getPresenter().prepareModelAndView(null);
        }

        if (semanticMapIe.getSecondMapWordId() == null) {
            getView().getMapWordsLayout().remove(toMapWordFormView);
            getView().getMapLinksLayout().remove(fromMapLinkView, toMapLinkView);
        } else {
            getView().getMapWordsLayout().add(toMapWordFormView);
            getView().getMapLinksLayout().add(fromMapLinkView, toMapLinkView);
        }
    }

}
