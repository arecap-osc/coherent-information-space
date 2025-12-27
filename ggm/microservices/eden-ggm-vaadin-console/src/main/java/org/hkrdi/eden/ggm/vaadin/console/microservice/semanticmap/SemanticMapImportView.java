package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class SemanticMapImportView extends Dialog implements FlowView {
    @Autowired
    private SemanticMapImportPresenter presenter;

    private Label instructionsLabel = new Label();
    private Label commandLabel = new Label();

    private TextArea insertStatementsTextArea = new TextArea();

    private Label specificSemanticMapLabel = new Label();
    private Checkbox specificSemanticMapCheckBox = new Checkbox();
    private TextField specificSemanticMapTextField = new TextField();

    private Button importButton = new Button("button.import.label");

    @Override
    public void buildView() {
        customizeView();
        buildViewWrappers();
        buildEventListeners();
    }

    private void buildEventListeners() {
        specificSemanticMapCheckBox.addValueChangeListener(getPresenter()::onCheckBoxValueChanged);
        importButton.addClickListener(getPresenter()::onImportButtonClick);
    }

    private void buildViewWrappers() {
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout specificSemanticMapLayout = new HorizontalLayout();
        specificSemanticMapLayout.add(specificSemanticMapTextField, specificSemanticMapCheckBox);
        specificSemanticMapLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        verticalLayout.add(instructionsLabel, commandLabel, insertStatementsTextArea, specificSemanticMapLabel, specificSemanticMapLayout, importButton);
        add(verticalLayout);
    }

    private void customizeView() {
        setWidth("700px");
        insertStatementsTextArea.setWidthFull();
        insertStatementsTextArea.setHeight("450px");
        insertStatementsTextArea.getStyle().set("overflow", "auto");
        specificSemanticMapTextField.setWidth("400px");
        specificSemanticMapTextField.setEnabled(false);

        instructionsLabel.setText("Introduceti import statement-urile pentru tabelele semantic_map, map_word si map_link (obligatoriu in aceasta ordine) obtinute din fisierele output ale comenzilor de forma:");
        commandLabel.setText("pg_dump --host {host} -p {port} -U {username} -n {schema} -t {table} --column-inserts {database} > {path}");
        specificSemanticMapLabel.setText("Pentru a importa o harta semantica specifica bifati casuta si introduceti denumirea acesteia");
    }

    @Override
    public SemanticMapImportPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    public TextField getSpecificSemanticMapTextField() {
        return specificSemanticMapTextField;
    }

    public TextArea getInsertStatementsTextArea() {
        return insertStatementsTextArea;
    }

    public Checkbox getSpecificSemanticMapCheckBox() {
        return specificSemanticMapCheckBox;
    }
}
