package org.arecap.eden.ia.console.service;


import org.arecap.eden.ia.console.informationalstream.api.factory.I18nPropertyFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface I18nRepositoryService<T extends I18nPropertyFactory<ID>, ID extends Serializable> extends RepositoryService<T, ID> {

    default Set<T> getI18nBeans(T currentI18nBean) {
        Set<T> i18nBeans = getI18nBeans(currentI18nBean, true, new HashSet<>());
        i18nBeans.remove(currentI18nBean);
        return i18nBeans;
    }

    default Set<T> getI18nBeans(T currentI18nBean, boolean recursiveSimilarity, Set<T> result) {
        List<T> i18nBeans = findAll();
        result.addAll(i18nBeans.stream()
                .filter(i18nBean-> currentI18nBean.getId().equals(i18nBean.getI18nId())).collect(Collectors.toSet()));
        result.addAll(i18nBeans.stream()
                .filter(i18nBean-> i18nBean.getId().equals(currentI18nBean.getI18nId())).collect(Collectors.toSet()));
        if(recursiveSimilarity) {
            result.stream().collect(Collectors.toList()).stream()
                    .forEach(i18nBean -> result.addAll(getI18nBeans(i18nBean, false, result)));
        }
        return result;
    }


}
