package org.hkrdi.eden.ggm.vaadin.console.service.semanticmap;

import org.hkrdi.eden.ggm.repository.semanticmap.MapLinkRepository;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.PagingAndSortingRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MapLinkRepositoryService implements PagingAndSortingRepositoryService<MapLink, Long> {

    @Autowired
    private MapLinkRepository mapLinkRepository;

    @Autowired
    @Qualifier("mapWordRepositoryService")
    private MapWordRepositoryService mapWordService;

    @Override
    public MapLinkRepository getRepository() {
        return mapLinkRepository;
    }

    public Optional<MapLink> findByFromWordIdAndToWordId(Long fromWordId, Long toWordId) {
        return mapLinkRepository.findByFromWordIdAndToWordId(fromWordId, toWordId);
    }

    public List<MapLink> findAllByFromWordSemanticMapId(Long semanticMapId) {
        return mapLinkRepository.findAllByFromWordSemanticMapId(semanticMapId);
    }

    public List<MapLink> findAllByFromWordIdOrToWordId(Long fromWordId, Long toWordId) {
        return mapLinkRepository.findAllByFromWordIdOrToWordId(fromWordId, toWordId);
    }

    public List<MapLink> findAllByFromWord(Long fromWordId) {
        return mapLinkRepository.findAllByFromWordId(fromWordId);
    }

    public List<MapLink> findAllByToWordId(Long toWordId) {
        return mapLinkRepository.findAllByToWordId(toWordId);
    }

    public MapLink saveEmptyMapLinkBetweenMapWords(Long fromMapWordId, Long toMapWordId) {
        Optional<MapWord> fromMapWord = mapWordService.findById(fromMapWordId);
        if (fromMapWord.isPresent() == false) {
            //TODO: log this
            return null;
        }
        Optional<MapWord> toMapWord = mapWordService.findById(toMapWordId);
        if (fromMapWord.isPresent() == false) {
            //TODO: log this
            return null;
        }

        return mapLinkRepository.save(new MapLink(fromMapWord.get(), toMapWord.get(), "", "", "", "", ""));
    }
}
