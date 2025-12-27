package org.hkrdi.eden.ggm.vaadin.console.microservice.common;

import com.vaadin.flow.component.notification.Notification;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;

public final class ApplicationDataWellsAndRoutesNotifier {


    public static void showFor(ApplicationData entity) {
        if (entity.getWells() != null || entity.getRoutes() != null ) {
            String result = "Informatia a fost propagata " + (entity.getWells() != null ? "in "+ entity.getWells().size()+ " de noduri." :
                    "pe "+entity.getRoutes().size() + " route ");
            Notification.show(result, 5000, Notification.Position.TOP_END);
        }
    }

}
