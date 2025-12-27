package org.arecap.eden.ia.console.media.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import javax.annotation.PostConstruct;

@SpringComponent
@VaadinSessionScope
public class ScreenProperties extends MediaRendererTransform2D {

    @PostConstruct
    protected void initScale() {
        setScale(8d);
    }
}
