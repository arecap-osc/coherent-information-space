package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.event.PhraseChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class FirstNodeView extends AbstractNodeView {


    private Button importBtn = new Button("button.import.label");

    @Autowired
    private ImportNodeValueDialogView importNodeValueDialogView;

    @Autowired
    private FirstNodePresenter presenter;


    @Override
    public FirstNodePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        super.buildView();
        importBtn.addClassName("node-buttons");
        getActionButtonsLayout().add(importBtn);
        importBtn.addClickListener(this::handleImportEvent);
    }
    
    private void handleImportEvent(ClickEvent<Button> buttonClickEvent) {
        importNodeValueDialogView.getPresenter().openFromRequester(getPresenter());
    }

    @Override
    protected void handleNodeTextValueChange() {
        if(getPresenter().getApplicationDataIe().getSecondNodeId() != null &&
                getPresenter().getApplicationDataIe().getSyntaxId() != null &&
                getPresenter().getApplicationDataIe().getSecondNodeId()
                        .compareTo(getPresenter().getApplicationDataIe().getSyntaxId()) == 0 ) {
            getPresenter().getUIEventBus().publish(this, new PhraseChangeEvent.ToSemanticChange(getSemantic().getValue()));
        } else {
        	getPresenter().getUIEventBus().publish(this, new PhraseChangeEvent.FromSemanticChange(getSemantic().getValue()));
        }
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
    	super.localeChange(event);
    	importBtn.setText(GgmI18NProviderStatic.getTranslation("button.import.label"));
    }
}
