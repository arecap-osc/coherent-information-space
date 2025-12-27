package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class FromMapWordView extends AbstractMapWordView {

    @Autowired
    private FromMapWordPresenter fromMapWordPresenter;

    @Override
    public void buildView() {
        super.buildView();
        getCancel().addClickListener(getPresenter()::onCancelButtonClickEvent);
    }

    @Override
    public FromMapWordPresenter getPresenter() {
        return fromMapWordPresenter;
    }

}
