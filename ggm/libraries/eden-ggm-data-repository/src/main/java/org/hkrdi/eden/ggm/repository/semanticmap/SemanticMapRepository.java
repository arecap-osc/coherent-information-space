package org.hkrdi.eden.ggm.repository.semanticmap;

import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemanticMapRepository extends PagingAndSortingRepository<SemanticMap, Long> {
    List<SemanticMap> findAllByLabelStartsWithIgnoreCase(String filterText);

    List<SemanticMap> findAll();
}
