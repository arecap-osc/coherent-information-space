package org.arecap.eden.ia.console.informationalstream.support.repository;

import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;
import org.arecap.eden.ia.console.service.ListRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends ListRepository<Feature, Long> {

    List<Feature> findAllByInformationalStreamId(Long informationalStreamId);


    List<Feature> findAllByInformationalStreamIdAndContentContainingIgnoreCase(Long informationalStreamId, String content);

}
