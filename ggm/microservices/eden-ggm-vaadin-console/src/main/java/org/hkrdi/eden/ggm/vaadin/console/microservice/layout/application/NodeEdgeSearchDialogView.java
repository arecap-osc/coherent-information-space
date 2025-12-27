package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@SpringComponent
@UIScope
public class NodeEdgeSearchDialogView extends Dialog implements FlowView {

    @Autowired
    private NodeEdgeSearchDialogPresenter presenter;

    private TextField indexTextField = new TextField("msg.search.dialog.nodeindex.label");
    private TextField semanticSyntaxTextField = new TextField("msg.search.dialog.syntaxsemantic.label");
    private Button search = new Button("button.search.label", VaadinIcon.SEARCH.create());

    @Override
    public void buildView() {
        indexTextField.addValueChangeListener(getPresenter()::validateIndexInput);
        search.addClickListener(getPresenter()::searchNode);
        add(new VerticalLayout(indexTextField, semanticSyntaxTextField, search));
    }

    @Override
    public NodeEdgeSearchDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public void close() {
        super.close();
        indexTextField.clear();
        semanticSyntaxTextField.clear();
    }

    public TextField getIndexTextField() {
        return indexTextField;
    }

    public TextField getSemanticSyntaxTextField() {
        return semanticSyntaxTextField;
    }

    public void setIndexInvalid(String errorMessage) {
        indexTextField.setInvalid(true);
        indexTextField.setErrorMessage(errorMessage);
    }

    public void validateIndexInput(Set<NodeBean> allNodes) {
        String value = indexTextField.getValue();
        if ("".equals(value)) {
            indexTextField.setInvalid(false);
            return;
        }
        if (StringUtil.isNumeric(value) == false) {
            indexTextField.setInvalid(true);
            indexTextField.setErrorMessage("Valoarea introdusa nu este un numar");
            return;
        }
        if (allNodes.size() > 0 && allNodes.contains(new NodeBean(allNodes.stream().findFirst().get().getNetwork(), new Long(value)))) {
            indexTextField.setInvalid(false);
            return;
        }

        indexTextField.setInvalid(true);
        indexTextField.setErrorMessage("Nodul nu este in retea");
    }

    public String getIndexValue() {
        return indexTextField.getValue();
    }
}
