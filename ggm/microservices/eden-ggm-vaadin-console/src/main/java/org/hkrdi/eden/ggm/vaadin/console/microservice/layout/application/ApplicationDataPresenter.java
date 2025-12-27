package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ApplicationDataWellsAndRoutesNotifier;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.edge.EdgeView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node.AbstractNodeView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ApplicationDataPresenter extends DefaultFlowEntityPresenter<ApplicationData, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDataPresenter.class);

    @Autowired
    protected ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    private DataMap dataMap;

    @Override
    public Class<ApplicationData> getEntityType() {
        return ApplicationData.class;
    }

    @Override
    public ApplicationDataRepositoryService getService() {
        return applicationDataService;
    }

    @Override
    public void afterSave() {
        ApplicationDataWellsAndRoutesNotifier.showFor(getBinder().getBean());
        getUIEventBus().publish(this, new InformationSavedEvent());
    }

    @Override
    public void prepareModel(EventObject event) {
        super.prepareModel(event);
        if (getInitialEntityId() != null) {
            dataMap = coherentSpaceService.getNetworkDataMaps(getBinder().getBean().getNetwork()).stream()
                    .filter(DataMapFilterUtil.byDataMapId(getBinder().getBean().getDataMapId())).findFirst().orElse(null);
        } else {
            dataMap = null;
        }
    }

    public DataMap getDataMap() {
        return dataMap;
    }

    public GgmRouteApplicationDataIe getApplicationDataIe() {
        return applicationDataIe;
    }

    public List<Long> getApplicationDataAsNodeWellsIds() {
        return getApplicationDataAsNodeWells().stream()
                .map(ApplicationData::getId)
                .collect(Collectors.toList());
    }

    public List<Long> getApplicationDataAsEdgeWellsIds() {
        return getApplicationDataAsEdgeWells().stream()
                .map(ApplicationData::getId)
                .collect(Collectors.toList());
    }

    public List<ApplicationData> getApplicationDataAsEdgeWells() {
        if (getInitialEntityId() == null) {
            return new ArrayList<>();
        }
        EdgeBean edgeBean = new EdgeBean(new NodeBean(getEntity().getNetwork(), getEntity().getAddressIndex()),
                new NodeBean(getEntity().getNetwork(), getEntity().getToAddressIndex()));
        Optional<DataMap> dataMapOpt = coherentSpaceService
                .findEdgeDataMap(edgeBean);
        if (!dataMapOpt.isPresent()) {
            LOGGER.info("The dataMap index "+getEntity().getNetwork()+"::"+getEntity().getDataMapId()+" not found from application_data index "+ getInitialEntityId());
            return Arrays.asList(new ApplicationData[] {getEntity()});
        }
        List<Long> dataMapIds = coherentSpaceService.findEdgeWells(edgeBean).stream().map(DataMap::getId).collect(Collectors.toList());
        List<ApplicationData> applicationIds = applicationDataService.findAllByApplicationIdAndDataMapIdIn(getEntity().getApplication().getId(), dataMapIds);
        if (applicationIds.isEmpty()) {
            LOGGER.info("The appication ids for application data id"+getInitialEntityId()+" not found ");
            return Arrays.asList(new ApplicationData[] {getEntity()});
        }
        return applicationIds;
    }

    public List<ApplicationData> getApplicationDataAsNodeWells() {
        if (getInitialEntityId() == null) {
            return new ArrayList<>();
        }
        Optional<DataMap> dataMapOpt = coherentSpaceService
                .findNodeDataMap(new NodeBean(getEntity().getNetwork(), getEntity().getAddressIndex()));
        if (!dataMapOpt.isPresent()) {
            LOGGER.info("The dataMap index " + getEntity().getNetwork() + "::" + getEntity().getDataMapId() + " not found from application_data index " + getInitialEntityId());
            return Arrays.asList(new ApplicationData[]{getEntity()});
        }
        NodeBean node = new NodeBean(dataMapOpt.get().getNetwork(), dataMapOpt.get().getAddressIndex());
        List<Long> dataMapIds = coherentSpaceService.findNodeWells(node).stream().map(DataMap::getId).collect(Collectors.toList());
        List<ApplicationData> applicationIds = applicationDataService.findAllByApplicationIdAndDataMapIdIn(getEntity().getApplication().getId(),
                dataMapIds);
        if (applicationIds.isEmpty()) {
            LOGGER.info("The appication ids for application data id" + getInitialEntityId() + " not found ");
            return Arrays.asList(new ApplicationData[]{getEntity()});
        }
        return applicationIds;
    }

    public CoherentSpaceService getCoherentSpaceService() {
        return coherentSpaceService;
    }

    public void onNodeWellsComboBoxValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<ApplicationData>, ApplicationData> comboBoxApplicationDataComponentValueChangeEvent) {
        AbstractNodeView view = (AbstractNodeView) getView();
        if (comboBoxApplicationDataComponentValueChangeEvent.getHasValue().isEmpty() == false) {
            ApplicationData applicationData = comboBoxApplicationDataComponentValueChangeEvent.getValue();
            Optional<DataMap> dataMapOptional = getCoherentSpaceService().getNetworkDataMaps(applicationData.getNetwork())
                    .stream().filter(DataMapFilterUtil.byId(applicationData.getDataMapId()))
                    .findAny();
            if(dataMapOptional.isPresent()) {
                DataMap dataMap = dataMapOptional.get();
                view.getLogicalFunction().setText(dataMap.getTrivalentLogic() + " " + dataMap.getTrivalentLogicType().replace('_', ' '));
            }

            return;
        }
        view.getLogicalFunction().setText("");
    }

    public void onEdgeWellsComboBoxValueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<ApplicationData>, ApplicationData> comboBoxApplicationDataComponentValueChangeEvent) {
        EdgeView view = (EdgeView) getView();
        if (comboBoxApplicationDataComponentValueChangeEvent.getHasValue().isEmpty() == false) {
            ApplicationData applicationData = comboBoxApplicationDataComponentValueChangeEvent.getValue();
            Optional<DataMap> dataMapOptional = getCoherentSpaceService().getNetworkDataMaps(applicationData.getNetwork())
                    .stream().filter(DataMapFilterUtil.byId(applicationData.getDataMapId()))
                    .findAny();
            if(dataMapOptional.isPresent()) {
                DataMap dataMap = dataMapOptional.get();
                view.getLogicalFunction().setText(dataMap.getTrivalentLogic() + " " + dataMap.getTrivalentLogicType().replace('_', ' ') +
                                "->" + dataMap.getToTrivalentLogic() + " " + dataMap.getToTrivalentLogicType().replace('_', ' '));
            }

            return;
        }
        view.getLogicalFunction().setText("");

    }

    public String wrapApplicationDataLabel(ApplicationData appData) {
        return appData.getNetwork().replace('_', '/')
                .replace("::", "/") + "/" + appData.getClusterIndex() + "/" + appData.getAddressIndex();
    }
}
