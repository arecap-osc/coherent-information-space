package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderStyleAware;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringComponent
@VaadinSessionScope
public class CoherentSpaceNetworkMediaLayerManager implements MediaLayerFactory {
	private static final Map<String, String> layerColors = new LinkedHashMap<>();
	{
		layerColors.put("DEFAULT","#6b89d8");
		
		layerColors.put("SUSTAINABLE_VERTICES::0","#6b89d8");
		layerColors.put("METABOLIC_VERTICES::0","#80ccff");
		
		layerColors.put("SUSTAINABLE_EDGES::1","#ffcc80");
		layerColors.put("SUSTAINABLE_VERTICES::1","#ffb3b3");
		layerColors.put("METABOLIC_VERTICES::1","#ff6600");
		layerColors.put("METABOLIC_EDGES::1","#e67300");
		
		layerColors.put("SUSTAINABLE_EDGES::2","#6DA22C");
		layerColors.put("SUSTAINABLE_VERTICES::2","#BBD41E");
		layerColors.put("METABOLIC_VERTICES::2","#3B7965");
		layerColors.put("METABOLIC_EDGES::2","#156c13");
	}
    private List<MediaLayer> mediaLayers = new ArrayList<>();

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @PostConstruct
    private void initMediaLayers() {
        coherentSpaceService.getAvailableNetworks()
                .forEach(network -> mediaLayers.add(
                		new CoherentSpaceNetworkMediaLayer(network, 
                											ColorFactory.web(
                													layerColors.containsKey(network)?layerColors.get(network):layerColors.get("DEFAULT")))));
    }

    //TODO: extern intr-un serviciu pentru a pastra modelul
    public void setMediaRendererLayers(String network, List<MediaRendererLayer> mediaRendererLayers) {
        Optional<MediaLayer> mediaLayer = getMediaLayerByName(network);
        if(mediaLayer.isPresent()) {
            mediaRendererLayers.stream()
                    .filter(mediaRendererLayer -> MediaRenderStyleAware.class.isAssignableFrom(mediaRendererLayer.getClass()))
                    .map(mediaRendererLayer -> (MediaRenderStyleAware)mediaRendererLayer)
                    .forEach(mediaRenderStyleAware -> mediaRenderStyleAware.setMediaRenderStyle(mediaLayer.get().getMediaRenderStyle()));
            mediaLayer.get().getMediaRendererLayers().clear();
            mediaLayer.get().getMediaRendererLayers().addAll(mediaRendererLayers);
        }
    }

    @Override
    public List<MediaLayer> getMediaLayers() {
        return mediaLayers;
    }

}
