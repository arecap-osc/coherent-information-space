package org.hkrdi.eden.ggm.repository.semanticmap;

import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapWordRepository extends PagingAndSortingRepository<MapWord, Long> {
    List<MapWord> findAllBySemanticMapId(Long mapId);

    Optional<MapWord> findByXAndY(Long x, Long y);

    Optional<MapWord> findByXAndYAndSemanticMapId(Long x, Long y, Long semanticMapId);
}
