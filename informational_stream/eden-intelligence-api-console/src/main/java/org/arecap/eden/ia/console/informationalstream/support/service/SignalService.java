package org.arecap.eden.ia.console.informationalstream.support.service;

import org.arecap.eden.ia.console.informationalstream.support.bean.Signal;
import org.arecap.eden.ia.console.informationalstream.support.repository.SignalRepository;
import org.arecap.eden.ia.console.service.I18nRepositoryService;
import org.arecap.eden.ia.console.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SignalService
        implements RepositoryService<Signal, Long>, I18nRepositoryService<Signal, Long> {

    @Autowired
    private SignalRepository signalRepository;

    @Autowired
    private InformationalStreamService informationalStreamService;

    @Override
    public SignalRepository getRepository() {
        return signalRepository;
    }

    @Override
    public List<Signal> findAll() {
        return getRepository().findAll();
    }

    public List<Signal> findAllByLocaleNot(Locale locale) {
        return getRepository().findAllByLocaleNot(locale);
    }

    public List<Signal> findAllByLocale(Locale locale) {
        return getRepository().findAllByLocale(locale);
    }

    public List<Signal> findAllOfInformationalStream(Long informationalStreamId) {
        return findAll(informationalStreamId, null, "");
    }

    public List<Signal> findAllOfFeature(Long featureId) {
        return findAll(null, featureId, "");
    }

    public List<Signal> findAllOfSearch(String searchText) {
        return findAll(null, null, searchText);
    }

    public List<Signal> findAllOfInformationalStream(Long informationalStreamId, String searchText) {
        return findAll(informationalStreamId, null, searchText);
    }


    public List<Signal> findAllOfFeature(Long featureId, String searchText) {
        return findAll(null, featureId, searchText);
    }

    public List<Signal> findAll(Long informationalStreamId, Long featureId, String searchText) {
        return findAll(informationalStreamId, featureId, searchText, searchText);
    }

    public List<Signal> findAll(Long informationalStreamId, Long featureId, String featurePart, String details) {
        List<Signal> searchSignals = getRepository().findAllByFeaturePartContainingIgnoreCaseOrDetailsContainingIgnoreCase(featurePart, details);
        if(featureId != null) {
            return searchSignals.stream()
                    .filter(signal -> signal.getFeature() != null && signal.getFeature().getId().compareTo(featureId) == 0)
                    .collect(Collectors.toList());
        }
        if(informationalStreamId != null) {
            return searchSignals.stream()
                    .filter(signal -> signal.getInformationalStream() != null && signal.getInformationalStream().getId().compareTo(informationalStreamId) == 0)
                    .collect(Collectors.toList());
        }
        return searchSignals.stream()
                .filter(signal -> signal.getFeature() == null && signal.getInformationalStream() == null)
                .collect(Collectors.toList());
    }

    public Set<Signal> getCongruenceSignals(Signal currentSignal) {
        Set<Signal> signals = getCongruenceSignals(currentSignal, true, new HashSet<>());
        signals.remove(currentSignal);
        return signals;
    }

    private Set<Signal> getCongruenceSignals(Signal currentSignal, boolean recursiveCongruence, Set<Signal> result) {
        List<Signal> signals = findAllByLocale(currentSignal.getLocale());
        result.addAll(signals.stream()
                .filter(signal-> currentSignal.getId().equals(signal.getCongruenceId())).collect(Collectors.toSet()));
        result.addAll(signals.stream()
                .filter(signal-> signal.getId().equals(currentSignal.getCongruenceId())).collect(Collectors.toSet()));
        if(recursiveCongruence) {
            result.stream().collect(Collectors.toList()).stream()
                    .forEach(signal -> getCongruenceSignals(signal, false, result));
        }
        return result;

    }

    public Set<Signal> getSimilaritySignals(Signal currentSignal) {
        Set<Signal> signals = getSimilaritySignals(currentSignal, true, new HashSet<>());
        Set<Signal> similarityCongruent = new HashSet<>();
        signals.stream().forEach(similarity -> similarityCongruent.addAll(getCongruenceSignals(similarity).stream()
                .filter(signal -> signal.getSimilarityId() != null)
                .map(congruence -> findById(congruence.getSimilarityId()).get()).collect(Collectors.toSet())));
        signals.stream().forEach(similarity -> similarityCongruent.addAll(getCongruenceSignals(similarity)));
        signals.addAll(similarityCongruent);
        signals.remove(currentSignal);
        signals.removeAll(getCongruenceSignals(currentSignal));
        return signals;
    }

    public Set<Signal> getSimilaritySignals(Signal currentSignal, boolean recursiveSimilarity, Set<Signal> result) {
        List<Signal> signals = findAllByLocale(currentSignal.getLocale());
        result.addAll(signals.stream()
                .filter(signal-> currentSignal.getId().equals(signal.getSimilarityId())).collect(Collectors.toSet()));
        result.addAll(signals.stream()
                .filter(signal-> signal.getId().equals(currentSignal.getSimilarityId())).collect(Collectors.toSet()));
        if(recursiveSimilarity) {
            result.stream().collect(Collectors.toList()).stream()
                    .forEach(signal -> result.addAll(getSimilaritySignals(signal, false, result)));
        }
        return result;
    }

}
