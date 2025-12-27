package org.arecap.eden.ia.console.informationalstream.support.repository;

import org.arecap.eden.ia.console.informationalstream.support.bean.FeatureStream;
import org.arecap.eden.ia.console.service.ListRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureStreamRepository extends ListRepository<FeatureStream, Long> {

    Optional<FeatureStream> findByDownstreamIdAndUpstreamId(Long downstreamId, Long upstreamId);

    List<FeatureStream> findAllByDownstreamFeatureIdAndUpstreamFeatureId(Long downstreamFeatureId, Long upstreamFeatureId);
}
