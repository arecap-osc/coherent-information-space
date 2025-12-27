package org.arecap.eden.ia.console.informationalstream.support.service;

import org.arecap.eden.ia.console.informationalstream.support.bean.FeatureStream;
import org.arecap.eden.ia.console.informationalstream.support.repository.FeatureStreamRepository;
import org.arecap.eden.ia.console.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeatureStreamService implements RepositoryService<FeatureStream, Long> {

    @Autowired
    private FeatureStreamRepository featureStreamRepository;

    @Override
    public FeatureStreamRepository getRepository() {
        return featureStreamRepository;
    }

    @Override
    public List<FeatureStream> findAll() {
        return getRepository().findAll();
    }

    public Optional<FeatureStream> findByDownstreamIdAndUpstreamId(Long downstreamId, Long upstreamId) {
        return getRepository().findByDownstreamIdAndUpstreamId(downstreamId, upstreamId);
    }

    public List<FeatureStream> findAllByFeatureId(Long featureId) {
        return getRepository().findAllByDownstreamFeatureIdAndUpstreamFeatureId(featureId, featureId);
    }

    public List<FeatureStream> findAllByInformationalStreamId(Long informationalStreamId) {
        return getRepository().findAll().stream()
                .filter(fs -> (fs.getUpstream().getInformationalStream() != null
                        && fs.getUpstream().getInformationalStream().getId().compareTo(informationalStreamId) == 0)
                        || (fs.getUpstream().getFeature() != null
                                && fs.getUpstream().getFeature().getInformationalStream().getId().compareTo(informationalStreamId) == 0)
                        ||(fs.getDownstream().getInformationalStream() != null
                        && fs.getDownstream().getInformationalStream().getId().compareTo(informationalStreamId) == 0)
                        || (fs.getDownstream().getFeature() != null
                        && fs.getDownstream().getFeature().getInformationalStream().getId().compareTo(informationalStreamId) == 0) )
                .collect(Collectors.toList());
    }

    public List<FeatureStream> findAll(Long informationalStreamId, Long featureId) {
        return informationalStreamId == null && featureId == null ? findAll() :
                (informationalStreamId == null ? findAllByFeatureId(featureId) : findAllByInformationalStreamId(informationalStreamId));
    }

}
