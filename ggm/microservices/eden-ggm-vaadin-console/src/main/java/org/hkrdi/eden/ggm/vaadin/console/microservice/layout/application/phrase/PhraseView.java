package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.phrase;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;


@SpringComponent
@UIScope
@Tag("phrase-form")
@HtmlImport("frontend://vaadin/coherentspace/phrase-form.html")
@Primary
public class PhraseView extends PolymerTemplate<TemplateModel> implements FlowEntityView {

    @Autowired
    private PhrasePresenter presenter;

    @Id("save-btn")
    private Button saveBtn;
    @Id("whoWhat")
    private TextArea whoWhat;
    @Id("how")
    private TextArea how;
    @Id("why")
    private TextArea why;
    @Id("whereWhen")
    private TextArea whereWhen;
    @Id("phraseIdFromNode")
    private Label phraseIdFromNode;
    @Id("phraseIdRoute")
    private Label phraseIdRoute;
    @Id("phraseIdToNode")
    private Label phraseIdToNode;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public void prepareView() {
        setVisible(getPresenter().getInitialEntityId() != null);
    }

    public void setFromNodeText(String fromNodeText) {
        phraseIdFromNode.setText(fromNodeText);
    }

    public void setRouteText(String routeText) {
        phraseIdRoute.setText(routeText);
    }

    public void setToNodeText(String toNodeText) {
        phraseIdToNode.setText(toNodeText);
    }

    @Override
    public PhrasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return saveBtn;
    }

	public Button getSaveBtn() {
		return saveBtn;
	}

	public TextArea getWhoWhat() {
		return whoWhat;
	}

	public TextArea getHow() {
		return how;
	}

	public TextArea getWhy() {
		return why;
	}

	public TextArea getWhereWhen() {
		return whereWhen;
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		FlowEntityView.super.localeChange(event);
		saveBtn.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
	    how.setLabel(GgmI18NProviderStatic.getTranslation("msg.phrase.view.how"));
	    why.setLabel(GgmI18NProviderStatic.getTranslation("msg.phrase.view.why"));
	    whereWhen.setLabel(GgmI18NProviderStatic.getTranslation("msg.phrase.view.wherewhen"));
	    whoWhat.setLabel(GgmI18NProviderStatic.getTranslation("msg.phrase.view.whowhat"));
//	    phraseIdFromNode.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
//	    phraseIdRoute.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
//	    phraseIdToNode.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));

	}
}
