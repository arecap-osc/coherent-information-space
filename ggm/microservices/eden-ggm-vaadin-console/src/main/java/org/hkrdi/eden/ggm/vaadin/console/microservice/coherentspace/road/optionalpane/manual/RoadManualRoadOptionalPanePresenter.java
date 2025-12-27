package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.manual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.NodeRoadSelectionClickEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual.RoadManualMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual.RoadManualRoadStateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.RoadOptionalPanePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator.NodesInNetworkStringValidator;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.StatusChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadManualRoadOptionalPanePresenter extends RoadOptionalPanePresenter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoadManualRoadOptionalPanePresenter.class);
	
    private NodesInNetworkStringValidator nodesInNetworkValidator = new NodesInNetworkStringValidator("");
    
	@Autowired
	private RoadManualRoadStateSelectionManager stateManager;
	
	@Autowired
	private RoadManualMultiImagePresenter roadMultiImagePresenter;
	
    @Autowired
    private CoherentSpaceService coherentSpaceService;
    
    @Override
    public RoadManualRoadOptionalPaneView getView() {
        return (RoadManualRoadOptionalPaneView)super.getView();
    }

    @Override
	public void prepareModel(EventObject event) {
		super.prepareModel(event);
		
		setNetworkForValidators();
		
		getBinder().forField(getView().getRoadTextField())
		.bind(Road::getRoad, Road::setRoad);
		
//		getBinder().forField(getView().getManualNodesTextField())
////			.withConverter(new StringToListLongConverter())
//			.withValidator(nodesInNetworkValidator)
//			.bind(Road::getManualNodes, Road::setManualNodes);
//		
//		getView().getRoadComboBox().setItems(new ArrayList<>());
//		getBinder().forField(getView().getRoadComboBox())
//			.withConverter(new StringToRoadConverter())
//			.withNullRepresentation("")
//			.bind(Road::getRoad, Road::setRoad);
		
		getView().getRoadTextField().addValueChangeListener(e -> {
			//update grid on road changed
			if (e.getValue()!=null) {
				updateGridOnRoadChange(e.getValue());
				handleExport(e.getValue(), getEntity().getGroupName(), getEntity().getName());
			}
    	});
	}
    
	public void setNetworkForValidators() {
		String network =stateManager.getCurrentNetwork();
		Set<NodeBean> allNodes = coherentSpaceService.getNetworkNodes(network);
		
		nodesInNetworkValidator.setup(allNodes, network);
	}

	public void handleComputeRoad(ClickEvent<Button> buttonClickEvent) {}
	
    @EventBusListenerMethod
    public void onNodeRoadClickEvent(NodeRoadSelectionClickEvent event) {
    	NodeBean clickedNode = (NodeBean)event.getSource();
        //node selected
        if (clickedNode != null) {	        
        	List<Long> manualNodes = new ArrayList<>();
        	if (getEntity().getRoad()!=null && !"".equals(getEntity().getRoad().trim())){
        		manualNodes =	Arrays.asList(getEntity().getRoad().split("\\s*,\\s*")).stream().map(m->Long.valueOf(m)).collect(Collectors.toList());
        	}
	        if (!manualNodes.isEmpty() && 
	        		manualNodes.get(manualNodes.size()-1).equals(clickedNode.getAddressIndex())){
	        	List<Long> newList = new ArrayList<Long>(manualNodes);
	        	newList.remove(clickedNode.getAddressIndex());
	        	getView().getRoadTextField().setValue(newList.stream().map(nb -> nb.toString()).collect(Collectors.joining(",")));
//	        	manualNodes.remove(clickedNode.getAddressIndex());
	        	
	        	return;
	        }
	        
	        //select to include nodes (not cluster selected)
	        //check if node
	        boolean isInToNodeNeighbors = false;
	        if (!manualNodes.isEmpty()) {
	        	Long lastNode = manualNodes.get(manualNodes.size()-1);
	        	List<NodeBean> toNodeNeighbors = coherentSpaceService.findToNodeNeighbors(new NodeBean(stateManager.getCurrentNetwork(),lastNode));
	        	if (toNodeNeighbors.contains(clickedNode)) {
	        		isInToNodeNeighbors = true;
	        	}
	        }
	        if (manualNodes.isEmpty() || isInToNodeNeighbors) {
	        	List<Long> newList = new ArrayList<Long>(manualNodes);
	        	newList.add(clickedNode.getAddressIndex());
	        	getView().getRoadTextField().setValue(newList.stream().map(nb -> nb.toString()).collect(Collectors.joining(",")));
//	        	manualNodes.add(clickedNode.getAddressIndex());
	        	return;
	        }
        }	     
	}
    
    public void clearRoadFormComboboxAndRoadGrid() {
		clearRoadComboboxAndRoadGrid();
    }
    
    @Override
	public void onEntityFormStatusChange(StatusChangeEvent e) {
		if (getEntity()!=null) {// && e.getBinder()!=null && e.getBinder().getBean()!=null) {
			try {
				Long lastSelectedNode = null;
				
				List<Long> manualNodes = new ArrayList<>();
	        	if (//((Road)e.getBinder().getBean()).getManualNodes()!=null && !"".equals(((Road)e.getBinder().getBean()).getManualNodes().trim())){
	        			getEntity().getRoad()!=null && !"".equals(getEntity().getRoad().trim())){
	        		manualNodes =	Arrays.asList(getEntity().getRoad().split("\\s*,\\s*")).stream().map(m->Long.valueOf(m)).collect(Collectors.toList());
	        				//((Road)e.getBinder().getBean()).getManualNodes().split("\\s*,\\s*")).stream().map(m->Long.valueOf(m)).collect(Collectors.toList());
	        	}
	        	
				if (manualNodes != null && manualNodes.size()>0) {
//					if (getEntityValuesBeforeBinderUpdate().getManualNodes() != null && getEntityValuesBeforeBinderUpdate().getManualNodes().size()>0) {
//						
//					}else {
						lastSelectedNode = manualNodes.get(manualNodes.size()-1);
//					}
				}
				
				getEntity().setRoad(manualNodes.stream().map(m->m.toString()).collect(Collectors.joining(",")));
				
				stateManager.mapFormFieldsToCurrentState(
						lastSelectedNode!=null?new NodeBean(stateManager.getCurrentNetwork(), lastSelectedNode):null, 
						null,
						manualNodes != null && manualNodes.size()>0?
								(manualNodes.stream().map(adr -> new NodeBean(stateManager.getCurrentNetwork(), adr)).collect(Collectors.toList())):null,
						null,		
						getEntity().getRoad() != null && !"".equals(getEntity().getRoad().trim())? Arrays.asList(getEntity().getRoad().split("\\s*,\\s*")).stream().map(s->new NodeBean(stateManager.getCurrentNetwork(), new Long(s))).
				    			collect(Collectors.toList()): null);	
				
				//refresh images. clearRoadComboboxAndRoadGrid triggers again onFormStatusChange and we loose needRefresh from layers
				roadMultiImagePresenter.composeOrRefreshImages();
			} catch (Exception exception) {
				LOGGER.error(e.toString(), exception);
			}

//			if (getEntityValuesBeforeBinderUpdate() != null && getEntity() != null) {
////				//clear road on form changed (exept road)
////				if (getEntityValuesBeforeBinderUpdate().getManualNodes() != null && !getEntityValuesBeforeBinderUpdate().getManualNodes().equals(getEntity().getManualNodes())){
////					clearRoadComboboxAndRoadGrid();
////
////					//refresh images
////					roadMultiImagePresenter.composeOrRefreshImages();
////				}
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
