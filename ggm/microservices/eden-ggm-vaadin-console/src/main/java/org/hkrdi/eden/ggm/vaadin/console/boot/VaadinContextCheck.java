package org.hkrdi.eden.ggm.vaadin.console.boot;


import com.vaadin.flow.component.Component;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.cop.support.BeanUtil;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Configuration
public class VaadinContextCheck {

    @PostConstruct
    protected void vaadinContextComponentsScan() {
        Map<Class<? extends Component>, Integer> componentsByType = new HashMap<>();
        Map<Class<? extends Component>, Class> componentToSuperclass = new HashMap<>();
        Arrays.stream(((ListableBeanFactory) BeanUtil.getBeanFactory()).getBeanNamesForType(Component.class)).
                map(beanName -> BeanUtil.getBeanFactory().getType(beanName))
                .forEach(componentType -> {
                    Class<? extends Component> flowComponentSuperclass = extractFlowComponentSuperclass((Class<? extends Component>) componentType);
                    componentToSuperclass.put(flowComponentSuperclass, componentType);
                    Integer cnt = Optional.ofNullable(componentsByType.get(flowComponentSuperclass)).orElse(0);
                    componentsByType.put(flowComponentSuperclass, ++cnt);
                });
        if(componentsByType.values().stream().filter(i -> i == 1).count() > 0) {
            Class<? extends Component> singleSuperClass = componentsByType.keySet()
                    .stream().filter(i -> componentsByType.get(i) == 1).findFirst().get();
            throw new BeanCreationException("A single type of [ " + singleSuperClass + " ] ."  +
                    " extended by [ " + componentToSuperclass.get(singleSuperClass) + " ] " +
                    " The SpringInstantiator will inject this in every PolymerTemplate by #getOrCreate ");
        }
    }

    public static Class<? extends Component> extractFlowComponentSuperclass(Class<? extends Component> componentType) {
        if(componentType.getPackage().getName().startsWith("com.vaadin.flow.component")){
            return componentType;
        }
        return extractFlowComponentSuperclass((Class<? extends Component>) componentType.getSuperclass());
    }




}
