package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.lexical;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ToMapLinkView extends AbstractMapLinkView {

    @Autowired
    private ToMapLinkPresenter presenter;

    @Override
    public void prepareView() {
        setVisible(getPresenter().getInitialEntityId() != null);
        if (isVisible()) {
            getLinkTitle().setText(getPresenter().getEntity().getFromWord().getLetter() + "(\t" +
                    getPresenter().getEntity().getFromWord().getWord() + "\t)" +
                    " -> " + getPresenter().getEntity().getToWord().getLetter() + "(\t" +
                    getPresenter().getEntity().getToWord().getWord() + "\t)");
        }

    }

    @Override
    public ToMapLinkPresenter getPresenter() {
        return presenter;
    }

}
