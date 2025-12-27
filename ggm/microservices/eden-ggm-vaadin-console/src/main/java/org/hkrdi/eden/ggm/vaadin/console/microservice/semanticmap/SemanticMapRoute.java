package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap;

import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.manager.SemanticMapManagerView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.SemanticMapMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word.MapEditorView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word.MapWordLinkView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "semantic-map", layout = GgmSystemPwaLayout.class)
public class SemanticMapRoute extends GgmSystemRouteLayout{

    @Autowired
    private SemanticMapManagerView semanticMapManagerView;

    @Autowired
    private SemanticMapMultiImageView multiImageView;

    @Autowired
    private MapWordLinkView mapWordLinkView;
    
    @Autowired
    private MapEditorView mapEditorView;

    @Autowired
    private TopBarView topBarView;

    @Override
	protected void buildRouteLayout() {
        topBarView.add(semanticMapManagerView);
        mapWordLinkView.setVisible(false);
        mapEditorView.setVisible(false);
        add(mapEditorView, multiImageView);
        semanticMapManagerView.getPresenter().semanticMapComboBoxSetItemsSelectFirst();
    }

    @Override
	protected String getRouteNameForBredCrumbAndTooltip() {
		return "msg.route.breadcrumb.semanticmap";
	}

	@Override
	protected String getRouteIconPath() {
		return "/frontend/img/semantic_map_logo.png";
	}
}
