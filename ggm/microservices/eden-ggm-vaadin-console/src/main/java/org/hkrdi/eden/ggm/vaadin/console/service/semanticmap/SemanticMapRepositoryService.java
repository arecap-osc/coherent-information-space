package org.hkrdi.eden.ggm.vaadin.console.service.semanticmap;

import org.hkrdi.eden.ggm.repository.semanticmap.SemanticMapRepository;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.PagingAndSortingRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SemanticMapRepositoryService implements PagingAndSortingRepositoryService<SemanticMap, Long> {

    @Autowired
    private SemanticMapRepository semanticMapRepository;

    @Override
    public SemanticMapRepository getRepository() {
        return semanticMapRepository;
    }

    public List<SemanticMap> findAllByLabelStartsWithIgnoreCase(String filterText) {
        return semanticMapRepository.findAllByLabelStartsWithIgnoreCase(filterText);
    }

    public List<SemanticMap> findAll() {
        return semanticMapRepository.findAll();
    }
}
