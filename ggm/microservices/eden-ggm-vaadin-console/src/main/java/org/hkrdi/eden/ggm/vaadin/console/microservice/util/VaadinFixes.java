package org.hkrdi.eden.ggm.vaadin.console.microservice.util;

import com.vaadin.flow.dom.Element;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.JsUtil;

public class VaadinFixes {

	public static void hideErrorMessages(Element element) {
		JsUtil.execute(
            "hideErrorMessages();\n" +

            "function toggle2(className, displayState){\n" +
            "\t    var elements = document.getElementsByClassName(className)\n" +
            "\t    for (var i = 0; i < elements.length; i++){\n" +
            "\t        elements[i].style.display = displayState;\n" +
            "\t    }\n" +
            "}\n" +

            "function hideErrorMessages() { \n" +
            "\t    toggle2('v-system-error', 'none');\n" +
            " \t   setTimeout(hideErrorMessages, 50);\n" +            
            "}\n", element);
	}

}
