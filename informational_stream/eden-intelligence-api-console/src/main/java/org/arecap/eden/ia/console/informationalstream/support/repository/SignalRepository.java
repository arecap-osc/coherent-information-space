package org.arecap.eden.ia.console.informationalstream.support.repository;

import org.arecap.eden.ia.console.informationalstream.support.bean.Signal;
import org.arecap.eden.ia.console.service.ListRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public interface SignalRepository extends ListRepository<Signal, Long> {

    List<Signal> findAllByLocaleNot(Locale locale);

    List<Signal> findAllByLocale(Locale locale);

    List<Signal> findAllByFeaturePartContainingIgnoreCaseOrDetailsContainingIgnoreCase(String featurePart, String details);

}
