package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.ScreenProperties;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class NodeInformationPresenter extends DefaultFlowPresenter {
    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private ScreenProperties screenProperties;

    @Override
    public NodeInformationView getView() {
        return (NodeInformationView)super.getView();
    }

    public void setAbsolutePosition(Double x, Double y) {
        Double absoluteX = x;
        Double absoluteY = y;

        if (x < 120) {
            absoluteX += 15;
        }
        if (x > screenProperties.getWidth() - 150) {
            absoluteX -= 250;
        }
        if (y < 100) {
            absoluteY += 15;
        }

        absoluteX = absoluteX.equals(x) ? x - 120 : absoluteX;
        absoluteY = absoluteY.equals(y) ? y - 100 : absoluteY;

        getView().getStyle().set("left", absoluteX + "px");
        getView().getStyle().set("top", absoluteY + "px");
    }

    public void setNodeInformation(Double x, Double y, NodeBean nodeBean) {
        setAbsolutePosition(x, y);

        DataMap dataMap = coherentSpaceService.findNodeDataMap(nodeBean).get();
        ApplicationData applicationData = applicationDataService.getApplicationData(applicationDataIe.getApplicationId(), dataMap);
        StringBuilder stringBuilder = new StringBuilder();

        if (applicationData.getSemantic() != null && ("".equals(applicationData.getSemantic()) == false)) {
            stringBuilder.append(applicationData.getSemantic() + System.lineSeparator());
        }
        if (applicationData.getSemanticDetails() != null && ("".equals(applicationData.getSemanticDetails()) == false)) {
            stringBuilder.append(applicationData.getSemanticDetails() + System.lineSeparator());
        }

        String labelString = stringBuilder.toString();
        if ("".equals(labelString)) {
            getView().setVisible(false);
            return;
        }
        getView().setVisible(true);
        getView().getTextArea().setValue(labelString);
    }

    public void keepVisibleIfMouseIsOver(Double offsetX, Double offsetY) {
        if(getView().isVisible() == false) {
            return;
        }
        String leftPx = getView().getStyle().get("left");
        String topPx = getView().getStyle().get("top");
        if (leftPx == null || topPx == null) {
            return;
        }
        double x = Double.parseDouble(leftPx.substring(0, leftPx.length() - 2));
        double y = Double.parseDouble(topPx.substring(0, topPx.length() - 2));
        if (x - 10 < offsetX && offsetX < x + 260 && y - 16 < offsetY && offsetY < y + 96) {
            getView().setVisible(true);
        } else {
            getView().setVisible(false);
        }
    }
}
