package org.arecap.eden.ia.console.informationalstream.support.service;

import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.informationalstream.support.repository.InformationalStreamRepository;
import org.arecap.eden.ia.console.service.I18nRepositoryService;
import org.arecap.eden.ia.console.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Service
public class InformationalStreamService
        implements RepositoryService<InformationalStream, Long>, I18nRepositoryService<InformationalStream, Long> {

    @Autowired
    private InformationalStreamRepository informationalStreamRepository;

    @Override
    public InformationalStreamRepository getRepository() {
        return informationalStreamRepository;
    }

    @Override
    public List<InformationalStream> findAll() {
        return getRepository().findAll();
    }

    public List<InformationalStream> getAllByNameStartsWithIgnoreCase(String name) {
        return getRepository().findAllByNameContainingIgnoreCase(name);
    }

    public List<InformationalStream> findAllByLocaleNot(Locale locale) {
        return getRepository().findAllByLocaleNot(locale);
    }

}
