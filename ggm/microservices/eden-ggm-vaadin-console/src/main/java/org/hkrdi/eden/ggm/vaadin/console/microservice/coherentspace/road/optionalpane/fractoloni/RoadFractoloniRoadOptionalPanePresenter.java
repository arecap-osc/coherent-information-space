package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.fractoloni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.NodeRoadSelectionClickEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.fractoloni.RoadFractoloniMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.fractoloni.RoadFractoloniRoadStateSelectionManager;
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
public class RoadFractoloniRoadOptionalPanePresenter extends RoadOptionalPanePresenter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoadFractoloniRoadOptionalPanePresenter.class);
	
    private NodesInNetworkStringValidator nodesInNetworkValidator = new NodesInNetworkStringValidator("");
    
	@Autowired
	private RoadFractoloniRoadStateSelectionManager stateManager;
	
	@Autowired
	private RoadFractoloniMultiImagePresenter roadMultiImagePresenter;
	
    @Autowired
    private CoherentSpaceService coherentSpaceService;
    
    @Override
    public RoadFractoloniRoadOptionalPaneView getView() {
        return (RoadFractoloniRoadOptionalPaneView)super.getView();
    }

    @Override
	public void prepareModel(EventObject event) {
		super.prepareModel(event);
		
		setNetworkForValidators();
		
//		getBinder().forField(getView().getGroupNameTextField())
//		.bind(Road::getGroupName, Road::setGroupName);
//		
//		getBinder().forField(getView().getRoadNameTextField())
//		.bind(Road::getName, Road::setName);
		
		getBinder().forField(getView().getRoadTextField())
		.bind(Road::getRoad, Road::setRoad);
		
		getBinder().forField(getView().getFractolonTextField())
		.bind(Road::getFractolon, Road::setFractolon);
		
//		getView().getRoadComboBox().setItems(new ArrayList<>());
//		getBinder().forField(getView().getRoadComboBox())
//			.withConverter(new StringToRoadConverter())
//			.withNullRepresentation("")
//			.bind(Road::getRoad, Road::setRoad);
		
		getView().getGroupComboBox().addValueChangeListener(e ->{
			if (e.getValue()!=null && !"".equals(e.getValue().trim())) {
				List<Road> roads =  getService().getRoadsByNetworkAndGroupName(stateManager.getCurrentNetwork(), e.getValue());
				getView().getRoadComboBox().setItems(roads);
				if (roads!=null && roads.size()>0) {
					getView().getRoadComboBox().setValue(roads.get(0));
				}
			}
		});
		
		getView().getRoadComboBox().setItemLabelGenerator(i->i.getName()+(i.getName()!=null&&i.getRoad()!=null?"-":"")+i.getRoad() );
		getView().getRoadComboBox().addValueChangeListener(e->{
			this.setEntity(e.getValue());
//			getView().getRoadTextField().setValue(e.getValue().getRoad());
			
//			getStateManager().currentState().setIncludeNodes(Arrays.asList(e.getValue().getFractolon().split("\\s*,\\s*")).stream()
//					.map(s-> new NodeBean(stateManager.getCurrentNetwork(), Long.valueOf(s))).collect(Collectors.toList()));
		});
		
		getView().getRoadTextField().addValueChangeListener(e -> {
			//update grid on road changed
			if (e.getValue()!=null) {
				updateGridOnRoadChange(e.getValue());
				if (getEntity()!=null) {
					handleExport(e.getValue(), getEntity().getGroupName(), getEntity().getName());
				}
			}
    	});
	}
    
    
    
	public void setNetworkForValidators() {
		String network = stateManager.getCurrentNetwork();
		Set<NodeBean> allNodes = coherentSpaceService.getNetworkNodes(network);
		
		nodesInNetworkValidator.setup(allNodes, network);
		
		setGroupNameAndRoadNameCombox(network);
	}

	private void setGroupNameAndRoadNameCombox(String network) {
		List<String> fractoloni = getService().getGroupNamesAndFractolon(network);
		getView().getGroupComboBox().setItems(fractoloni);
		if (fractoloni!=null && fractoloni.size()>0) {
			getView().getGroupComboBox().setValue(fractoloni.get(0));
		}
	}

	public void handleComputeRoad(ClickEvent<Button> buttonClickEvent) {}
	
    @EventBusListenerMethod
    public void onNodeRoadClickEvent(NodeRoadSelectionClickEvent event) {}
    
    public void clearRoadFormComboboxAndRoadGrid() {
		clearRoadComboboxAndRoadGrid();
    }
    
    @Override
	public void onEntityFormStatusChange(StatusChangeEvent e) {
		if (getEntity()!=null) {// && e.getBinder()!=null && e.getBinder().getBean()!=null) {
			try {
				stateManager.mapFormFieldsToCurrentState(
						null, 
						null,
						getEntity().getFractolon()!=null && !"".equals(getEntity().getFractolon().trim())?Arrays.asList(getEntity().getFractolon().split("\\s*,\\s*")).stream()
							.map(s-> new NodeBean(stateManager.getCurrentNetwork(), Long.valueOf(s))).collect(Collectors.toList()):null,
						null,		
						getEntity().getRoad() != null && !"".equals(getEntity().getRoad().trim())? Arrays.asList(getEntity().getRoad().split("\\s*,\\s*")).stream().map(s->new NodeBean(stateManager.getCurrentNetwork(), new Long(s))).
				    			collect(Collectors.toList()): null);	
				
				//refresh images. clearRoadComboboxAndRoadGrid triggers again onFormStatusChange and we loose needRefresh from layers
				roadMultiImagePresenter.composeOrRefreshImages();
			} catch (Exception exception) {
				LOGGER.error(e.toString(), exception);
			}

		}		
	}

	@Override
	protected RoadFractoloniRoadStateSelectionManager getStateManager() {
		return stateManager;
	}

	@Override
	public RoadMultiImagePresenter getRoadMultiImagePresenter() {
		return roadMultiImagePresenter;
	}
}
