package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import java.util.EventObject;
import java.util.Optional;

import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;

public abstract class ConfigurableMediaLayerStylePresenter extends DefaultFlowPresenter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Optional<MediaLayer> selected = Optional.empty();

    @Override
    public ConfigurableMediaLayerStyleView getView() {
        return (ConfigurableMediaLayerStyleView) super.getView();
    }

    @Override
    public void prepareModel(EventObject event) {
        Optional<MediaLayer> selectedItem = selected;
        getView().getMediaLayerGrid().setItems(getMediaLayerFactory().getMediaLayers());
        if (selectedItem.isPresent()) {
            getView().getMediaLayerGrid().getSelectionModel().select(selectedItem.get());
        }
    }

    protected abstract MediaLayerFactory getMediaLayerFactory();

    protected abstract void notifyMultiImageView();

    public void handleMediaLayerSelectedEvent(SelectionEvent<Grid<MediaLayer>, MediaLayer> gridMediaLayerSelectionEvent) {
        if (selected.isPresent() == false && gridMediaLayerSelectionEvent.getFirstSelectedItem().isPresent()) {
            getView().getMediaLayerGrid().getDataProvider().refreshAll();
        }
        selected = gridMediaLayerSelectionEvent.getFirstSelectedItem();
        if (selected.isPresent() == false) {
            LOGGER.info("User deselected media layer");
            getView().getConfigurableMediaRendererStyleLayerView().setEnabled(false);
            getView().getMediaLayerGrid().getDataProvider().refreshAll();
            return;
        }
        LOGGER.info("User selected media layer " + selected.get().getName());
        getView().getConfigurableMediaRendererStyleLayerView().setEnabled(true);
        getView().getConfigurableMediaRendererStyleLayerView().getPresenter().setMediaLayer(selected.get());
        getView().getConfigurableMediaRendererStyleLayerView().getPresenter().activateValueChangeListeners();
        getView().getMediaLayerGrid().getDataProvider().refreshAll();
    }

    public Button getGridColumnVisibleBtnLayout(MediaLayer mediaLayer) {
        Button visibleBtn = new Button(mediaLayer.isVisible() ? VaadinIcon.EYE.create() : VaadinIcon.EYE_SLASH.create());
        if (!mediaLayer.isVisible()) {
            visibleBtn.getStyle().set("color", "GRAY");
        }
        visibleBtn.setId(mediaLayer.getName());
        visibleBtn.addClickListener(this::handleGridVisibleBtn);
        return visibleBtn;
    }

    public Icon getGridColumnColorIconLayout(MediaLayer mediaLayer) {
        Icon colorIcon = VaadinIcon.CIRCLE.create();
        colorIcon.getStyle().set("margin-right", "7px");
        ConfigurableGraphics2dStyle configuration = ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle());
        colorIcon.setColor(configuration.getAlphaColorAsStr());
        return colorIcon;
    }

    public HorizontalLayout getGridColumnTextLayout(MediaLayer mediaLayer) {
        HorizontalLayout wrapper = new HorizontalLayout();
        Text text = new Text(mediaLayer.getName());
        wrapper.add(text);
        if (!mediaLayer.isVisible()) {
        	wrapper.getStyle().set("color", "GRAY");
        }
        return wrapper;
    }

    public Button getGridColumnUpBtnLayout(MediaLayer mediaLayer) {
        Button upBtn = new Button(VaadinIcon.ARROW_UP.create());
        upBtn.addClickListener(this::handleGridUpBtn);
        upBtn.setVisible(selected.isPresent() && selected.get().getName().equalsIgnoreCase(mediaLayer.getName()));
        return upBtn;
    }

    public Button getGridColumnDownBtnLayout(MediaLayer mediaLayer) {
        Button downBtn = new Button(VaadinIcon.ARROW_DOWN.create());
        downBtn.addClickListener(this::handleGridDownBtn);
        downBtn.setVisible(selected.isPresent() && selected.get().getName().equalsIgnoreCase(mediaLayer.getName()));
        return downBtn;
    }

    private void handleGridVisibleBtn(ClickEvent<Button> buttonClickEvent) {
        Button gridVisibleBtn = buttonClickEvent.getSource();
        Optional<MediaLayer> mediaLayer = getMediaLayerFactory().getMediaLayerByName(gridVisibleBtn.getId().get());
        if (mediaLayer.isPresent()) {
            LOGGER.info("User made media layer " + mediaLayer.get().getName() + (mediaLayer.get().isVisible() ? " invisible" : " visible"));
            mediaLayer.get().setVisible(!mediaLayer.get().isVisible());
            if (mediaLayer.get().isVisible()) {
            	selected = mediaLayer;
            }
            prepareModel(null);
            notifyMultiImageView();
        } else {
            //TODO log this is an error
        }
    }

    private void handleGridUpBtn(ClickEvent<Button> buttonClickEvent) {
        if (selected.isPresent()) {
            int index = getSelectedMediaLayerIndex();
            if (index > 0) {
                LOGGER.info("User moved up media layer " + selected.get().getName() + " to position " + (index - 1));
                applyGridMediaLayerOrder(index, index - 1);
            }
        }
    }

    private void handleGridDownBtn(ClickEvent<Button> buttonClickEvent) {
        if (selected.isPresent()) {
            int index = getSelectedMediaLayerIndex();
            if (index < getMediaLayerFactory().getMediaLayers().size() - 1) {
                LOGGER.info("User moved down media layer " + selected.get().getName() + " to position " + (index + 1));
                applyGridMediaLayerOrder(index, index + 1);
            }
        }
    }

    private void applyGridMediaLayerOrder(int removeIndex, int addIndex) {
        getMediaLayerFactory().getMediaLayers().remove(removeIndex);
        getMediaLayerFactory().getMediaLayers().add(addIndex, selected.get());
        prepareModel(null);
        notifyMultiImageView();
    }

    private int getSelectedMediaLayerIndex() {
        return getMediaLayerFactory().getMediaLayers().indexOf(selected.get());
    }

}
