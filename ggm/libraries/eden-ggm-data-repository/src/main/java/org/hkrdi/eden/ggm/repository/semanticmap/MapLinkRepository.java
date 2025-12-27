package org.hkrdi.eden.ggm.repository.semanticmap;

import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapLinkRepository extends PagingAndSortingRepository<MapLink, Long> {
    Optional<MapLink> findByFromWordIdAndToWordId(Long fromWordId, Long toWordId);

    List<MapLink> findAllByFromWordSemanticMapId(Long semanticMapId);

    List<MapLink> findAllByFromWordIdOrToWordId(Long fromWordId, Long toWordId);

    List<MapLink> findAllByFromWordId(Long fromWordId);

    List<MapLink> findAllByToWordId(Long toWordId);
}
