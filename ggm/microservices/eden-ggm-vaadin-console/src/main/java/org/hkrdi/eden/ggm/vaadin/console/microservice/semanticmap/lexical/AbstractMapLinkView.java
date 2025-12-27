package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.templatemodel.TemplateModel;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;

@Tag("map-link")
@HtmlImport("frontend://vaadin/semanticmap/map-link.html")
public abstract class AbstractMapLinkView extends PolymerTemplate<TemplateModel> implements FlowEntityView {

    @Id("linkTitle")
    private Label linkTitle;

    @Id("verb")
    private TextField verb;

    @Id("conjunction")
    private TextArea conjunction;

    @Id("phrase")
    private TextArea phrase;

    @Id("details")
    private TextArea details;

    @Id("saveBtn")
    private Button saveBtn;

    @Id("verbalization")
    private TextArea verbalisation;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public Button getSaveButton() {
        return saveBtn;
    }

    public TextField getVerb() {
        return verb;
    }

    public TextArea getConjunction() {
        return conjunction;
    }

    public TextArea getPhrase() {
        return phrase;
    }

    public TextArea getDetails() {
        return details;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public TextArea getVerbalisation() {
        return verbalisation;
    }

    public Label getLinkTitle() {
        return linkTitle;
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
    	FlowEntityView.super.localeChange(event);
    	
    	saveBtn.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
    	
        details.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.syntax.details"));
        verb.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.verb"));
        conjunction.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.conjunction"));
        verbalisation.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.verbalization"));
        phrase.setLabel(GgmI18NProviderStatic.getTranslation("msg.edge.view.phrase"));
    }
}
