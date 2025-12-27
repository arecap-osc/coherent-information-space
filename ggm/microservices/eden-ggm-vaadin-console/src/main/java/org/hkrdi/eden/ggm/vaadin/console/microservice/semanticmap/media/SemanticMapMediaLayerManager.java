package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.SemanticMapServiceFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class SemanticMapMediaLayerManager implements MediaRenderLayerFactory, MediaLayerFactory, SemanticMapServiceFactory, StateSelectionManager {

    private List<MediaLayer> mediaLayers = new ArrayList<>();

    @PostConstruct
    private void initLayers() {
    	SemanticMapMediaLayer semanticMapDataLetters = new SemanticMapMediaLayer("DATA_LETTERS", false);
    	semanticMapDataLetters.getMediaRendererLayers().add(new SemanticMapWordsLettersMediaLayer());
    	mediaLayers.add(semanticMapDataLetters);

    	SemanticMapMediaLayer semanticGeometryRendererLayer = new SemanticMapMediaLayer("SUSTAINABLE_VERTICES::2", true);
        semanticGeometryRendererLayer.getMediaRendererLayers()
                .add(new SemanticMapWordLinksMediaLayer(semanticGeometryRendererLayer.getName()));
        semanticGeometryRendererLayer.getMediaRendererLayers()
                .add(new SemanticMapLinkSelectionMediaLayer(semanticGeometryRendererLayer.getName()));
        semanticGeometryRendererLayer.getMediaRendererLayers()
                .add(new SemanticMapWordsMediaLayer(semanticGeometryRendererLayer.getName()));
        semanticGeometryRendererLayer.getMediaRendererLayers()
                .add(new SemanticMapLinksMediaLayer(semanticGeometryRendererLayer.getName()));
        semanticGeometryRendererLayer.getMediaRendererLayers()
                .add(new SemanticMapWordSelectionMediaLayer(semanticGeometryRendererLayer.getName()));
        mediaLayers.add(semanticGeometryRendererLayer);
    }

    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        List<MediaRendererLayer> mediaRendererLayers = new ArrayList<>();
        getMediaLayers().stream()
                .filter(mediaLayer -> mediaLayer.isVisible())
                .forEach(mediaLayer -> mediaRendererLayers.addAll(mediaLayer.getMediaRendererLayers()));
        return mediaRendererLayers;
    }


    @Override
    public List<MediaLayer> getMediaLayers() {
        return mediaLayers;
    }

	@Override
	public String getCurrentNetwork() {
		return null;
	}

    @Override
    public Long getCurrentApplicationId() {
        return null;
    }

}
