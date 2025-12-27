package org.arecap.eden.ia.console.informationalstream.support.service;

import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;
import org.arecap.eden.ia.console.informationalstream.support.repository.FeatureRepository;
import org.arecap.eden.ia.console.service.I18nRepositoryService;
import org.arecap.eden.ia.console.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureService
        implements RepositoryService<Feature, Long>, I18nRepositoryService<Feature, Long> {

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public FeatureRepository getRepository() {
        return featureRepository;
    }

    @Override
    public List<Feature> findAll() {
        return getRepository().findAll();
    }

    public List<Feature> findByInformationalStreamId(Long informationalStreamId) {
        return getRepository().findAllByInformationalStreamId(informationalStreamId);
    }


    public List<Feature> findAll(Long informationalStreamId, String content) {
        return getRepository().findAllByInformationalStreamIdAndContentContainingIgnoreCase(informationalStreamId, content);
    }


}
