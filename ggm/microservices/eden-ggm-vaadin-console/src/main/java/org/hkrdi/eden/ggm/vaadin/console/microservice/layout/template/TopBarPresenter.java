package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template;


import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.plexus.util.StringUtils;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.featureflag.FeatureFlags;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.MarkedSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class TopBarPresenter extends DefaultFlowPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopBarPresenter.class);

    private TopBarView view;

    private MarkedSelectionProcessor stateSelectionProcessor;

    private MultiImagePresenter multiImagePresenter;

    private StateSelectionManager stateSelectionManager;

    @Autowired
    private ApplicationDataRepositoryService applicationDataRepositoryService;

    @Override
    public void setView(FlowView view) {
        this.view = (TopBarView) view;
    }

    @Override
    public TopBarView getView() {
        return view;
    }
    
    @Override
    public void afterPrepareModel(EventObject event) {
    	super.afterPrepareModel(event);
    	
    	if (FeatureFlags.LANGUAGE_CHOOSER_COMBOBOX.check()) {
        	if (UI.getCurrent().getSession().getAttribute("selectedLanguage") == null) {
        		UI.getCurrent().getSession().setAttribute("selectedLanguage", 
        				UI.getCurrent().getLocale().getLanguage().toUpperCase());
        	}
        	String language = (String)UI.getCurrent().getSession().getAttribute("selectedLanguage");
        	
        	getView().setLanguageInCombo(language);
        }
    }

    public void setupPresenter(MarkedSelectionProcessor stateSelectionProcessor, StateSelectionManager stateSelectionManager, MultiImagePresenter multiImagePresenter) {
        this.stateSelectionProcessor = stateSelectionProcessor;
        this.stateSelectionManager = stateSelectionManager;
        this.multiImagePresenter = multiImagePresenter;
        getView().showMarkFunctionality();
    }

    public void markProvidedNodesAndEdges(ClickEvent<Button> buttonClickEvent) {
        if (stateSelectionManager.getCurrentNetwork() == null || stateSelectionManager.getCurrentNetwork().length() == 0) {
            Notification.show("Nicio retea nu este selectata");
            return;
        }

        String nodesAndEdgesToMark = getView().getNodesAndEdgesToMark();
        if (nodesAndEdgesToMark != null && nodesAndEdgesToMark.length() > 0) {
            List<String> inputList = Stream.of(nodesAndEdgesToMark.split(",")).map(String::trim).collect(toList());
            List<String> indices = inputList.stream().filter(StringUtils::isNumeric).collect(toList());
            resetMarkedMediaLayers();
            indices.stream().forEach(index -> markNode(new Long(index)));
            if (inputList.removeAll(indices) || indices.size() == 0) {
                Set<Long> nodes = new HashSet<>();
                List<ApplicationData> edges = new ArrayList<>();

                for (String semanticOrSyntax : inputList) {
                    nodes.addAll(applicationDataRepositoryService.findByContainingSemanticIgnoreCase(stateSelectionManager.getCurrentApplicationId(),
                            Arrays.asList(stateSelectionManager.getCurrentNetwork()), semanticOrSyntax).stream().map(ApplicationData::getAddressIndex).collect(Collectors.toSet()));
                    edges.addAll(applicationDataRepositoryService.findByContainingSyntaxIgnoreCase(stateSelectionManager.getCurrentApplicationId(),
                            Arrays.asList(stateSelectionManager.getCurrentNetwork()), semanticOrSyntax));
                }

                nodes.stream().forEach(this::markNode);
                edges.stream().forEach(this::markEdge);
            }
            return;
        }
        Notification.show("Nicio valoare nu a fost introdusa");
    }

    private void resetMarkedMediaLayers() {
        stateSelectionProcessor.getMarkedNodesMediaLayer(stateSelectionManager.getCurrentNetwork()).clear();
        stateSelectionProcessor.getMarkedEdgesMediaLayer(stateSelectionManager.getCurrentNetwork()).clear();
    }

    private void markEdge(ApplicationData applicationData) {
        stateSelectionProcessor.processEdgeMarkerSelection(new EdgeBean(new NodeBean(stateSelectionManager.getCurrentNetwork(), applicationData.getAddressIndex()),
                new NodeBean(applicationData.getNetwork(), applicationData.getToAddressIndex())));
        multiImagePresenter.refreshAll();
    }

    private void markNode(Long index) {
        stateSelectionProcessor.processNodeMarkerSelection(new NodeBean(stateSelectionManager.getCurrentNetwork(), index));
        multiImagePresenter.refreshAll();
    }

    public void resetMarkingSelection(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("User clicked on the reset markings button");
        if (stateSelectionManager.getCurrentNetwork() == null || stateSelectionManager.getCurrentNetwork().length() == 0) {
            Notification.show("Nicio retea nu este selectata");
            return;
        }

        resetMarkedMediaLayers();
        getView().clearMarkMultipleTextField();
        multiImagePresenter.refreshAll();
    }
    
    public void languageSelected(String lang) {
    	UI.getCurrent().access(()->{
    		UI.getCurrent().setLocale(new Locale(lang.toLowerCase()));
    		UI.getCurrent().getSession().setAttribute("selectedLanguage", lang.toUpperCase());
    		}
    	);
    }
}
