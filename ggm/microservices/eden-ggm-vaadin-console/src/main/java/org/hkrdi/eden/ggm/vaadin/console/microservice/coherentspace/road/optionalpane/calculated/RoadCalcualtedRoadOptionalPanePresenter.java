package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.calculated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.NodeRoadSelectionClickEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated.RoadCalculatedMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated.RoadCalculatedRoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.RoadOptionalPanePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.converter.StringToListLongConverter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.converter.StringToRoadConverter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator.ClustersInNetworkValidator;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator.NodeInNetworkValidator;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator.NodesInNetworkValidator;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.StatusChangeEvent;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadCalcualtedRoadOptionalPanePresenter extends RoadOptionalPanePresenter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoadCalcualtedRoadOptionalPanePresenter.class);
	
    private NodeInNetworkValidator nodeInNetworkValidatorFromNodeTextField = new NodeInNetworkValidator("Nodul nu exista in retea");
    private NodeInNetworkValidator nodeInNetworkValidatorToNodeTextField = new NodeInNetworkValidator("Nodul nu exista in retea");
    private NodesInNetworkValidator nodesInNetworkValidator = new NodesInNetworkValidator("");
    private ClustersInNetworkValidator clustersInNetworkValidator = new ClustersInNetworkValidator("");
    
	@Autowired
	private RoadCalculatedRoadStateSelectionManager stateManager;
	
	@Autowired
	private RoadCalculatedMultiImagePresenter roadMultiImagePresenter;

	@Autowired
    private CoherentSpaceService coherentSpaceService;
	
    @Override
    public RoadCalcualtedRoadOptionalPaneView getView() {
    	return (RoadCalcualtedRoadOptionalPaneView)super.getView();
    }
    
    @Override
	public void prepareModel(EventObject event) {
		super.prepareModel(event);
		
		setNetworkForValidators();
		
		getBinder().forField(getView().getFromNodeTextField())
		.withNullRepresentation("")
		.withConverter(new StringToLongConverter("Se asteapta un numar!"))
//		.withValidator(new LongRangeValidator("Valoare intre 0 si 100.000.000", 0L, 100_000_000L))
		.withValidator(nodeInNetworkValidatorFromNodeTextField)
		.bind(Road::getFromNode, Road::setFromNode);

		getBinder().forField(getView().getToNodeTextField())
			.withNullRepresentation("")
			.withConverter(new StringToLongConverter("Se asteapta un numar!"))
//			.withValidator(new LongRangeValidator("Valoare intre 0 si 100.000.000", 0L, 100_000_000L))
			.withValidator(nodeInNetworkValidatorFromNodeTextField)
			.bind(Road::getToNode, Road::setToNode);

		getBinder().forField(getView().getIncludeNodesTextField())
			.withConverter(new StringToListLongConverter())
			.withValidator(nodesInNetworkValidator)
			.bind(Road::getIncludeNodes, Road::setIncludeNodes);

		getBinder().forField(getView().getRoadTextField())
			.bind(Road::getRoad, Road::setRoad);
		
		getView().getRoadComboBox().setItems(new ArrayList<>());
//		getBinder().forField(getView().getRoadComboBox())
//			.withConverter(new StringToRoadConverter())
//			.withNullRepresentation("")
//			.bind(Road::getRoad, Road::setRoad);
		
		getBinder().forField(getView().getExcludeClustersTextField())
//			.withConverter(new StringToListLongConverter())
			.withValidator(clustersInNetworkValidator)
			.bind(Road::getExcludeClusters, Road::setExcludeClusters);
		
		getView().getRoadComboBox().addValueChangeListener(e -> {
//			if (e.isFromClient()) {
				if (e.getOldValue() != null && e.getValue() != null) {
					//clear road on form changed (exept road)
					if ((e.getOldValue().getFromNode() != null && !e.getOldValue().getFromNode().equals(e.getValue().getFromNode())) ||
							(e.getOldValue().getToNode() != null && !e.getOldValue().getToNode().equals(e.getValue().getToNode())) ||
							(e.getOldValue().getIncludeNodes() != null && !e.getOldValue().getIncludeNodes().equals(e.getValue().getIncludeNodes())) ||
							(e.getOldValue().getExcludeClusters() != null && !e.getOldValue().getExcludeClusters().equals(e.getValue().getExcludeClusters())) ||
							(e.getOldValue().getSearchSyntax() != null && !e.getOldValue().getSearchSyntax().equals(e.getValue().getSearchSyntax()))) {
						clearRoadComboboxAndRoadGrid();
	
						//refresh images
						roadMultiImagePresenter.composeOrRefreshImages();
					}
				}
				
				if (e.getValue() != null) {
					//update grid on road changed
	//				if ((e.getOldValue().getRoad() == null && e.getValue().getRoad()!=null) ||
	//					(e.getOldValue().getRoad() != null && !getEntityValuesBeforeBinderUpdate().getRoad().equals(e.getValue().getRoad()))){
						updateGridOnRoadChange(e.getValue().getRoad());
						handleExport(e.getValue().getRoad(), e.getValue().getGroupName(), e.getValue().getName());
				}
	
				////////
				if (e.getValue()!=null) {
					getView().getRoadTextField().setValue(e.getValue().getRoad());
				}
//			}
    	});

	}

	public void setNetworkForValidators() {
		String network =stateManager.getCurrentNetwork();
		Set<NodeBean> allNodes = coherentSpaceService.getNetworkNodes(network);
		List<ClusterBean> allClusters = coherentSpaceService.getNetworkClusters(network);
		
		nodeInNetworkValidatorFromNodeTextField.setup(allNodes, network);
		nodeInNetworkValidatorToNodeTextField.setup(allNodes, network);
		nodesInNetworkValidator.setup(allNodes, network);
		clustersInNetworkValidator.setup(allClusters, network);
	}

    public void handleComputeRoad(ClickEvent<Button> buttonClickEvent) {
    	
    	List<Road> roads = computeRoad();
    	if (!roads.isEmpty()) {
    		getView().getRoadComboBox().setItems(roads);
    		getView().getRoadComboBox().setValue(roads.get(0));
    		
//    		stateManager.clearSelectionRoadLayersForCurrentNetwork();
//    		
//    		//refresh images
//        	roadMultiImagePresenter.composeOrRefreshImages();
    	}else {
    		clearRoadComboboxAndRoadGrid();
    		
    		//clear multiImage
        	stateManager.clearAllRoadLayersForCurrentNetwork();
	    	
        	//refresh images
        	roadMultiImagePresenter.composeOrRefreshImages();
    		Notification.show("Nothing to compute");
    	}
    	
    }
    
    private List<Road> computeRoad() {
    	//populate road combobox
    	
    	if (stateManager.currentState().getStartNode()!=null && stateManager.currentState().getEndNode()!=null) {
    		Notification.show("compute road");
    		List<List<NodeBean>> roadsAsNodeBeans = coherentSpaceService
    				.findRoad(stateManager.currentState().getStartNode(), 
            		stateManager.currentState().getEndNode(), stateManager.currentState().getIncludeNodes(),
            		new ArrayList<NodeBean>(),  
            		stateManager.currentState().getExcludeClusters()) 
    				.stream().collect(Collectors.toList());
    		if (roadsAsNodeBeans.size() > 0) {
    			stateManager.currentState().setRoad(roadsAsNodeBeans.stream().findFirst().get());
    		}else {
    			stateManager.currentState().setRoad(new ArrayList<>());
    		}
    		List<Road> roads = new ArrayList<>();
	    	if (!stateManager.currentState().getRoad().isEmpty()) {
	    		roads = roadsAsNodeBeans.stream().map(lnb->
	    			new Road(lnb.get(0).getNetwork(), null,
		    			lnb.stream().map(nb -> nb.getAddressIndex().toString()).collect(Collectors.joining(",")))
	    				).collect(Collectors.toList());
//		    	roadComboBox.setItems(roads);
//		    	roadComboBox.setValue(roads.get(0));
//		    	roadStateSelectionManager.processNodeSelection(null);
////		    	refresh images
//		    	roadPresenter.composeOrRefreshImages();
		    	return roads;
	    	}
		} /*
			 * else { Notification.show("Nothing to compute"); }
			 */
    	return new ArrayList<>();
    	//clear road grid
    }
    
    @EventBusListenerMethod
    public void onNodeRoadClickEvent(NodeRoadSelectionClickEvent event) {
    	NodeBean clickedNode = (NodeBean)event.getSource();
        //node selected
        if (clickedNode != null) {
	        //deselect nodes
	        if (getEntity().getFromNode() != null && 
	        		getEntity().getFromNode().equals(clickedNode.getAddressIndex())) {
	        	getView().getFromNodeTextField().setValue("");
//	        	getEntity().setFromNode(null);
	        	return;
	        }
	        if (getEntity().getToNode()!= null && getEntity().getToNode().equals(clickedNode.getAddressIndex())) {
	        	getView().getToNodeTextField().setValue("");
//	        	getEntity().setToNode(null);
	        	return;
	        }
	        
	        if (getEntity().getIncludeNodes()!=null && !getEntity().getIncludeNodes().isEmpty() && getEntity().getIncludeNodes().contains(clickedNode.getAddressIndex())){
	        	List<Long> newList = new ArrayList<Long>(getEntity().getIncludeNodes());
	        	newList.remove(clickedNode.getAddressIndex());
	        	getView().getIncludeNodesTextField().setValue(newList.stream().map(nb -> nb.toString()).collect(Collectors.joining(",")));
//	        	getEntity().getIncludeNodes().remove(clickedNode.getAddressIndex());
	        	
	        	return;
	        }
	        
	        //select from node
	        if (getEntity().getFromNode() == null) {
	        	getView().getFromNodeTextField().setValue(clickedNode.getAddressIndex()+"");
//	        	getEntity().setFromNode(clickedNode.getAddressIndex());
	        	return;
	        }

	        //select to node
	        if (getEntity().getToNode() == null) {
	        	getView().getToNodeTextField().setValue(clickedNode.getAddressIndex()+"");
//	        	getEntity().setToNode(clickedNode.getAddressIndex());
	        	return;
	        }
	
	        //select to include nodes (not cluster selected)
	        if (true) {
	        	List<Long> newList = new ArrayList<Long>(getEntity().getIncludeNodes());
	        	newList.add(clickedNode.getAddressIndex());
	        	getView().getIncludeNodesTextField().setValue(newList.stream().map(nb -> nb.toString()).collect(Collectors.joining(",")));
//	        	getEntity().getIncludeNodes().add(clickedNode.getAddressIndex());
	        	return;
	        }
        }	     
	}
    
    public void clearRoadFormComboboxAndRoadGrid() {
		
		getView().getFromNodeTextField().setValue("");
		getView().getToNodeTextField().setValue("");
		getView().getIncludeNodesTextField().setValue("");
		getView().getExcludeClustersTextField().setValue("");
		getView().getSearchSyntax().setValue("");

		clearRoadComboboxAndRoadGrid();
    }
    
	@Override
	public void onEntityFormStatusChange(StatusChangeEvent e) {
		if (getEntity()!=null) {
			try {
				stateManager.mapFormFieldsToCurrentState(
						getEntity().getFromNode()!=null?new NodeBean(stateManager.getCurrentNetwork(), getEntity().getFromNode()):null, 
						getEntity().getToNode() != null?new NodeBean(stateManager.getCurrentNetwork(), getEntity().getToNode()): null,
						getEntity().getIncludeNodes() != null && getEntity().getIncludeNodes().size()>0?
								(getEntity().getIncludeNodes().stream().map(adr -> new NodeBean(stateManager.getCurrentNetwork(), adr)).collect(Collectors.toList())):
								null,
						getEntity().getExcludeClusters() != null && !"".equals(getEntity().getExcludeClusters().trim())?
								(Stream.of(getEntity().getExcludeClusters().split("\\s*,\\s*")).map(adr -> new ClusterBean(stateManager.getCurrentNetwork(), Long.valueOf(adr))).collect(Collectors.toList())):
								null,		
						getEntity().getRoad() != null && !"".equals(getEntity().getRoad())? Arrays.asList(getEntity().getRoad().split(",")).stream().map(s->new NodeBean(stateManager.getCurrentNetwork(), new Long(s))).
				    			collect(Collectors.toList()): null);	
				
				//refresh images. clearRoadComboboxAndRoadGrid triggers again onFormStatusChange and we loose needRefresh from layers
				roadMultiImagePresenter.composeOrRefreshImages();
			} catch (Exception exception) {
				LOGGER.error(e.toString(), exception);
			}

//			if (getEntityValuesBeforeBinderUpdate() != null && getEntity() != null) {
//				//clear road on form changed (exept road)
//				if ((getEntityValuesBeforeBinderUpdate().getFromNode() != null && !getEntityValuesBeforeBinderUpdate().getFromNode().equals(getEntity().getFromNode())) ||
//						(getEntityValuesBeforeBinderUpdate().getToNode() != null && !getEntityValuesBeforeBinderUpdate().getToNode().equals(getEntity().getToNode())) ||
//						(getEntityValuesBeforeBinderUpdate().getIncludeNodes() != null && !getEntityValuesBeforeBinderUpdate().getIncludeNodes().equals(getEntity().getIncludeNodes())) ||
//						(getEntityValuesBeforeBinderUpdate().getExcludeClusters() != null && !getEntityValuesBeforeBinderUpdate().getExcludeClusters().equals(getEntity().getExcludeClusters())) ||
//						(getEntityValuesBeforeBinderUpdate().getSearchSyntax() != null && !getEntityValuesBeforeBinderUpdate().getSearchSyntax().equals(getEntity().getSearchSyntax()))) {
//					clearRoadComboboxAndRoadGrid();
//
//					//refresh images
//					roadMultiImagePresenter.composeOrRefreshImages();
//				}
//				
//				//update grid on road changed
//				if ((getEntityValuesBeforeBinderUpdate().getRoad() == null && getEntity().getRoad()!=null) ||
//					(getEntityValuesBeforeBinderUpdate().getRoad() != null && !getEntityValuesBeforeBinderUpdate().getRoad().equals(getEntity().getRoad()))){
//					updateGridOnRoadChange(getEntity().getRoad());
//					handleExport(getEntity());
//				}
//			}
		}		
	}

	@Override
	protected RoadStateSelectionManager getStateManager() {
		return stateManager;
	}

	@Override
	public RoadMultiImagePresenter getRoadMultiImagePresenter() {
		return roadMultiImagePresenter;
	}
}
