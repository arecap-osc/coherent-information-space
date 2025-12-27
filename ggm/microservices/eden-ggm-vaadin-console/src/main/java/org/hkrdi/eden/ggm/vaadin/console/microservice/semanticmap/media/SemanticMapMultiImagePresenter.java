package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ScreenProperties;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.SemanticMapStateSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.SemanticMapChangedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.SemanticMapWordLetterRefreshEvent;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class SemanticMapMultiImagePresenter extends MultiImagePresenter {

    @Autowired
    private SemanticMapStateSelectionProcessor stateSelectionManager;

    @Autowired
    private SemanticMapService semanticMapService;

    @Autowired
    private SemanticMapMediaLayerManager mediaLayerManager;

    @Autowired
    private ScreenProperties screenProperties;

    @Override
    public MediaRenderLayerFactory getMediaRendererLayerFactory() {
        return mediaLayerManager;
    }

    @Override
    public SemanticMapMultiImageView getView() {
        return (SemanticMapMultiImageView)super.getView();
    }

    @Override
    public void doClickBusiness(Double screenX, Double screenY) {
        //get first selected node from well
        Optional<WordBean> word = Optional.empty();
        List<WordBean> multiNetworkSelectedWords =
                mediaLayerManager.getMediaLayers().stream()
                        .filter(MediaLayer::isVisible)
                        .map(mediaLayer -> (SemanticMapMediaLayer)mediaLayer)
                        .filter(SemanticMapMediaLayer::hasSelectable)
                        .map(MediaLayer::getName)
                        .map(network -> semanticMapService.findNetworkNodeByScreenCoordinate(network, screenX, screenY, screenProperties))
                        .filter(selectedNode -> selectedNode.isPresent())
                        .map(selectedNode -> selectedNode.get())
                        .collect(Collectors.toList());

        //get node from last visible network
        if (!multiNetworkSelectedWords.isEmpty()) {
            word = Optional.of(multiNetworkSelectedWords.get(multiNetworkSelectedWords.size()-1));
        }

        if (word.isPresent()) {
            stateSelectionManager.processWordSelection(word.get());
        }else {
            //cluster
        }

        //TODO repaint
        this.composeOrRefreshImages();
    }

    public void changeSemanticMap(Long semanticMapId) {
        stateSelectionManager.processSemanticMapSelectionChange(semanticMapId);
        this.composeOrRefreshImages();
    }

    @EventBusListenerMethod
    public void onSemanticMapChangeEvent(SemanticMapChangedEvent semanticMapChangedEvent) {
        changeSemanticMap(((SemanticMap) semanticMapChangedEvent.getSource()).getId());
    }

    @EventBusListenerMethod
    public void onSemanticMapWordLetterRefreshEvent(SemanticMapWordLetterRefreshEvent semanticMapWordLetterRefreshEvent) {
        changeSemanticMap((Long) semanticMapWordLetterRefreshEvent.getSource());
    }

}
