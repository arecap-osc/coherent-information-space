package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.appmanager;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.export.FeedbackExportAllColumnsRowsComponent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SpringComponent
@UIScope
public class FeedbackApplicationManagerView extends HorizontalLayout implements FlowView {
    @Autowired
    private FeedbackApplicationManagerPresenter presenter;

    @Autowired
    private FeedbackApplicationEditorDialogView feedbackApplicationEditorDialogView;
    
    @Autowired
    private FeedbackImportView feedbackImportView;
    
    @Autowired
    @Qualifier("feedbackExportAllColumnsRowsComponent")
    private FeedbackExportAllColumnsRowsComponent exportComponent;
    
    private ComboBox<FeedbackApplication> feedbackApplicationsComboBox = new ComboBox<>();

    private Button feedbackAppDialogButton = new Button(VaadinIcon.MENU.create());

    @Override
    public void buildView() {
        customizeView();
        buildEventListeners();
        buildViewWrapper();
    }

    private void customizeView() {
        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    @Override
    public void afterPrepareView() {
        if (feedbackApplicationsComboBox.getOptionalValue().isPresent() == false) {
            getPresenter().feedbackApplicationsGridComboBoxSetItemsSelectFirst();
        }
    }

    private void buildViewWrapper() {
        feedbackAppDialogButton.setWidth("20px");
        feedbackApplicationsComboBox.setItemLabelGenerator(FeedbackApplication::getLabel);
        feedbackApplicationsComboBox.setAllowCustomValue(false);
        feedbackApplicationsComboBox.getElement().setAttribute("title", "tooltip.currentfeedbackapp.combobox");
        feedbackAppDialogButton.getElement().setAttribute("title", "tooltip.editfeedbackapp.button");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setMargin(false);
        actions.setPadding(false);
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        actions.setHeight("40px");
        actions.setWidthFull();
        actions.add(feedbackApplicationsComboBox, feedbackAppDialogButton, feedbackImportView , exportComponent);

        add(actions);
    }

    private void buildEventListeners() {
        feedbackAppDialogButton.addClickListener(getPresenter()::handleDialogButtonClick);
        feedbackApplicationsComboBox.addValueChangeListener(getPresenter()::onSemanticGridComboBoxChangeEvent);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public FeedbackApplicationManagerPresenter getPresenter() {
        return presenter;
    }

    public FeedbackApplicationEditorDialogView getFeedbackApplicationEditorDialogView() {
        return feedbackApplicationEditorDialogView;
    }

    public ComboBox<FeedbackApplication> getFeedbackApplicationsComboBox() {
        return feedbackApplicationsComboBox;
    }
}
