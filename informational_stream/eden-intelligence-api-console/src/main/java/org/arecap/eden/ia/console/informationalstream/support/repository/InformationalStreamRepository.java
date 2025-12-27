package org.arecap.eden.ia.console.informationalstream.support.repository;


import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.service.ListRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public interface InformationalStreamRepository extends ListRepository<InformationalStream, Long> {

    List<InformationalStream> findAllByNameContainingIgnoreCase(String name);

    List<InformationalStream> findAllByLocaleNot(Locale locale);

}
