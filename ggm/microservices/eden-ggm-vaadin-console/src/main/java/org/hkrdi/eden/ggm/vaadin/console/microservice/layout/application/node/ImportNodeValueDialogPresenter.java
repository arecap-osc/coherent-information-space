package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.service.ETLRCService;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class ImportNodeValueDialogPresenter extends ApplicationDataPresenter {

    private ApplicationDataPresenter requester;

    @Autowired
    private ETLRCService etlService;

    @Override
    public Long getInitialEntityId() {
        return requester.getInitialEntityId();
    }

    @Override
    public ImportNodeValueDialogView getView() {
        return (ImportNodeValueDialogView)super.getView();
    }

    public List<Etl> getNotImported() {
        return etlService.findAllByApplicationAndNotUsedInApplication(
                getEntity().getApplication(), ETLRCService.getLevel(getEntity().getNetwork()));
    }

    public List<Etl> getImported() {
        return etlService.findAllByApplicationAndUsedInApplication(
                getEntity().getApplication(), ETLRCService.getLevel(getEntity().getNetwork()));
    }
    
    public void openFromRequester(ApplicationDataPresenter applicationDataPresenter) {
        requester = applicationDataPresenter;
        prepareModelAndView(null);
        getView().open();
    }

    @Override
    public void afterSave() {
        super.afterSave();
        Optional<Etl> selectedEtl = getView().getSelectedForNodeField();
        if(selectedEtl.isPresent()) {
            etlService.persistEtlUsedInApplication(selectedEtl.get(), getEntity().getAddressIndex().intValue());
        }
        requester.prepareModelAndView(null);
        getUIEventBus().publish(this, new InformationSavedEvent());
        getView().close();
    }

	public void reset() {
		
		Optional<Etl> selectedEtl =  getImported().stream().filter(etl->etl.getNode() == (getEntity().getAddressIndex().intValue())).findFirst();
        if(selectedEtl.isPresent()) {
            etlService.persistEtlNotUsedInApplication(selectedEtl.get(), getEntity().getAddressIndex().intValue());
        }
        requester.prepareModelAndView(null);
        getView().close();
	}

}
