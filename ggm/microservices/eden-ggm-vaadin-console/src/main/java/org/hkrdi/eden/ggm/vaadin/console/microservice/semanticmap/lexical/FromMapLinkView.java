package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class FromMapLinkView extends AbstractMapLinkView {

    @Autowired
    private FromMapLinkPresenter presenter;

    @Override
    public void prepareView() {
        setVisible(getPresenter().getInitialEntityId() != null);
        if(isVisible()) {
            getLinkTitle().setText(getPresenter().getEntity().getFromWord().getLetter() + "(\t" +
                    getPresenter().getEntity().getFromWord().getWord() + "\t)" +
                    " -> " + getPresenter().getEntity().getToWord().getLetter() + "(\t" +
                    getPresenter().getEntity().getToWord().getWord() + "\t)");
        }
    }

    @Override
    public FromMapLinkPresenter getPresenter() {
        return presenter;
    }

}
