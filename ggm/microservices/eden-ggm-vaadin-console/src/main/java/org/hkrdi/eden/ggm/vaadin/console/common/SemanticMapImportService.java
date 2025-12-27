package org.hkrdi.eden.ggm.vaadin.console.common;


import com.vaadin.flow.spring.annotation.SpringComponent;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapLinkRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapWordRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@SpringComponent
public class SemanticMapImportService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Map<Long, Long> semanticMapIdsCorrespondence = new HashMap<>();
    private Map<Long, Long> mapWordIdsCorrespondence = new HashMap<>();

    @Autowired
    private SemanticMapRepositoryService semanticMapService;

    @Autowired
    private MapWordRepositoryService mapWordService;

    @Autowired
    private MapLinkRepositoryService mapLinkService;

    public SemanticMapImportService() {
    }

    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'write')")
    @Transactional
    public boolean importSemanticMap(String insertStatements) {
        boolean result = true;
        try {
            String[] lines = insertStatements.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("semantic_map (") || line.contains("semantic_map(")) {
                    saveSemanticMapFromInsertStatement(line);
                }
                if (line.contains("map_word (") || line.contains("map_word(")) {
                    saveMapWordFromImportStatement(line);
                }
                if (line.contains("map_link (") || line.contains("map_link(")) {
                    saveMapLinkFromImportStatement(line);
                }
            }
        } catch (Exception e) {
            result = false;
            LOGGER.error(e.getMessage(), e);
        } finally {
            semanticMapIdsCorrespondence.clear();
            mapWordIdsCorrespondence.clear();
        }

        return result;
    }

    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'write')")
    @Transactional
    public boolean importSemanticMap(String insertStatements, String semanticMapLabel) {
        boolean result = true;
        try {
            String[] lines = insertStatements.split("\\r?\\n");
            for (String line : lines) {
                if ((line.contains("semantic_map (") || line.contains("semantic_map(")) &&
                        line.contains("\'" + semanticMapLabel + "\')")) {
                    saveSemanticMapFromInsertStatement(line);
                }
                if ((line.contains("map_word (") || line.contains("map_word(")) &&
                        semanticMapIdsCorrespondence.keySet().stream().anyMatch(semanticMapId -> line.contains(String.valueOf(semanticMapId)))) {
                    saveMapWordFromImportStatement(line);
                }
                if ((line.contains("map_link (") || line.contains("map_link(")) &&
                        mapWordIdsCorrespondence.keySet().stream().anyMatch(mapWordId -> line.contains(String.valueOf(mapWordId)))) {
                    saveMapLinkFromImportStatement(line);
                }
            }
        } catch (Exception e) {
            result = false;
            LOGGER.error(e.getMessage(), e);
        } finally {
            semanticMapIdsCorrespondence.clear();
            mapWordIdsCorrespondence.clear();
        }
        return result;
    }

    private void saveSemanticMapFromInsertStatement(String line) {
        String[] values = getValuesFromInsertStatement(line);
        SemanticMap semanticMap = new SemanticMap();
        semanticMap.setBrief(values[1].trim());
        semanticMap.setDescription(values[2].trim());
        semanticMap.setLabel(values[3].trim());

        SemanticMap persistedSemanticMap = semanticMapService.save(semanticMap);
        semanticMapIdsCorrespondence.put(new Long(values[0]), persistedSemanticMap.getId());
    }

    private void saveMapWordFromImportStatement(String line) {
        String[] values = getValuesFromInsertStatement(line);
        MapWord mapWord = new MapWord();
        mapWord.setDetails(values[1].trim());
        mapWord.setLetter(values[2].trim());
        mapWord.setWord(values[3].trim());
        mapWord.setX(Long.valueOf(values[4].trim()));
        mapWord.setY(Long.valueOf(values[5].trim()));

        SemanticMap semanticMap = semanticMapService.findById(semanticMapIdsCorrespondence.get(Long.valueOf(values[6].trim()))).get();
        mapWord.setSemanticMap(semanticMap);

        MapWord persistedMapWord = mapWordService.save(mapWord);
        mapWordIdsCorrespondence.put(new Long(values[0]), persistedMapWord.getId());
    }

    private void saveMapLinkFromImportStatement(String line) {
        String[] values = getValuesFromInsertStatement(line);
        MapLink mapLink = new MapLink();
        mapLink.setConjunction(values[1].trim());
        mapLink.setDetails(values[2].trim());
        mapLink.setPhrase(values[3].trim());
        mapLink.setVerb(values[4].trim());
        mapLink.setVerbalization(values[5].trim());

        MapWord fromMapWord = mapWordService.findById(mapWordIdsCorrespondence.get(Long.valueOf(values[6].trim()))).get();
        MapWord toMapWord = mapWordService.findById(mapWordIdsCorrespondence.get(Long.valueOf(values[7].trim()))).get();
        mapLink.setFromWord(fromMapWord);
        mapLink.setToWord(toMapWord);

        mapLinkService.save(mapLink);
    }

    private String[] getValuesFromInsertStatement(String line) {
        return line.substring(line.indexOf("VALUES ("), line.indexOf(");"))
                .replace("VALUES (", "")
                .replace("\'", "")
                .split(",");
    }
}