package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapLinkSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.MapWordSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.SemanticMapMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer.SemanticMapLinkSelectionMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer.SemanticMapWordLinksMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer.SemanticMapWordSelectionMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer.SemanticMapWordsLettersMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.LinkBean;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

import java.util.Optional;

@SpringComponent
@UIScope
public class SemanticMapStateSelectionProcessor  {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
	private UIEventBus uIEventBus; 
	
    @Autowired
    private SemanticMapMediaLayerManager mediaLayerManager;

    @Autowired
    private SemanticMapService semanticMapService;

    @Autowired
    private SemanticMapIe semanticMapIe;

    private WordBean fromWord;

    private WordBean toWord;

    public void processWordSelection(WordBean selectedWord) {
        mediaLayerManager.getMediaRendererLayers().stream().forEach(layer -> layer.setNeedRefresh(false));
        if(fromWord != null && fromWord.equals(selectedWord)) {
            deselectFromWord();
            return;
        }
        if(fromWord == null) {
            fromWord = selectedWord;
            selectFromWord();
            return;
        }
        if(toWord != null && toWord.equals(selectedWord)) {
            deselectToWord();
            return;
        }
        Optional<WordBean> tw = semanticMapService.getWordBeanLinks(fromWord).stream()
                .filter(lnk -> lnk.getToWord().equals(selectedWord)).findAny().map(LinkBean::getToWord);
        if(tw.isPresent()) {
            toWord = selectedWord;
            selectToWord();
            return;
        }
        fromWord = selectedWord;
        toWord = null;
        selectFromWord();
        deselectToWord();
    }

    public void processSemanticMapSelectionChange(Long semanticMapId) {
        if(fromWord != null) {
            deselectFromWord();
        }
        if(toWord != null) {
            deselectToWord();
        }
        refreshMapWordsMediaLayer(semanticMapId);
    }

    public void refreshMapWordsMediaLayer(Long semanticMapId) {
        SemanticMapWordsLettersMediaLayer semanticMapWordsLettersMediaLayer =
                mediaLayerManager.getLayerByType(mediaLayerManager, SemanticMapWordsLettersMediaLayer.class).get();
        semanticMapWordsLettersMediaLayer.setSemanticMapId(semanticMapId);
        semanticMapWordsLettersMediaLayer.setNeedRefresh(true);
    }

    private void deselectFromWord() {
        SemanticMapWordSelectionMediaLayer wordSelectionMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapWordSelectionMediaLayer.class).get();
        wordSelectionMediaLayer.setFromWord(null);
        wordSelectionMediaLayer.setToWord(null);
        wordSelectionMediaLayer.setNeedRefresh(true);
        deselectLinkBean();
        SemanticMapWordLinksMediaLayer wordLinksMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapWordLinksMediaLayer.class).get();
        wordLinksMediaLayer.setWordLinks(null);
        wordLinksMediaLayer.setNeedRefresh(true);
        fromWord = null;
        semanticMapIe.setFirstMapWordId(null);
        semanticMapIe.setSecondMapWordId(null);
        uIEventBus.publish(this, new MapWordSelectionChangeEvent(Optional.empty()));
        if(toWord != null) {
        	uIEventBus.publish(this, new MapLinkSelectionChangeEvent(Optional.empty()));
        }
        toWord = null;
        LOGGER.info("User deselected FROM word on semantic-map route");
    }

    private void deselectToWord() {
        SemanticMapWordSelectionMediaLayer wordSelectionMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapWordSelectionMediaLayer.class).get();
        wordSelectionMediaLayer.setToWord(null);
        wordSelectionMediaLayer.setNeedRefresh(true);
        deselectLinkBean();
        semanticMapIe.setSecondMapWordId(null);

        if(toWord != null) {
        	uIEventBus.publish(this, new MapLinkSelectionChangeEvent(Optional.empty()));
        }
        toWord = null;
        LOGGER.info("User deselected TO word on semantic-map route");
    }

    private void deselectLinkBean() {
        semanticMapIe.setFirstMapLinkId(null);
        semanticMapIe.setSecondMapLinkId(null);
        SemanticMapLinkSelectionMediaLayer linkSelectionMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapLinkSelectionMediaLayer.class).get();
        linkSelectionMediaLayer.setLinkBean(null);
        linkSelectionMediaLayer.setNeedRefresh(true);
    }

    private void selectFromWord() {
        SemanticMapWordSelectionMediaLayer wordSelectionMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapWordSelectionMediaLayer.class).get();
        wordSelectionMediaLayer.setFromWord(fromWord);
        wordSelectionMediaLayer.setToWord(null);
        wordSelectionMediaLayer.setNeedRefresh(true);
        SemanticMapWordLinksMediaLayer wordLinksMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapWordLinksMediaLayer.class).get();
        wordLinksMediaLayer.setWordLinks(semanticMapService.getWordBeanLinks(fromWord));
        wordLinksMediaLayer.setNeedRefresh(true);
        MapWord mapWord = semanticMapService.findMapWord(semanticMapIe.getSemanticMapId(), fromWord);
        LOGGER.info("User selected FROM word " +  mapWord.getLetter() + "(" + mapWord.getWord() +") on semantic-map route");
        semanticMapIe.setFirstMapWordId(mapWord.getId());
        semanticMapIe.setSecondMapWordId(null);
        uIEventBus.publish(this, new MapWordSelectionChangeEvent(Optional.of(mapWord)));
        if(toWord != null) {
        	uIEventBus.publish(this, new MapLinkSelectionChangeEvent(Optional.empty()));
        }
        toWord = null;
    }

    private void selectToWord() {
        SemanticMapWordSelectionMediaLayer wordSelectionMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, toWord.getNetwork(), SemanticMapWordSelectionMediaLayer.class).get();
        wordSelectionMediaLayer.setToWord(toWord);
        wordSelectionMediaLayer.setNeedRefresh(true);
        selectLinkBean();
        LinkBean linkBean = new LinkBean(fromWord, toWord);
        MapLink mapLink = semanticMapService.findMapLink(semanticMapIe.getSemanticMapId(), linkBean);
        LOGGER.info("User selected TO word " +  mapLink.getToWord().getLetter() + "(" + mapLink.getToWord().getWord() +") on semantic-map route");
        semanticMapIe.setSecondMapWordId(mapLink.getToWord().getId());
        semanticMapIe.setFirstMapLinkId(mapLink.getId());
        semanticMapIe.setSecondMapLinkId(semanticMapService.findMapLink(semanticMapIe.getSemanticMapId(),
                linkBean.transpose()).getId());
        uIEventBus.publish(this, new MapWordSelectionChangeEvent(Optional.of(mapLink.getFromWord())));
        uIEventBus.publish(this, new MapLinkSelectionChangeEvent(Optional.of(mapLink.getToWord())));

    }

    private void selectLinkBean() {
        SemanticMapLinkSelectionMediaLayer linkSelectionMediaLayer =
                mediaLayerManager.getLayerByNetworkAndType(mediaLayerManager, fromWord.getNetwork(), SemanticMapLinkSelectionMediaLayer.class).get();
        linkSelectionMediaLayer.setLinkBean(new LinkBean(fromWord, toWord));
        linkSelectionMediaLayer.setNeedRefresh(true);
    }

}