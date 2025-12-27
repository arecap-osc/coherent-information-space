package org.arecap.eden.ia.console.microservice.layout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;

import java.io.Serializable;

public final class JsUtil {

    public static void execute(String js, Serializable... parameters) {
        UI.getCurrent().getPage().executeJavaScript(js, parameters);
    }

    public static String serialize(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) { //TODO
            e.printStackTrace();
        }
        return "{}";
    }

}
