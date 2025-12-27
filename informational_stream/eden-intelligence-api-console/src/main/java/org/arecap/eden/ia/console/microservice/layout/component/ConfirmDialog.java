package org.arecap.eden.ia.console.microservice.layout.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.arecap.eden.ia.console.common.i18n.I18NProviderStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmDialog extends Dialog{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmDialog.class);

    private Button save = new Button(I18NProviderStatic.getTranslation("button.ok.label"));

    private HorizontalLayout actions = new HorizontalLayout();

    public ConfirmDialog(String title, String content, ComponentEventListener<ComponentEvent<Dialog>> saveListener) {
		setWidth("400px");
		actions.add(save);
		VerticalLayout editorForm = new VerticalLayout();
		Label titleLabel = new Label(I18NProviderStatic.getTranslation(title));
		titleLabel.getStyle().set("font-weight", "bold");

		editorForm.add(titleLabel, new Label(I18NProviderStatic.getTranslation(content)), actions);
		add(editorForm);
		
//		save.getStyle().set("position", "absolute");
//		save.getStyle().set("right", "0");
		save.getElement().getThemeList().add("primary");
		save.addClickListener(e -> {
			saveListener.onComponentEvent(getSaveEvent());
			this.close();
		});
	}
    
    public ConfirmDialog(String title, String content,
    		String confirmationKey,
    		ComponentEventListener<ComponentEvent<Dialog>> saveListener,
    		String cancelKey,
    		ComponentEventListener<ComponentEvent<Dialog>> cancelListener) {
		setWidth("400px");
		
		Button ok = new Button(I18NProviderStatic.getTranslation(confirmationKey));
		Button cancel = new Button(I18NProviderStatic.getTranslation(cancelKey));
		actions.add(ok,cancel);
		VerticalLayout editorForm = new VerticalLayout();
		Label titleLabel = new Label(I18NProviderStatic.getTranslation(title));
		titleLabel.getStyle().set("font-weight", "bold");

		editorForm.add(titleLabel, new Label(I18NProviderStatic.getTranslation(content)), actions);
		add(editorForm);
		
//		save.getStyle().set("position", "absolute");
//		save.getStyle().set("right", "0");
		ok.getElement().getThemeList().add("primary");
		ok.addClickListener(e -> {
			saveListener.onComponentEvent(getSaveEvent());
			this.close();
		});
		
		cancel.getElement().getThemeList().add("primary");
		cancel.addClickListener(e -> {
			cancelListener.onComponentEvent(getCancelEvent());
			this.close();
		});
	}
	
	public ComponentEvent<Dialog> getSaveEvent() {
		return new ComponentEvent<Dialog>(this, true);
	}
	
	public ComponentEvent<Dialog> getCancelEvent() {
		return new ComponentEvent<Dialog>(this, false);
	}
}
