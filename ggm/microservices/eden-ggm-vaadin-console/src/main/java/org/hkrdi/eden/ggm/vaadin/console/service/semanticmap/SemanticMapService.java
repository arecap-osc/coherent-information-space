package org.hkrdi.eden.ggm.vaadin.console.service.semanticmap;

import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoordinatesUtil;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class SemanticMapService {

    public static String sustainableRimNetwork = "SUSTAINABLE_VERTICES::0";

    public static String metabolicRimNetwork = "METABOLIC_VERTICES::0";

    @Autowired
    private SemanticMapRepositoryService semanticMapRepositoryService;

    @Autowired
    private MapWordRepositoryService mapWordRepositoryService;

    @Autowired
    private MapLinkRepositoryService mapLinkRepositoryService;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    private Map<String, List<DataMap>> semanticMapNetworksFrames = new ConcurrentHashMap<>();

    private Map<String, Map<String, List<DataMap>>> semanticMapNetworks = new ConcurrentHashMap<>();

    private Map<String, Map<String, Set<WordBean>>> semanticMapNetworksWords = new ConcurrentHashMap<>();

    private Map<String, Map<String, Set<LinkBean>>> semanticMapNetworksLinks = new ConcurrentHashMap<>();

    private Map<String, Set<WordBean>> semanticMapDefaultNetworksWords = new ConcurrentHashMap<>();

    private Map<String, Set<LinkBean>> semanticMapDefaultNetworksLinks = new ConcurrentHashMap<>();

    private Map<WordBean, Set<LinkBean>> wordsLinks = new ConcurrentHashMap<>();

    public List<String> getAvailableNetworks() {
        return coherentSpaceService.getAvailableNetworks();
    }

    public List<MapWord> findMapWords(Long semanticMapId) {
        return mapWordRepositoryService.findAllBySemanticMapId(semanticMapId);
    }

    public MapWord findMapWord(Long semanticMapId, WordBean wordBean) {
        return getOrSaveMapWord(semanticMapId, wordBean.getX(), wordBean.getY());
    }

    public MapLink findMapLink(Long semanticMapId, LinkBean linkBean) {
        MapWord fromMapWord = findMapWord(semanticMapId, linkBean.getFromWord());
        MapWord toMapWord = findMapWord(semanticMapId, linkBean.getToWord());
        return mapLinkRepositoryService
                        .findByFromWordIdAndToWordId(fromMapWord.getId(), toMapWord.getId())
                .orElseGet(() -> mapLinkRepositoryService
                        .saveEmptyMapLinkBetweenMapWords(fromMapWord.getId(), toMapWord.getId()));
    }

    public Optional<Point> getRequestGraphicsCoordinates(WordBean word, MediaRendererTransform mrt) {
        Optional<Point> result = getAlgebraicCoordinates(word);
        if (!result.isPresent()) {
            return result;
        }
        return Optional.of(CoordinatesUtil.getAlgebraicToRequestGraphics(result.get(), mrt));
    }

    public Optional<Point> getRequestGraphicsCoordinates(WordBean wordBean, int startFrom, int totalParts, MediaRendererTransform mrt) {
        return Optional.of(coherentSpaceService.splitPoint(getRequestGraphicsCoordinates(wordBean, mrt).get(),
                new Point(mrt.getCenterX(), mrt.getCenterY()), startFrom, totalParts));
    }

    public Optional<WordBean> findNetworkNodeByScreenCoordinate(String network, Double x, Double y, MediaRendererTransform mrt) {
        Double nodeSelectionRangeDistance = coherentSpaceService.findNetworkNodeSelectionRangeDistance(network, mrt);
        Optional<WordBean> wordBean = findNetworkWordByScreenCoordinate(network, nodeSelectionRangeDistance, x, y, mrt);
        return wordBean;
    }

    public Set<WordBean> getDefaultSemanticMapWords(String network) {
        return Optional.ofNullable(semanticMapDefaultNetworksWords.get(network))
                .orElseGet(() -> putAndGetDefaultNetworkWords(network));
    }

    public Set<LinkBean> getDefaultSemanticMapLinks(String network) {
        return Optional.ofNullable(semanticMapDefaultNetworksLinks.get(network))
                .orElseGet(() -> putAndGetDefaultNetworkLinks(network));
    }

    public Set<WordBean> getSemanticMapWords(String frameNetwork, String network) {
        if(semanticMapNetworksWords.get(frameNetwork) == null) {
            semanticMapNetworksWords.put(frameNetwork, new ConcurrentHashMap<>());
        }
        return Optional.ofNullable(semanticMapNetworksWords.get(frameNetwork).get(network))
                .orElseGet(() -> putAndGetSemanticMapWord(frameNetwork, network));
    }

    public Set<LinkBean> getSemanticMapLinks(String frameNetwork, String network) {
        if(semanticMapNetworksLinks.get(frameNetwork) == null) {
            semanticMapNetworksLinks.put(frameNetwork, new ConcurrentHashMap<>());
        }
        return Optional.ofNullable(semanticMapNetworksLinks.get(frameNetwork).get(network))
                .orElseGet(() -> putAndGetSemanticMapLinks(frameNetwork, network));
    }

    public Set<LinkBean> getWordBeanLinks(WordBean wordBean) {
        return Optional.ofNullable(wordsLinks.get(wordBean))
                .orElseGet(() -> putAndGetWordBeanLinks(wordBean));
    }

    public MapWord getOrSaveMapWord(Long semanticMapId, Long x, Long y) {
        return mapWordRepositoryService.findByXAndYAndSemanticMapId(x, y, semanticMapId)
                .orElseGet(() -> saveNewMapWord(semanticMapId, x, y));
    }

    public List<MapLink>  getMapLinksForDataMap(Long semanticMapId, DataMap dataMap) {
        return dataMap == null ? new ArrayList<>() :
                mapLinkRepositoryService.findAllByFromWordSemanticMapId(semanticMapId).stream()
                        .filter(mapLink -> GeometryUtil.isOnSegment(bindFromWordCoordinate(mapLink),
                                bindToWordCoordinate(mapLink), dataMap.getAtAddressCoordinates())
                                && GeometryUtil.isOnSegment(bindFromWordCoordinate(mapLink),
                                bindToWordCoordinate(mapLink), dataMap.getToAddressCoordinates())
                        )
                        .filter(mapLink -> {
                            Point head = bindFromWordCoordinate(mapLink);
                            Point at = dataMap.getAtAddressCoordinates();
                            Point to = dataMap.getToAddressCoordinates();
                            return GeometryUtil.getDistance(at, head).compareTo(GeometryUtil.getDistance(to, head)) < 0;
                        })
//                        .sorted((c1, c2) -> {
//                            Point head = bindFromWordCoordinate(c1);
//                            Point tail = bindFromWordCoordinate(c2);
//                            Point at = dataMap.getAtAddressCoordinates();
//                            return GeometryUtil.getDistance(head, at).compareTo(GeometryUtil.getDistance(tail, at));
//                                        })
                .collect(Collectors.toList());
    }


    private Point bindFromWordCoordinate(MapLink mapLink) {
        return new Point(DataMap.toDoubleAddress(mapLink.getFromWord().getX()), DataMap.toDoubleAddress(mapLink.getFromWord().getY()));
    }

    private Point bindToWordCoordinate(MapLink mapLink) {
        return new Point(DataMap.toDoubleAddress(mapLink.getToWord().getX()), DataMap.toDoubleAddress(mapLink.getToWord().getY()));
    }

    private MapWord saveNewMapWord(Long semanticMapId, Long x, Long y) {
        MapWord mapWord = new MapWord();
        mapWord.setSemanticMap(semanticMapRepositoryService.findById(semanticMapId).get());
        mapWord.setLetter("-");
        mapWord.setWord("");
        mapWord.setDetails("");
        mapWord.setX(x);
        mapWord.setY(y);
        return mapWordRepositoryService.save(mapWord);
    }


    private Optional<WordBean> findNetworkWordByScreenCoordinate(String network, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        return getDefaultSemanticMapWords(network).stream().filter(wordBean -> isWordBeanTouched(wordBean, radius, x, y, mrt))
                .findAny();
    }

    private boolean isWordBeanTouched(WordBean wordBean, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        Optional<Point> wordBeanCoordiantes = getRequestGraphicsCoordinates(wordBean, mrt);
        return getRequestGraphicsCoordinates(wordBean, mrt).isPresent() &&
                GeometryUtil.getDistance(wordBeanCoordiantes.get(), new Point(x, y)) < radius;
    }

    private Set<LinkBean> putAndGetWordBeanLinks(WordBean wordBean) {
        Set<LinkBean> wordLinks = new HashSet<>();
        wordLinks.addAll(getDefaultSemanticMapWords(wordBean.getNetwork()).stream()
                .filter(tail -> tail.equals(wordBean) == false)
                .filter(tail -> isLink(wordBean, tail))
                .map(tail -> bindLinkBean(wordBean, tail))
                .collect(Collectors.toSet()));
        wordsLinks.put(wordBean, wordLinks);
        return wordLinks;
    }


    private boolean isLink(WordBean fromWord, WordBean toWord) {
        Point tail = getAlgebraicCoordinates(fromWord).orElse(new Point(0, 0));
        Point head = getAlgebraicCoordinates(toWord).orElse(new Point(0, 0));
        Double angle = Math.abs(GeometryUtil
                .round(Math
                        .atan2(tail.getX()-head.getX(),
                                tail.getY()-head.getY())
                        * 180 / Math.PI, 2));
        return angle.compareTo(0d) == 0
                || angle.compareTo(30d) == 0
                || angle.compareTo(60d) == 0
                || angle.compareTo(90d) == 0
                || angle.compareTo(120d) == 0
                || angle.compareTo(150d) == 0
                || angle.compareTo(180d) == 0;
    }

    private Set<WordBean> putAndGetDefaultNetworkWords(String network) {
        Set<WordBean> semanticMapNetworkWords = new HashSet<>();
        semanticMapNetworkWords.addAll(getDefaultSustainableNetworkWords(network));
        semanticMapNetworkWords.addAll(getDefaultMetabolicNetworkWords(network));
        semanticMapDefaultNetworksWords.put(network, semanticMapNetworkWords);
        return semanticMapNetworkWords;
    }

    private Set<LinkBean> putAndGetDefaultNetworkLinks(String network) {
        Set<LinkBean> semanticMapNetworkLinks = new HashSet<>();
        semanticMapNetworkLinks.addAll(getSemanticMapLinks(sustainableRimNetwork, network));
        semanticMapNetworkLinks.addAll(getSemanticMapLinks(metabolicRimNetwork, network));
        return semanticMapNetworkLinks;
    }

    private Set<WordBean> getDefaultSustainableNetworkWords(String network) {
        Set<WordBean> sustainableNetworkWords = new HashSet<>();
        List<DataMap> sustainableRimDataMaps = getSemanticMapFrameDataMaps(sustainableRimNetwork);
        List<WordBean> semanticMapSustainableWords = getSemanticMapDataMaps(sustainableRimNetwork, network).stream()
                .filter(DataMapFilterUtil.byNotInside(new ClusterBean(metabolicRimNetwork, 0L)))
                .map(this::bindAtWord)
                .collect(Collectors.toList());
        List<WordBean> metabolicProjectionWords = getSemanticMapDataMaps(metabolicRimNetwork, network).stream()
                .filter(DataMapFilterUtil.byInside(new ClusterBean(sustainableRimNetwork, 0L)))
                .map(this::bindAtWord)
                .collect(Collectors.toList());
        sustainableRimDataMaps.stream()
                .filter(DataMapFilterUtil.byAlternateTrivalentRoutes())
                .forEach(dataMap -> {
                    Point at = getAlgebraicCoordinates(bindAtWord(dataMap)).orElse(new Point(0,0));
                    Point to = getAlgebraicCoordinates(bindToWord(dataMap)).orElse(new Point(0, 0));
                    semanticMapSustainableWords.addAll(metabolicProjectionWords.stream()
                            .map(this::getAlgebraicCoordinates).map(o -> o.orElse(new Point(0, 0)))
                            .map(p -> GeometryUtil.getPerpendicularPoint(at, to, p))
                            .filter(p -> GeometryUtil.isOnSegment(at, to, p))
                            .map(p -> new WordBean(network, DataMap.toLongDoubleRoundAddress(p.getX()),
                                    DataMap.toLongDoubleRoundAddress(p.getY())))
                            .collect(Collectors.toList()));
                });
        sustainableNetworkWords.addAll(semanticMapSustainableWords);
        return semanticMapSustainableWords.stream().collect(Collectors.toSet());
    }

    private Set<WordBean> getDefaultMetabolicNetworkWords(String network) {
        List<DataMap> sustainableRimDataMaps = getSemanticMapFrameDataMaps(sustainableRimNetwork);
        return getSemanticMapDataMaps(metabolicRimNetwork, network).stream()
                .filter(DataMapFilterUtil.byNotInSemanticMap(sustainableRimDataMaps))
                .filter(DataMapFilterUtil.byNotInside(new ClusterBean(sustainableRimNetwork, 0L)))
                .map(this::bindAtWord)
                .collect(Collectors.toSet());
    }

    private Optional<Point> getAlgebraicCoordinates(WordBean wordBean) {
        return Optional.ofNullable(new Point(DataMap.toDoubleAddress(wordBean.getX()),
                DataMap.toDoubleAddress(wordBean.getY())));
    }

    private Set<LinkBean> putAndGetSemanticMapLinks(String frameNetwork, String network) {
        Set<LinkBean> semanticMapNetworkLinks = new HashSet<>();
        getSemanticMapWords(frameNetwork, network).stream()
                .forEach(wb -> {
                    semanticMapNetworkLinks.addAll(getWordBeanLinks(wb));
                });
        semanticMapNetworksLinks.get(frameNetwork).put(network, semanticMapNetworkLinks);
        return semanticMapNetworkLinks;
    }

    private Set<WordBean> putAndGetSemanticMapWord(String frameNetwork, String network) {
        Set<WordBean> semanticMapNetworkWords = getSemanticMapDataMaps(frameNetwork, network).stream()
                .map(this::bindAtWord).collect(Collectors.toSet());
        semanticMapNetworksWords.get(frameNetwork).put(network, semanticMapNetworkWords);
        return semanticMapNetworkWords;
    }

    private WordBean bindAtWord(DataMap dataMap) {
        return new WordBean(dataMap.getNetwork(), dataMap.getAtX(), dataMap.getAtY());
    }

    private WordBean bindToWord(DataMap dataMap) {
        return new WordBean(dataMap.getNetwork(), dataMap.getToX(), dataMap.getToY());
    }

    private LinkBean bindLinkBean(WordBean fromWord, WordBean toWord) {
        return new LinkBean(fromWord, toWord);
    }

    private List<DataMap> getSemanticMapFrameDataMaps(String network) {
        return Optional.ofNullable(semanticMapNetworksFrames.get(network))
                .orElseGet(() -> putAndGetNetworkFrameDataMaps(network));
    }

    private List<DataMap> putAndGetNetworkFrameDataMaps(String network) {
        List<DataMap> semanticMapNetworkFrame = coherentSpaceService.getNetworkDataMaps(network).stream()
                .filter(DataMapFilterUtil.byClusterIndex(0L))
                .filter(DataMapFilterUtil.byOuterRoutes())
                .collect(Collectors.toList());
        semanticMapNetworksFrames.put(network, semanticMapNetworkFrame);
        return semanticMapNetworkFrame;
    }

    private List<DataMap> getSemanticMapDataMaps(String frameNetwork, String network) {
        if(semanticMapNetworks.get(frameNetwork) == null) {
            semanticMapNetworks.put(frameNetwork, new ConcurrentHashMap<>());
        }
        return Optional.ofNullable(semanticMapNetworks.get(frameNetwork).get(network))
                .orElseGet(() -> putAndGetNetworkDataMaps(frameNetwork, network));
    }

    private List<DataMap> putAndGetNetworkDataMaps(String frameNetwork, String network) {
        List<DataMap> semanticMapNetwork = coherentSpaceService.getNetworkDataMaps(network).stream()
                .filter(DataMapFilterUtil.byInGrid(getSemanticMapFrameDataMaps(frameNetwork)))
                .collect(Collectors.toList());
        semanticMapNetworks.get(frameNetwork).put(network, semanticMapNetwork);
        return semanticMapNetwork;
    }

}
