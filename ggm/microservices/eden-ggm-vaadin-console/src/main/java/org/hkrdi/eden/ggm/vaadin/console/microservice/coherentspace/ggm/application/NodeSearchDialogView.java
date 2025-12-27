package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public abstract class NodeSearchDialogView extends Dialog {

    @Autowired
    private ApplicationDataRepositoryService applicationService;
    
    @Autowired
    private CoherentSpaceService coherentSpaceService;

    private Grid<ApplicationData> grid;

    private TextField filter;

    private Application application;
    
    private List<String> networks;

    @PostConstruct
    protected void setup() {
    	
        this.grid = new Grid<>(ApplicationData.class);
        this.filter = new TextField();
        filter.getStyle().set("min-width", "500px");
//        this.addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter);
        add(actions, grid);

        grid.getStyle().set("min-width", "500px");
        grid.getStyle().set("width", "1000px");
        grid.setHeight("600px");
        grid.setColumns("network", "clusterIndex", "addressIndex");
        
        afterSetup();
        
//        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listSemantic(e.getValue()));
        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
        	if (e.getValue() == null) {return;}
        	List<DataMap> routes = coherentSpaceService.getNetworkDataMaps(
        			e.
        			getValue().
        			getNetwork());
        	if (routes == null) {
        		return;
        	}
            DataMap dm = routes
                        .stream()
                        .filter(DataMapFilterUtil.byId(e.getValue().getDataMapId())).findFirst().get();
        	if (dm != null ) {
        	    //TODO node selection
//        		SyntaxSelectionChangeEvent.push(dm, uIEventBus);
        		close();
        	}
        });

        // Initialize listing
        listSemantic(null);
    }


    protected void afterSetup() {
    }
    
    protected abstract List<ApplicationData> filterFunction(Long applicationId, List<String> networks, String filter);
    
    private void listSemantic(String filter) {
    	if (application == null || networks == null) {
    		return;
    	}
//        if(changeHandler != null) {
//            changeHandler.onChange();
//        }
        //TODO push on change
        if (StringUtils.isEmpty(filter)) {
        	grid.setItems(new ArrayList<ApplicationData>());
        }
        else {
        	List<ApplicationData> ad = filterFunction(application.getId(), networks, filter);
            grid.setItems(ad);
        }
    }

//    public void setChangeHandler(ChangeHandler h) {
//        // ChangeHandler is notified when either save or delete
//        // is clicked
//        changeHandler = h;
//    }


    public void openFor(Application application) {
		this.application = application;
		filter.setValue("");
		this.open();
	}


	public Grid<ApplicationData> getGrid() {
		return grid;
	}


	public Application getApplication() {
		return application;
	}
    
}
