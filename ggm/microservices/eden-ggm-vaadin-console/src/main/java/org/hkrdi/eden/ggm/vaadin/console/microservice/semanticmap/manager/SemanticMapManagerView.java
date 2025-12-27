package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.manager;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.SemanticMapImportView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class SemanticMapManagerView extends HorizontalLayout implements FlowView {

    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticMapManagerView.class);

    private ComboBox<SemanticMap> semanticMapComboBox = new ComboBox<>();

    private Button editBtn = new Button(VaadinIcon.MENU.create());

    private Button importBtn = new Button(VaadinIcon.UPLOAD.create());

    @Autowired
    private SemanticMapImportView semanticMapImportView;

    @Autowired
    private SemanticMapManagerPresenter semanticMapEntityPresenter;

    @Override
    public void buildView() {
        setWidthFull();
        setPadding(false);
//        getStyle().set("border-bottom", "1px solid black");
        VerticalLayout vertical = new VerticalLayout();
        HorizontalLayout div = new HorizontalLayout();
        div.add(semanticMapComboBox, editBtn, importBtn);
        div.setWidthFull();
        vertical.add(div);
        add(vertical);

        semanticMapComboBox.getElement().setAttribute("title", "tooltip.currentsemmap.combobox");
        editBtn.getElement().setAttribute("title", "tooltip.editsemmap.button");
        importBtn.getElement().setAttribute("title", "tooltip.importsemmap.button");

        editBtn.addClickListener(getPresenter()::handleEditEvent);
        importBtn.addClickListener(getPresenter()::handleImportEvent);

        semanticMapComboBox.setItemLabelGenerator(SemanticMap::getLabel);
        semanticMapComboBox.setAllowCustomValue(false);
        semanticMapComboBox.addValueChangeListener(getPresenter()::onSemanticMapComboBoxChangeEvent);
    }

    @Override
    public void afterPrepareView() {
        if (semanticMapComboBox.getOptionalValue().isPresent() == false) {
            getPresenter().semanticMapComboBoxSetItemsSelectFirst();
        }
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public SemanticMapManagerPresenter getPresenter() {
        return semanticMapEntityPresenter;
    }

    public ComboBox<SemanticMap> getSemanticMapComboBox() {
        return semanticMapComboBox;
    }

    public SemanticMapImportView getSemanticMapImportView() {
        return semanticMapImportView;
    }
}
