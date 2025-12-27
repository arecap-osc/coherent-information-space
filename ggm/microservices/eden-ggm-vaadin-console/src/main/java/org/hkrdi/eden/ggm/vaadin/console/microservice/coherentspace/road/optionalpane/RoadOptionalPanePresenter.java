package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.common.AttachedDocGridComponent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.service.RoadExportDataService;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.service.RoadService;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1RoadClearSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1RoadSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1ToNode2RoadClearSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node1ToNode2RoadSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node2RoadClearSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.Node2RoadSelectedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RoadApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RoadNetworkChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.roadmanager.RoadEditorDialogView.RoadEditorDialogViewEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeSemanticBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeSemanticBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.server.StreamResource;

public abstract class RoadOptionalPanePresenter extends DefaultFlowEntityPresenter<Road, Long> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoadOptionalPanePresenter.class);
	
    @Autowired
    private ApplicationRepositoryService applicationService;
    
    @Autowired
    private CoherentSpaceService coherentSpaceService;
    
    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private RoadService roadService;
    
    @Autowired
    private RoadExportDataService exportDataService;
    
    @Override
	public RoadOptionalPaneView getView() {
		return (RoadOptionalPaneView)super.getView();
	}

	@Override
    public RoadService getService() {
        return roadService;
    }
    
	@Override
	public Class<Road> getEntityType() {
		return Road.class;
	}
	
	@Override
	public Road cloneEntity(Road entity) {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().duplicate();
	}
    
	public abstract void setNetworkForValidators();
		
	@Override
    public void beforeSave() {
    	getEntity().setNetwork(getStateManager().getCurrentNetwork());
    }
	
	public void handleGroupNameRoadSelection(Road road) {

	}

	@Override
	public void prepareModel(EventObject event) {
		super.prepareModel(event);

		populateGroupNameRoadComboBox();

		getView().getGroupNameRoadComboBox().setItemLabelGenerator(i -> i.getGroupName()+" - "+i.getName());
	}

	private void populateGroupNameRoadComboBox() {
		getView().getGroupNameRoadComboBox().setItems(getService().getRoadsByNetwork(getStateManager().getCurrentNetwork()));
	}

	public void handleRoadSelection(Road road) {
		if (road.getRoad()!=null && !road.getRoad().trim().equals("")) {
	    	String network = getStateManager().getCurrentNetwork();//road.getNetwork();
	    	List<NodeBean> roadNodeBeans = Arrays.asList(road.getRoad().split(",")).stream().map(s->new NodeBean(network, new Long(s))).
	    			collect(Collectors.toList());
	
	//    	//clear multiImage
	////    	roadStateSelectionManager.clearSelectionRoadLayersForCurrentNetwork();
	//    	
	//    	//set selected road in model
	//    	stateManager.currentState().setRoad(roadNodeBeans);
	//    	//process selection in model
	//    	stateManager.paintRoad();
	//    	
	//    	//refresh images
	//    	roadMultiImagePresenter.composeOrRefreshImages();
	
	    	//show semantic and syntax in grid
	    	List<NodeSemanticBean> semantic= roadNodeBeans.stream()
				.map(nodeBean -> coherentSpaceService.findWellsNodeSemanticBean(nodeBean, getStateManager().getCurrentApplicationId()))
				.filter( nodeBeanOptional -> nodeBeanOptional.isPresent())
				.map(nodeBeanOptional -> nodeBeanOptional.get())
				.collect(Collectors.toList());
	    	
	    	List<EdgeSemanticBean> semanticAndSyntax = new ArrayList<>();
	    	if (semantic.size()>1) {
	    		//add first node
	    		semanticAndSyntax.add(wrapNodeSemanticBeanAsEdgeSemanticBean(semantic.get(0)));
		    	for (int i=1; i<semantic.size(); i++) {
		    		NodeSemanticBean nodeSemanticBean1 = semantic.get(i-1);
		    		NodeSemanticBean nodeSemanticBean2 = semantic.get(i);
		    		EdgeSemanticBean edgeSemanticBean = coherentSpaceService.findWellsNodeSemanticBean(
		    				new EdgeBean(nodeSemanticBean1.getNode(), nodeSemanticBean2.getNode()), 
		    				getStateManager().getCurrentApplicationId()).get();
		    		
		    		semanticAndSyntax.add(edgeSemanticBean);
		    		semanticAndSyntax.add(wrapNodeSemanticBeanAsEdgeSemanticBean(nodeSemanticBean2));
		    	}
	    	}
	    	
	    	getView().getSemanticGrid().setItems(semanticAndSyntax);
		}
	}

	protected abstract RoadStateSelectionManager getStateManager();

	private SecurityContext localSC = null;
	
    protected void handleExport(String road, String groupName, String roadName) {
    	localSC = SecurityContextHolder.getContext();
        getView().getDownloadAnchor()
                    .setHref(new StreamResource("road"+ (groupName!=null?"_"+groupName:"") + (roadName!=null?"_"+roadName:"") + "_raport" + ".pdf", () -> createExportStreamResource(road, localSC)));
    }
    
	private InputStream createExportStreamResource(String road, SecurityContext sc) {
        try {
        	SecurityContextHolder.clearContext();
			SecurityContextHolder.getContext().setAuthentication(sc.getAuthentication());
            ByteArrayInputStream is = new ByteArrayInputStream(org.apache.commons.io.IOUtils.toByteArray(getClass().getResourceAsStream("/edenggmroad.docx")));
            List<String> visibleNetworks = new ArrayList<String>();
            visibleNetworks.add(getStateManager().getCurrentNetwork());
            Application application = applicationService.getApplicationById(getStateManager().getCurrentApplicationId());
            byte[] result = exportDataService.generate(application, 
            		is,visibleNetworks, road);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
        	SecurityContextHolder.clearContext();
        }
        return null;
    }
	
    public abstract void handleComputeRoad(ClickEvent<Button> buttonClickEvent) ;
    
    public List<Application> getApplications() {
    	return applicationService.getAllApplications();
    }

    @EventBusListenerMethod
    public void onNode1RoadSelectedEvent(Node1RoadSelectedEvent node1RoadSelectedEvent){
        NodeBean node = (NodeBean) node1RoadSelectedEvent.getSource();
    	getView().getNode1SemanticLabel().setText(
    			coherentSpaceService.findWellsNodeSemanticBean((NodeBean)node1RoadSelectedEvent.getSource(), getStateManager().getCurrentApplicationId()).
    				get().getNote()
    			);

        getView().  getAttachedDocumentsLayout().removeAll();
        Optional<ApplicationData> applicationData = applicationDataService.findByAddressIndex(getStateManager().getCurrentApplicationId(), Arrays.asList(node.getNetwork()),
                node.getAddressIndex()).stream().findAny();
        if (applicationData.isPresent()) {
            AttachedDocGridComponent attachedDocGridComponent = new AttachedDocGridComponent(getStateManager().getCurrentApplicationId(),
                    getView().getAttachedDocumentsDialogView().getPresenter().getApplicationDataAsNodeWellsIds(applicationData.get()), ApplicationData.class, new HorizontalLayout());
            attachedDocGridComponent.setActive(false);
            getView().getAttachedDocumentsLayout().add(attachedDocGridComponent);
        }
    }
    
    protected void updateGridOnRoadChange(String roadStr){
    	if (roadStr!=null && !roadStr.trim().equals("")) {
	    	String network = getStateManager().getCurrentNetwork();//road.getNetwork();
	    	List<NodeBean> roadNodeBeans = Arrays.asList(roadStr.split(",")).stream().map(s->new NodeBean(network, new Long(s))).
	    			collect(Collectors.toList());
	
	    	//show semantic and syntax in grid
	    	List<NodeSemanticBean> semantic= roadNodeBeans.stream()
				.map(nodeBean -> coherentSpaceService.findWellsNodeSemanticBean(nodeBean, getStateManager().getCurrentApplicationId()))
				.filter( nodeBeanOptional -> nodeBeanOptional.isPresent())
				.map(nodeBeanOptional -> nodeBeanOptional.get())
				.collect(Collectors.toList());
	    	
	    	List<EdgeSemanticBean> semanticAndSyntax = new ArrayList<>();
	    	if (semantic.size()>1) {
	    		//add first node
	    		semanticAndSyntax.add(wrapNodeSemanticBeanAsEdgeSemanticBean(semantic.get(0)));
		    	for (int i=1; i<semantic.size(); i++) {
		    		NodeSemanticBean nodeSemanticBean1 = semantic.get(i-1);
		    		NodeSemanticBean nodeSemanticBean2 = semantic.get(i);
		    		Optional<EdgeSemanticBean> edgeSemanticBeanOptional = coherentSpaceService.findWellsNodeSemanticBean(
		    				new EdgeBean(nodeSemanticBean1.getNode(), nodeSemanticBean2.getNode()), 
		    				getStateManager().getCurrentApplicationId());
		    		
		    		if (edgeSemanticBeanOptional.isPresent()){
		    			semanticAndSyntax.add(edgeSemanticBeanOptional.get());
		    		}
		    		semanticAndSyntax.add(wrapNodeSemanticBeanAsEdgeSemanticBean(nodeSemanticBean2));
		    	}
	    	}
	    	
	    	getView().getSemanticGrid().setItems(semanticAndSyntax);
		}
    }
    
    @EventBusListenerMethod
    public void onNode1RoadClearSelectedEvent(Node1RoadClearSelectedEvent node1RoadSelectedEvent){
    	getView().getNode1SemanticLabel().setText("");
    }
    
    @EventBusListenerMethod
    public void onNode1ToNode2RoadSelectedEvent(Node1ToNode2RoadSelectedEvent node1ToNode2RoadSelectedEvent){
    	getView().getNode1Node2SyntaxLabel().setText(
    			coherentSpaceService.findWellsNodeSemanticBean((EdgeBean)node1ToNode2RoadSelectedEvent.getSource(), getStateManager().getCurrentApplicationId()).
    				get().getSyntax()
    			);
    }
    
    @EventBusListenerMethod
    public void onNode1ToNode2RoadClearSelectedEvent(Node1ToNode2RoadClearSelectedEvent node1ToNode2RoadSelectedEvent){
    	getView().getNode1Node2SyntaxLabel().setText("");
        getView().getAttachedDocumentsLayout().removeAll();
    }
    
    @EventBusListenerMethod
    public void onNode2RoadSelectedEvent(Node2RoadSelectedEvent node2RoadSelectedEvent){
        NodeBean node = (NodeBean) node2RoadSelectedEvent.getSource();
    	getView().getNode2SemanticLabel().setText(
    			coherentSpaceService.findWellsNodeSemanticBean((NodeBean)node2RoadSelectedEvent.getSource(), getStateManager().getCurrentApplicationId()).
    				get().getNote()
    			);

        Optional<ApplicationData> applicationData = applicationDataService.findByAddressIndex(getStateManager().getCurrentApplicationId(), Arrays.asList(node.getNetwork()),
                node.getAddressIndex()).stream().findAny();
        if (applicationData.isPresent()) {
            AttachedDocGridComponent attachedDocGridComponent = new AttachedDocGridComponent(getStateManager().getCurrentApplicationId(),
                    getView().getAttachedDocumentsDialogView().getPresenter().getApplicationDataAsNodeWellsIds(applicationData.get()), ApplicationData.class, new HorizontalLayout());
            attachedDocGridComponent.setActive(false);
            getView().getAttachedDocumentsLayout().add(attachedDocGridComponent);
        }
    }
    
    @EventBusListenerMethod
    public void onNode2RoadClearSelectedEvent(Node2RoadClearSelectedEvent e){
    	getView().getNode2SemanticLabel().setText("");
    }
    
    @EventBusListenerMethod
    public void onRoadChangeCurrentApplication(RoadApplicationChangeEvent applicationEvent) {
    	clearRoadFormComboboxAndRoadGrid();
    }
    
    @EventBusListenerMethod
    public void onRoadNetworkChangeEvent(RoadNetworkChangeEvent event){
    	clearRoadFormComboboxAndRoadGrid();
    	
    	//just reset the validator. No way to remove it for now (18.10.2019)
    	setNetworkForValidators();

    	populateGroupNameRoadComboBox();
    }
    
    public abstract void clearRoadFormComboboxAndRoadGrid();
    
	public void clearRoadComboboxAndRoadGrid() {
		//clear road text
		getView().getRoadTextField().setValue("");
		//clear road combobox
		getView().getRoadComboBox().setItems(Collections.emptyList());
		//clear road grid
		getView().getSemanticGrid().setItems(Collections.emptyList());
    }
    	
    private EdgeSemanticBean wrapNodeSemanticBeanAsEdgeSemanticBean(NodeSemanticBean node) {
    	return new EdgeSemanticBean(new EdgeBean(node.getNode(), null), node.getNote(), node.getDetails());
    }

	public void handleGridSelection(SelectionEvent<Grid<EdgeSemanticBean>, EdgeSemanticBean> edgeSemanticBean) {
		EdgeBean selectedEdgeBean = edgeSemanticBean.getFirstSelectedItem().get().getEdge();

		if (selectedEdgeBean.getToNode() != null) {//edge selected
			getStateManager().processNodeOrEdgeSelection(null, selectedEdgeBean);
		}else {
			getStateManager().processNodeOrEdgeSelection(selectedEdgeBean.getFromNode(), null);
		}
    	//refresh images
    	getRoadMultiImagePresenter().composeOrRefreshImages();
	}

	public abstract RoadMultiImagePresenter getRoadMultiImagePresenter();

	public void handleClearAllRoads(ClickEvent<Button> e) {
		clearRoadFormComboboxAndRoadGrid();
		
		//clear multiImage
    	getStateManager().clearAllRoadLayersForCurrentNetwork();
    	    	
    	//refresh images
    	getRoadMultiImagePresenter().composeOrRefreshImages();
	}
	
	public void onConfirmationSave(ComponentEvent<Dialog> event) {
		populateGroupNameRoadComboBox();
		getView().getGroupNameRoadComboBox().setValue(((RoadEditorDialogViewEvent)event).getSelectedRoad());
////		if (((RoadNameSaveEvent)event).getName().equals(getEntity().getName()) &&
////				((RoadNameSaveEvent)event).getGroupName().equals(getEntity().getGroupName())) {
////
////		}
//    	getEntity().setName(((RoadNameSaveEvent)event).getName());
//    	getEntity().setGroupName(((RoadNameSaveEvent)event).getGroupName());
////    	getEntity().setManualNodes(getEntity().getRoad()!=null && !"".equals(getEntity().getRoad())?
////    			Arrays.asList(getEntity().getRoad().split("\\s*,\\s*")).stream().map(m->Long.valueOf(m)).collect(Collectors.toList()):null);
//    	this.save();
//
////    	getView().getRoadComboBox().setItems(getEntity());
////    	getView().getRoadComboBox().setValue(getEntity());
    }

    public Text getEdgeSemanticBeanText(EdgeSemanticBean edgeSemanticBean) {
        return new Text(edgeSemanticBean.getEdge().getToNode() != null ?
                "(" + edgeSemanticBean.getEdge().getFromNode().getAddressIndex() + "-" + edgeSemanticBean.getEdge().getToNode().getAddressIndex() + ") " :
                "(" + edgeSemanticBean.getEdge().getFromNode().getAddressIndex() + ") " +
                        edgeSemanticBean.getSyntax());
    }

    public Button getAttachmentsButton(EdgeSemanticBean edgeSemanticBean) {
        Button button = new Button(VaadinIcon.PAPERCLIP.create());
        button.getStyle().set("margin-left", "auto");
        if (edgeSemanticBean.getEdge().getToNode() != null) {
            button.setVisible(false);
            return button;
        }
        Optional<ApplicationData> applicationDataOpt = applicationDataService.findByAddressIndex(getStateManager().getCurrentApplicationId(),
                Arrays.asList(edgeSemanticBean.getEdge().getFromNode().getNetwork()), edgeSemanticBean.getEdge().getFromNode().getAddressIndex()).stream()
                .findAny();
        button.addClickListener(clickEvent -> getView().getAttachedDocumentsDialogView().open(applicationDataOpt));
        return button;
    }
}
