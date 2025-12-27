package org.hkrdi.eden.ggm.vaadin.console.common.i18n.flow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import com.vaadin.flow.component.combobox.ComboBox;
import org.eclipse.jgit.util.StringUtils;
import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.GeneratedVaadinTextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class InternationalizeViewEngine {


    public static void internationalize(Object object) {
        internationalize(object, UI.getCurrent().getLocale());
    }

    public static void internationalize(Object object, Locale locale) {
        if (!Component.class.isAssignableFrom(object.getClass())) {
            return;
        }
        translateSubComponents((Component) object, locale);
    }

    private static void translateSubComponents(Component component, Locale locale) {
        translateComponent(component, locale);
        component.getChildren().forEach(c -> translateSubComponents(c, locale));
//		Stream.of(FieldUtils.getAllFields(component.getClass())).forEach(field-> {
//			try {
//				field.setAccessible(true);
//				if (Component.class.isAssignableFrom(field.getType())){
//					Component c = (Component)field.get(component);
//					try {
//						translateComponent(c, locale);
////						translateSubComponents(c, locale);
//					}catch(Throwable t) {
//						t.printStackTrace();
//					}
//				}
//			}catch(Throwable t) {
//				t.printStackTrace();
//			}
//		});;
    }

    private static void translateComponent(Component component, Locale locale) {
        if (component == null) {
            return;
        }
        //translate tooltip if exists
        if (StringUtils.isEmptyOrNull(component.getElement().getAttribute("title")) == false) {
            component.getElement().setAttribute("title", storeMsgTitleKeyAndGetTranslation(component.getElement().getAttribute("title"), component, locale));
        }
        if (Label.class.isAssignableFrom(component.getClass())) {
            translateLabel((Label) component, locale);
            return;
        }
        if (Checkbox.class.isAssignableFrom(component.getClass())) {
            translateCheckbox((Checkbox) component, locale);
            return;
        }
        if (Button.class.isAssignableFrom(component.getClass())) {
            translateButton((Button) component, locale);
            return;
        }
        if (Anchor.class.isAssignableFrom(component.getClass())) {
            translateAnchor((Anchor) component, locale);
            return;
        }
        if (Image.class.isAssignableFrom(component.getClass())) {
            translateImage((Image) component, locale);
            return;
        }
        if (TextArea.class.isAssignableFrom(component.getClass())) {
            translateTextArea((TextArea) component, locale);
            return;
        }
        if (H4.class.isAssignableFrom(component.getClass())) {
            translateH4((H4) component, locale);
            return;
        }
        if (TextField.class.isAssignableFrom(component.getClass())) {
            translateTextField((TextField) component, locale);
            return;
        }
        if (ComboBox.class.isAssignableFrom(component.getClass())) {
            translateComboBox((ComboBox) component, locale);
        }
//		if (GeneratedVaadinTextField.class.isAssignableFrom(component.getClass())) {
//			translateGenericTextField((GeneratedVaadinTextField)component);
//			return;
//		}

        //try generic label
        if (component.getElement().getProperty("label") != null) {
            translateLabelProperty(component, component.getElement().getProperty("label"), locale);
            return;
        }
    }

    private static void translateTextArea(TextArea button, Locale locale) {
//		if (isMsgKey(textArea.getLabel())){
//			textArea.setLabel(GgmI18NProviderStatic.getTranslation(textArea.getLabel(), locale));
//		}

        button.setLabel(storeMsgKeyAndGetTranslation(button.getLabel(), button, locale));
    }

    private static void translateTextField(TextField button, Locale locale) {
//		if (isMsgKey(textArea.getLabel())){
//			textArea.setLabel(GgmI18NProviderStatic.getTranslation(textArea.getLabel(), locale));
//		}

        button.setLabel(storeMsgKeyAndGetTranslation(button.getLabel(), button, locale));
    }

    private static void translateComboBox(ComboBox comboBox, Locale locale) {
        comboBox.setLabel(storeMsgKeyAndGetTranslation(comboBox.getLabel(), comboBox, locale));
    }

    private static void callSetter(String methodName, Object target, String value) {
        Method m;
        try {
            m = target.getClass().getMethod(methodName, String.class);
            m.setAccessible(true);
            m.invoke(target, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static String callGetter(String methodName, Object target) {
        Method m;
        try {
            m = target.getClass().getMethod(methodName);
            m.setAccessible(true);
            return (String) m.invoke(target);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void translateLabelProperty(Component comp, String labelValue, Locale locale) {
        if (isMsgKey(labelValue)) {
            comp.getElement().setProperty("label", GgmI18NProviderStatic.getTranslation(labelValue, locale));
        }
    }

    private static void translateGenericTextField(GeneratedVaadinTextField textArea, Locale locale) {
        if (isMsgKey(callGetter("getLabelString", textArea))) {
            callSetter("setLabelString", textArea, GgmI18NProviderStatic.getTranslation(callGetter("getLabelString", textArea), locale));
        }
    }

    private static void translateLabel(Label label, Locale locale) {
//		if (isMsgKey(label.getText())){
//			label.setText(GgmI18NProviderStatic.getTranslation(label.getText(), locale));
//		}

        label.setText(storeMsgKeyAndGetTranslation(label.getText(), label, locale));
    }

    private static void translateCheckbox(Checkbox button, Locale locale) {
//		if (isMsgKey(button.getLabel())){
//			button.getElement().setAttribute("msg.key", button.getLabel());
//			button.setLabel(GgmI18NProviderStatic.getTranslation(button.getLabel(), locale));
//		}else if (button.getElement().getAttribute("msg.key") != null){
//			button.setLabel(GgmI18NProviderStatic.getTranslation(button.getElement().getAttribute("msg.key"), locale));
//		}
        button.setLabel(storeMsgKeyAndGetTranslation(button.getLabel(), button, locale));
    }

    private static String storeMsgKeyAndGetTranslation(String value, Component c, Locale locale) {
        if (isMsgKey(value)) {
            c.getElement().setAttribute("msg.key", value);
            return GgmI18NProviderStatic.getTranslation(value, locale);
        } else if (c.getElement().getAttribute("msg.key") != null) {
            return GgmI18NProviderStatic.getTranslation(c.getElement().getAttribute("msg.key"), locale);
        }
        return value;
    }

    private static String storeMsgTitleKeyAndGetTranslation(String value, Component c, Locale locale) {
        if (isMsgKey(value)) {
            c.getElement().setAttribute("msg.title.key", value);
            return GgmI18NProviderStatic.getTranslation(value, locale);
        } else if (c.getElement().getAttribute("msg.title.key") != null) {
            return GgmI18NProviderStatic.getTranslation(c.getElement().getAttribute("msg.title.key"), locale);
        }
        return value;
    }

    private static void translateButton(Button button, Locale locale) {
        button.setText(storeMsgKeyAndGetTranslation(button.getText(), button, locale));
    }

    private static void translateAnchor(Anchor anchor, Locale locale) {
        if (anchor.getElement().getAttribute("download") == null) {
            //apply it only for non download anchors
            anchor.setText(storeMsgKeyAndGetTranslation(anchor.getText(), anchor, locale));
        }
    }

    private static void translateH4(H4 anchor, Locale locale) {
        anchor.setText(storeMsgKeyAndGetTranslation(anchor.getText(), anchor, locale));
    }

    private static void translateImage(Image image, Locale locale) {
        image.setText(storeMsgKeyAndGetTranslation(image.getText(), image, locale));
        if (image.getElement().getAttribute("title") != null && !image.getElement().getAttribute("title").isEmpty()) {
            image.getElement().setAttribute("title", storeMsgKeyAndGetTranslation(image.getText(), image, locale));
        }
    }
    //logo.getElement().setAttribute("title", getRouteNameForBredCrumbAndTooltip())

    private static void translateGrid(Grid grid) {
        //column name can't be changed
    }

    private static boolean isMsgKey(String msg) {
        return msg != null && !msg.trim().isEmpty() && msg.contains(".")
                && msg.indexOf(".") != msg.length() - 1
                && !msg.contains(" ") && !msg.contains(";") && !msg.contains("!")
                && !msg.contains(",") && !msg.contains("=");
    }
}
