package org.arecap.eden.ia.console.media.mvp;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.media.MediaRenderLayerFactory;
import org.arecap.eden.ia.console.media.MediaRendererLayer;
import org.arecap.eden.ia.console.media.MediaRendererTransform;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class ImageComposer {
    private List<MediaRendererLayer> layers;

    private Map<String, Image> imagesMap = new HashMap<>();

    private MediaRendererTransform mediaRendererTransform = new MediaRendererTransform2D();

    public List<Image> getImages(MediaRenderLayerFactory mediaRenderLayerFactory, MediaRendererTransform mediaRendererTransform, Boolean forceRefresh) {
        layers = mediaRenderLayerFactory.getMediaRendererLayers();
        if (forceRefresh) {
            imagesMap.values().stream().filter(img -> img.getParent().isPresent())
                    .forEach(img -> ((HasComponents) img.getParent().get()).remove(img));
            imagesMap.clear();
            if(MediaRendererTransform2D.class.isAssignableFrom(mediaRendererTransform.getClass())) {
                ((MediaRendererTransform2D) this.mediaRendererTransform).config(mediaRendererTransform);
            }
        }
        refreshImages(mediaRendererTransform);
        List<String> names = mediaRenderLayerFactory.getMediaRendererLayersNames();
        Collections.reverse(names);
        return names.stream()
                .filter(name -> imagesMap.get(name) != null)
                .map(name -> setLayerRefreshFalseAndReturnImage(mediaRenderLayerFactory, name))
                .collect(Collectors.toList());
    }


    public void refreshImages(MediaRendererTransform mediaRendererTransform) {
        layers.stream()
                .filter(layer -> layer.isNeedRefresh() || imagesMap.get(layer.getName()) == null)
                .map(layer -> new StreamResource(layer.getName() + "__" + System.currentTimeMillis(), () -> layer.draw(mediaRendererTransform)))
                .map(streamResource -> createNewImageOrUpdateExisting(streamResource))
                .forEach(image -> imagesMap.put(image.getAlt().get().substring(0, image.getAlt().get().indexOf("__")), image));
    }

    private Image createNewImageOrUpdateExisting(StreamResource streamResource) {
        if (imagesMap.get(streamResource.getName().substring(0, streamResource.getName().indexOf("__"))) == null) {
            return new Image(streamResource, streamResource.getName());
        }

        Image image = imagesMap.get(streamResource.getName().substring(0, streamResource.getName().indexOf("__")));
        image.setSrc(streamResource);
        return image;
    }

    private Image setLayerRefreshFalseAndReturnImage(MediaRenderLayerFactory mediaLayerConfigurationFactoryMarker, String name) {
        mediaLayerConfigurationFactoryMarker.getMediaRendererLayer(name).get().setNeedRefresh(false);
        return imagesMap.get(name);
    }
}
