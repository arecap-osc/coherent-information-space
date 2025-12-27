package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.templatemodel.TemplateModel;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;

@Tag("map-word")
@HtmlImport("frontend://vaadin/semanticmap/map-word.html")
public abstract class AbstractMapWordView extends PolymerTemplate<TemplateModel> implements FlowEntityView {
    @Id("letter")
    private TextField letter;

    @Id("word")
    private TextArea word;

    @Id("details")
    private TextArea details;

    @Id("saveBtn")
    private Button save;

    @Id("cancelBtn")
    private Button cancel;

    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        save.setIcon(VaadinIcon.DISC.create());
        cancel.setIcon(VaadinIcon.CLOSE_SMALL.create());
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    public Button getCancel() {
        return cancel;
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
    	FlowEntityView.super.localeChange(event);
    	
    	save.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
    	cancel.setText(GgmI18NProviderStatic.getTranslation("button.cancel.label"));
    	
    	letter.setLabel(GgmI18NProviderStatic.getTranslation("msg.map.word.letter"));
    	word.setLabel(GgmI18NProviderStatic.getTranslation("msg.map.word.semantic"));
    	details.setLabel(GgmI18NProviderStatic.getTranslation("msg.map.word.details"));
    }
}
