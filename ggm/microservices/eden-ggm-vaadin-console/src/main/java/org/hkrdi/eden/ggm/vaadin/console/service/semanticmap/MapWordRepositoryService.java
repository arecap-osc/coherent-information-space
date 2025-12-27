package org.hkrdi.eden.ggm.vaadin.console.service.semanticmap;

import org.hkrdi.eden.ggm.repository.semanticmap.MapWordRepository;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.PagingAndSortingRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MapWordRepositoryService implements PagingAndSortingRepositoryService<MapWord, Long> {

    @Autowired
    private MapWordRepository mapWordRepository;

    @Override
    public MapWordRepository getRepository() {
        return mapWordRepository;
    }

    public List<MapWord> findAllBySemanticMapId(Long mapId) {
        return mapWordRepository.findAllBySemanticMapId(mapId);
    }

    public Optional<MapWord> findByXAndY(Long x, Long y) {
        return mapWordRepository.findByXAndY(x, y);
    }

    public Optional<MapWord> findByXAndYAndSemanticMapId(Long x, Long y, Long semanticMapId) {
        return mapWordRepository.findByXAndYAndSemanticMapId(x, y, semanticMapId);
    }

    public MapWord save(MapWord mapWord) {
        return mapWordRepository.save(mapWord);
    }
}
