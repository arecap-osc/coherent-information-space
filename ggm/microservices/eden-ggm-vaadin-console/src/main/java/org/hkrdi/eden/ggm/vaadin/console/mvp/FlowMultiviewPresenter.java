package org.hkrdi.eden.ggm.vaadin.console.mvp;

import java.util.EventObject;
import java.util.List;

public interface FlowMultiviewPresenter<T, ID> extends FlowPresenter<T, ID> {


    List<FlowView> getViews();

    @Override
    default void prepareModelAndView(EventObject event) {
        prepareModel(event);

        getViews().stream().forEach(view -> beforePrepareView(view, event));
        getViews().stream().forEach(this::prepareView);
        afterPrepareModel(event);
        getViews().stream().forEach(this::afterPrepareView);
        getView().internationalize();
    }

    default void afterPrepareView(FlowView flowView) {
        flowView.afterPrepareView();
    }

    default void beforePrepareView(FlowView flowView, EventObject event) {
        flowView.beforePrepareView(event);
    }

    default void prepareView(FlowView flowView) {
        flowView.prepareView();
    }

    default void setView(FlowView view) {
        if(getViews().contains(view) == false) {
            getViews().add(view);
        }
    }

    default FlowView getView() {
        return getViews().stream().findFirst().orElse(null);
    }

    default <T extends FlowView> FlowView getView(Class<T> flowViewType) {
        return getViews().stream().filter(e -> e.getClass().isAssignableFrom(flowViewType)).findFirst().orElse(null);
    }
}
