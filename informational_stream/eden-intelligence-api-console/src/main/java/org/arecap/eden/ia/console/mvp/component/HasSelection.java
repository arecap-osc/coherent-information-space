package org.arecap.eden.ia.console.mvp.component;

import java.util.Optional;

public interface HasSelection<T> {

    void publishSelection(Optional<T> selection);

}
