package org.arecap.eden.ia.console.boot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 * @author George Boboc
 * @since 1.0
 */
@Service
@Order(value=Ordered.HIGHEST_PRECEDENCE)
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Autowired
    private VaadinContextCheck vaadinContextCheck;

    @PostConstruct
    protected void init(){
        vaadinContextCheck.vaadinContextComponentsScan();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Map<String, Class<?>> getAnnotatedBeans(Class<? extends Annotation> annotationType) {
        String[] beanNames = BeanUtil.getBeanNamesForAnnotation(annotationType);
        Map<String, Class<?>> results = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            results.put(beanName, BeanUtil.getBeanType(beanName));
        }
        return results;
    }

    public static <T> T getBean(Class<T> beanClass) {
    	if (context == null) {
    		//on init phase
    		return null;
    	}
        return context.getBean(beanClass);
    }

    public static <T> T getBean(String beanName) {
        if (context == null) {
            //on init phase
            return null;
        }
        return (T) context.getBean(beanName);
    }

    public static Class<?> getBeanType(String beanName) {
        if (context == null) {
            //on init phase
            return null;
        }
        return context.getType(beanName);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> beanType) {
        if (context == null) {
            //on init phase
            return null;
        }
        return context.getBeansOfType(beanType);
    }

    public static BeanFactory getBeanFactory() {
        if (context == null) {
            return null;
        }
        return context.getAutowireCapableBeanFactory();
    }

    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotation) {
        if (context == null) {
            return new String[0];
        }
        return context.getBeanNamesForAnnotation(annotation);
    }


}