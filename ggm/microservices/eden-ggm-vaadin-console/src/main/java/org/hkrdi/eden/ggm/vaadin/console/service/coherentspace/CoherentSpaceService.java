package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

import org.hkrdi.eden.ggm.algebraic.*;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.NetworkBuilder;
import org.hkrdi.eden.ggm.algebraic.netting.factory.NetworkFactory;
import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.hkrdi.eden.ggm.repository.DataMapRepository;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoordinatesUtil;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CoherentSpaceService {

    @Autowired
    private DataMapRepository dataMapRepository;

    @Autowired
    private ApplicationDataRepositoryService applicationDataRepositoryService;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Value("${hexavalent.logic.deep.max}")
    private int deepMax;

    private Map<String, List<DataMap>> networksDataMaps = new ConcurrentHashMap<>();

    private Map<String, DataMap> networkMinRoute = new ConcurrentHashMap<>();

    private Map<String, Map<Long, List<DataMap>>> networkNodesAtIndex = new ConcurrentHashMap<>();

    private Map<String, Map<Long, List<DataMap>>> networkNodesToIndex = new ConcurrentHashMap<>();

    private Map<String, List<HexavalentLogic>> hexavalentLogicGroups = new ConcurrentHashMap<>();

    private Map<String, Map<Long, List<DataMap>>> networksCluster = new ConcurrentHashMap<>();

    private List<String> availableNetworks = new ArrayList<>();

    public List<String> getAvailableNetworks() {
        return availableNetworks;
    }

    public List<NodeBean> getNetworkNodesAtIndex(String network) {
        return networkNodesAtIndex.get(network).keySet()
                .stream().map(key -> new NodeBean(network, key)).collect(Collectors.toList());
    }

    public List<NodeBean> findToNodeNeighbors(NodeBean nodeBean) {
        return networkNodesAtIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex()).stream()
                .map(this::bindToNodeBean).collect(Collectors.toList());
    }

    public List<NodeBean> getNetworkNodesToIndex(String network) {
        return networkNodesToIndex.get(network).keySet()
                .stream().map(key -> new NodeBean(network, key)).collect(Collectors.toList());
    }


    public String getTrivalentLogic(NodeBean nodeBean, ClusterBean clusterBean) {
        List<DataMap> dataMaps = Optional.ofNullable(networkNodesAtIndex.get(nodeBean.getNetwork())
                .get(nodeBean.getAddressIndex())).orElse(new ArrayList<>()).stream()
                .filter(DataMapFilterUtil.byClusterIndex(clusterBean.getClusterIndex()))
                .collect(Collectors.toList());
        if(dataMaps.size() > 0) {
            return dataMaps.get(0).getTrivalentLogic();
        }
        dataMaps = Optional.ofNullable(networkNodesToIndex.get(nodeBean.getNetwork())
                .get(nodeBean.getAddressIndex())).orElse(new ArrayList<>()).stream()
                .filter(DataMapFilterUtil.byClusterIndex(clusterBean.getClusterIndex()))
                .collect(Collectors.toList());
        if(dataMaps.size() > 0) {
            return dataMaps.get(0).getToTrivalentLogic();
        }
        return "";
    }

    public Set<NodeBean> getNetworkNodes(String network) {
        Set<NodeBean> networkNodes = new HashSet<>();
        if (network != null) {
            networkNodes.addAll(getNetworkNodesAtIndex(network));
            networkNodes.addAll(getNetworkNodesToIndex(network));
        }
        return networkNodes;
    }

    public List<ClusterBean> getNetworkClusters(String network) {
        if (network == null) {
            return new ArrayList<>();
        }
        return networksCluster.get(network).keySet()
                .stream().map(key -> new ClusterBean(network, key)).collect(Collectors.toList());
    }

    public Optional<Point> getRequestGraphicsCoordinates(NodeBean nb, ClusterBean cb, int startFrom, int totalParts, MediaRendererTransform mrt) {
        return Optional.of(splitPoint(getRequestGraphicsCoordinates(nb, mrt).get(),
                getRequestGraphicsCoordinates(cb, mrt).get(), startFrom, totalParts));
    }

    public Optional<Point> getRequestGraphicsCoordinates(EdgeBean edgeBean, int startFrom, int totalParts, MediaRendererTransform mrt) {
        return Optional.of(splitPoint(getRequestGraphicsCoordinates(edgeBean.getFromNode(), mrt).get(),
                getRequestGraphicsCoordinates(edgeBean.getToNode(), mrt).get(), startFrom, totalParts));
    }

    public Optional<Point> getRequestGraphicsCoordinates(NodeBean nb, MediaRendererTransform mrt) {
        Optional<Point> result = getAlgebraicCoordinates(nb);
        if (!result.isPresent()) {
            return result;
        }
        return Optional.of(CoordinatesUtil.getAlgebraicToRequestGraphics(result.get(), mrt));
    }

    public Optional<Point> getRequestGraphicsCoordinates(ClusterBean cb, MediaRendererTransform mrt) {
        Optional<Point> result = getAlgebraicCoordinates(cb);
        if (!result.isPresent()) {
            return result;
        }
        return Optional.of(CoordinatesUtil.getAlgebraicToRequestGraphics(result.get(), mrt));
    }

    public Optional<NodeBean> findNetworkNodeByScreenCoordinate(String network, Double x, Double y, MediaRendererTransform mrt) {
        Double nodeSelectionRangeDistance = findNetworkNodeSelectionRangeDistance(network, mrt);
        Optional<NodeBean> nodeAtIndex = findNetworkAtNodeByScreenCoordinate(network, nodeSelectionRangeDistance, x, y, mrt);
        return nodeAtIndex.isPresent() ? nodeAtIndex :
                findNetworkToNodeByScreenCoordinate(network, nodeSelectionRangeDistance, x, y, mrt);
    }

    public Optional<NodeBean> findNetworkNodeByScreenCoordinate(String network, Double x, Double y, MediaRendererTransform mrt, double nodeSelectionRangeDistance) {
        Optional<NodeBean> nodeAtIndex = findNetworkAtNodeByScreenCoordinate(network, nodeSelectionRangeDistance, x, y, mrt);
        return nodeAtIndex.isPresent() ? nodeAtIndex :
                findNetworkToNodeByScreenCoordinate(network, nodeSelectionRangeDistance, x, y, mrt);
    }

    public Optional<EdgeBean> findNetworkEdgeByScreenCoordinate(String network, Double x, Double y, MediaRendererTransform mrt, double nodeSelectionRangeDistance) {
//        Double nodeSelectionRangeDistance = findNetworkNodeSelectionRangeDistance(network, mrt);
        Optional<EdgeBean> edgeAtIndex = findNetworkAtEdgeByScreenCoordinate(network, nodeSelectionRangeDistance, x, y, mrt);
        return edgeAtIndex;
    }

    public Set<ClusterBean> findNodeClusters(NodeBean nodeBean) {
        // TODO should be cached ?
        return getNodeDataMaps(nodeBean).stream()
                .filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex))
                .map(this::bindClusterBean)
                .collect(Collectors.toSet());
    }

    public Set<EdgeBean> findNodeEdges(NodeBean nodeBean) {
        // TODO should be cached ?
        return getNodeDataMaps(nodeBean).stream()
                .map(this::bindEdgeBean)
                .collect(Collectors.toSet());
    }

    public Set<EdgeBean> findNodeClusterEdges(NodeBean nodeBean, ClusterBean clusterBean) {
        // TODO should be cached ?
        return getNodeClusterDataMaps(nodeBean, clusterBean).stream()
                .map(this::bindEdgeBean)
                .collect(Collectors.toSet());
    }

    public Set<ClusterBean> findEdgeClusters(EdgeBean edgeBean) {
        // TODO should be cached ?
        return getEdgeDataMaps(edgeBean).stream()
                .filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex))
                .map(this::bindClusterBean)
                .collect(Collectors.toSet());
    }

    public Set<NodeBean> findNodeNeighbors(NodeBean nodeBean) {
        Set<NodeBean> nodeNeighbors = new HashSet<>();
        List<DataMap> nodeDataMaps = getNodeDataMaps(nodeBean);
        nodeNeighbors.addAll(nodeDataMaps.stream()
                .filter(DataMapFilterUtil.byToAddressIndex(nodeBean.getAddressIndex()))
                .map(this::bindAtNodeBean)
                .collect(Collectors.toSet()));
        nodeNeighbors.addAll(nodeDataMaps.stream()
                .filter(DataMapFilterUtil.byAddressIndex(nodeBean.getAddressIndex()))
                .map(this::bindToNodeBean)
                .collect(Collectors.toSet()));
        return nodeNeighbors;
    }

    public Set<NodeBean> findClusterNodes(ClusterBean clusterBean) {
        // TODO should be cached ?
        Set<NodeBean> clusterNodes = new HashSet<>();
        clusterNodes.addAll(getClusterDataMaps(clusterBean).stream()
                .map(this::bindAtNodeBean)
                .collect(Collectors.toSet()));
        clusterNodes.addAll(getClusterDataMaps(clusterBean).stream()
                .map(this::bindToNodeBean)
                .collect(Collectors.toSet()));
        return clusterNodes;
    }

    public Set<EdgeBean> findNetworkEdges(String network) {
        // TODO should be cached ?
        return getNetworkDataMaps(network).stream()
                .map(this::bindEdgeBean)
                .collect(Collectors.toSet());
    }

    public Set<EdgeBean> findNetworkOuterEdges(String network) {
        // TODO should be cached ?
        return getNetworkDataMaps(network).stream()
                .filter(DataMapFilterUtil.byOuterRoutes())
                .map(this::bindEdgeBean)
                .collect(Collectors.toSet());
    }

    public Set<EdgeBean> findClusterEdges(ClusterBean clusterBean) {
        // TODO should be cached ?
        return getClusterDataMaps(clusterBean).stream()
                .map(this::bindEdgeBean)
                .collect(Collectors.toSet());
    }

    public Set<EdgeBean> findOuterClusterEdges(ClusterBean clusterBean) {
        // TODO should be cached ?
        return getClusterDataMaps(clusterBean).stream()
                .filter(DataMapFilterUtil.byOuterRoutes())
                .map(this::bindEdgeBean)
                .collect(Collectors.toSet());
    }

    /**
     * The list are the road computation by the system this need to be filter before as
     * a application display limit as 1000 roads for a selection .
     * This method could cover a random selection of 1000 limits roads
     */
    public List<List<NodeBean>> findRoad(NodeBean startNode, NodeBean endNode,
                                         List<NodeBean> haveNodes, List<NodeBean> excludeNodes,
                                         List<ClusterBean> excludeClusters) {
        // TODO Auto-generated method stub
        List<HexavalentLogic> hexavalentLogicGroup = getHexavalentLogicGroup(startNode.getNetwork());
        RouteBuilder routeBuilder = new RouteBuilder(hexavalentLogicGroup, startNode.getNetwork());
        return routeBuilder.computeHexavalentLogicRoute(startNode.getAddressIndex().intValue(), endNode.getAddressIndex().intValue(),
                excludeNodes.stream().map(en -> en.getAddressIndex().intValue()).collect(Collectors.toSet()),
                excludeClusters.stream().map(ec -> ec.getClusterIndex().intValue()).collect(Collectors.toSet()),
                haveNodes.stream().map(nb -> nb.getAddressIndex().intValue()).collect(Collectors.toSet())).stream()
                .map(aa -> aa.stream().map(a -> new NodeBean(startNode.getNetwork(), new Long(a))).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

//    public void saveNetworkSemantic(String network, SemanticBean semanticBean) {
//        // TODO Auto-generated method stub
//    }
//
//    public void saveClusterSemantic(ClusterBean clusterBean, SemanticBean semanticBean) {
//        // TODO Auto-generated method stub
//    }
//
//    public List<NodeBean> saveNodeSemantic(NodeBean nodeBean, SemanticBean semanticBean) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    public List<EdgeBean> saveEdgeSemantic(EdgeBean edgeBean, SemanticBean semanticBean) {
//        // TODO Auto-generated method stub
//        return null;
//    }

    public boolean isNodeInCluster(NodeBean nodeBean, ClusterBean clusterBean) {
        boolean inside = false;
        Point p = findNodeDataMap(nodeBean).get().getAtAddressCoordinates();
        Point cognitiveSourceAddress = getCognitiveSourceAddressCoordinates(clusterBean);

        Point polyI = cognitiveSourceAddress;
        Point polyJ = getSocialDeciderAddressCoordinates(clusterBean);

        if( (polyI.getY() < p.getY() && polyJ.getY() >= p.getY()
                || polyI.getY() >= p.getY() && polyJ.getY() < p.getY()) &&
                (polyI.getX() <= p.getX() || polyJ.getX() <= p.getX())) {
            inside ^= (polyI.getX() + (p.getY() - polyI.getY()) /
                    (polyJ.getY() - polyI.getY()) * (polyJ.getX() - polyI.getX()) < p.getX());
        }

        polyI = polyJ;
        polyJ = getCognitiveSensorAddressCoordinates(clusterBean);

        if( (polyI.getY() < p.getY() && polyJ.getY() >= p.getY()
                || polyI.getY() >= p.getY() && polyJ.getY() < p.getY()) &&
                (polyI.getX() <= p.getX() || polyJ.getX() <= p.getX())) {
            inside ^= (polyI.getX() + (p.getY() - polyI.getY()) /
                    (polyJ.getY() - polyI.getY()) * (polyJ.getX() - polyI.getX()) < p.getX());
        }

        polyI = polyJ;
        polyJ = getSocialSourceAddressCoordinates(clusterBean);

        if( (polyI.getY() < p.getY() && polyJ.getY() >= p.getY()
                || polyI.getY() >= p.getY() && polyJ.getY() < p.getY()) &&
                (polyI.getX() <= p.getX() || polyJ.getX() <= p.getX())) {
            inside ^= (polyI.getX() + (p.getY() - polyI.getY()) /
                    (polyJ.getY() - polyI.getY()) * (polyJ.getX() - polyI.getX()) < p.getX());
        }

        polyI = polyJ;
        polyJ = getCognitiveDeciderAddressCoordinates(clusterBean);

        if( (polyI.getY() < p.getY() && polyJ.getY() >= p.getY()
                || polyI.getY() >= p.getY() && polyJ.getY() < p.getY()) &&
                (polyI.getX() <= p.getX() || polyJ.getX() <= p.getX())) {
            inside ^= (polyI.getX() + (p.getY() - polyI.getY()) /
                    (polyJ.getY() - polyI.getY()) * (polyJ.getX() - polyI.getX()) < p.getX());
        }

        polyI = polyJ;
        polyJ = getSocialSensorAddressCoordinates(clusterBean);

        if( (polyI.getY() < p.getY() && polyJ.getY() >= p.getY()
                || polyI.getY() >= p.getY() && polyJ.getY() < p.getY()) &&
                (polyI.getX() <= p.getX() || polyJ.getX() <= p.getX())) {
            inside ^= (polyI.getX() + (p.getY() - polyI.getY()) /
                    (polyJ.getY() - polyI.getY()) * (polyJ.getX() - polyI.getX()) < p.getX());
        }

        polyI = polyJ;
        polyJ = cognitiveSourceAddress;

        if( (polyI.getY() < p.getY() && polyJ.getY() >= p.getY()
                || polyI.getY() >= p.getY() && polyJ.getY() < p.getY()) &&
                (polyI.getX() <= p.getX() || polyJ.getX() <= p.getX())) {
            inside ^= (polyI.getX() + (p.getY() - polyI.getY()) /
                    (polyJ.getY() - polyI.getY()) * (polyJ.getX() - polyI.getX()) < p.getX());
        }

        return inside;
    }

    private Point getCognitiveSourceAddressCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> dataMap = findClusterNodes(clusterBean).stream()
                .map(nb -> findNodeDataMap(nb).get())
                .filter(DataMapFilterUtil.byTrivalentLogicType("cognitive_outer"))
                .filter(DataMapFilterUtil.bySourceAddresses())
                .findAny();
        if(dataMap.isPresent()) {
            return dataMap.get().getAtAddressCoordinates();
        }
        dataMap =
                findClusterNodes(clusterBean).stream()
                        .map(nb -> findNodeDataMap(nb).get())
                        .filter(DataMapFilterUtil.byToTrivalentLogicType("cognitive_outer"))
                        .filter(DataMapFilterUtil.byToSourceAddresses())
                        .findAny();
        return dataMap.get().getToAddressCoordinates();
    }

    private Point getCognitiveSensorAddressCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> dataMap = findClusterNodes(clusterBean).stream()
                .map(nb -> findNodeDataMap(nb).get())
                .filter(DataMapFilterUtil.byTrivalentLogicType("cognitive_outer"))
                .filter(DataMapFilterUtil.bySensorAddresses())
                .findAny();
        if(dataMap.isPresent()) {
            return dataMap.get().getAtAddressCoordinates();
        }
        dataMap =
                findClusterNodes(clusterBean).stream()
                        .map(nb -> findNodeDataMap(nb).get())
                        .filter(DataMapFilterUtil.byToTrivalentLogicType("cognitive_outer"))
                        .filter(DataMapFilterUtil.byToSensorAddresses())
                        .findAny();
        return dataMap.get().getToAddressCoordinates();
    }

    private Point getCognitiveDeciderAddressCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> dataMap = findClusterNodes(clusterBean).stream()
                .map(nb -> findNodeDataMap(nb).get())
                .filter(DataMapFilterUtil.byTrivalentLogicType("cognitive_outer"))
                .filter(DataMapFilterUtil.byDeciderAddresses())
                .findAny();
        if(dataMap.isPresent()) {
            return dataMap.get().getAtAddressCoordinates();
        }
        dataMap =
                findClusterNodes(clusterBean).stream()
                        .map(nb -> findNodeDataMap(nb).get())
                        .filter(DataMapFilterUtil.byToTrivalentLogicType("cognitive_outer"))
                        .filter(DataMapFilterUtil.byToDeciderAddresses())
                        .findAny();
        return dataMap.get().getToAddressCoordinates();
    }

    private Point getSocialSourceAddressCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> dataMap = findClusterNodes(clusterBean).stream()
                .map(nb -> findNodeDataMap(nb).get())
                .filter(DataMapFilterUtil.byTrivalentLogicType("social_outer"))
                .filter(DataMapFilterUtil.bySourceAddresses())
                .findAny();
        if(dataMap.isPresent()) {
            return dataMap.get().getAtAddressCoordinates();
        }
        dataMap =
                findClusterNodes(clusterBean).stream()
                        .map(nb -> findNodeDataMap(nb).get())
                        .filter(DataMapFilterUtil.byToTrivalentLogicType("social_outer"))
                        .filter(DataMapFilterUtil.byToSourceAddresses())
                        .findAny();
        return dataMap.get().getToAddressCoordinates();
    }

    private Point getSocialSensorAddressCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> dataMap = findClusterNodes(clusterBean).stream()
                .map(nb -> findNodeDataMap(nb).get())
                .filter(DataMapFilterUtil.byTrivalentLogicType("social_outer"))
                .filter(DataMapFilterUtil.bySensorAddresses())
                .findAny();
        if(dataMap.isPresent()) {
            return dataMap.get().getAtAddressCoordinates();
        }
        dataMap =
                findClusterNodes(clusterBean).stream()
                        .map(nb -> findNodeDataMap(nb).get())
                        .filter(DataMapFilterUtil.byToTrivalentLogicType("social_outer"))
                        .filter(DataMapFilterUtil.byToSensorAddresses())
                        .findAny();
        return dataMap.get().getToAddressCoordinates();
    }

    private Point getSocialDeciderAddressCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> dataMap = findClusterNodes(clusterBean).stream()
                .map(nb -> findNodeDataMap(nb).get())
                .filter(DataMapFilterUtil.byTrivalentLogicType("social_outer"))
                .filter(DataMapFilterUtil.byDeciderAddresses())
                .findAny();
        if(dataMap.isPresent()) {
            return dataMap.get().getAtAddressCoordinates();
        }
        dataMap =
                findClusterNodes(clusterBean).stream()
                        .map(nb -> findNodeDataMap(nb).get())
                        .filter(DataMapFilterUtil.byToTrivalentLogicType("social_outer"))
                        .filter(DataMapFilterUtil.byToDeciderAddresses())
                        .findAny();
        return dataMap.get().getToAddressCoordinates();
    }

    public List<DataMap> getNetworkDataMaps(String network) {
        return Optional.ofNullable(networksDataMaps.get(network))
                .orElseGet(() -> putAndGetNetworkDataMaps(network));
    }

    public Optional<DataMap> findClusterDataMap(ClusterBean clusterBean) {
        List<DataMap> clusterDataMaps = networksCluster.get(clusterBean.getNetwork())
                .get(clusterBean.getClusterIndex());
        return (clusterDataMaps == null || clusterDataMaps.size() == 0) ? Optional.empty() :
                Optional.of(clusterDataMaps.get(0));
    }

    public List<DataMap> findClusterDataMaps(ClusterBean clusterBean) {
        return networksCluster.get(clusterBean.getNetwork())
                .get(clusterBean.getClusterIndex());
    }

    public List<DataMap> findNodeWells(NodeBean nodeBean) {
        List<DataMap> wells = new ArrayList<>();
        Optional<DataMap> nodeDataMap = findNodeDataMap(nodeBean);
        if (nodeDataMap.isPresent()) {
            getAvailableNetworks().stream()
                    .forEach(network -> wells
                            .addAll(getNetworkDataMaps(network).stream()
                                    .filter(DataMapFilterUtil
                                            .byAddressesAt(nodeDataMap.get().getAtX(), nodeDataMap.get().getAtY()))
                                    .collect(Collectors.toList())));
        }
        return wells;
    }

    public List<DataMap> findEdgeWells(EdgeBean edgeBean) {
        List<DataMap> wells = new ArrayList<>();
        Optional<DataMap> nodeDataMap = findEdgeDataMap(edgeBean);
        if (nodeDataMap.isPresent()) {
            getAvailableNetworks().stream()
                    .forEach(network -> wells
                            .addAll(getNetworkDataMaps(network).stream()
                                    .filter(DataMapFilterUtil
                                            .byRoute(nodeDataMap.get().getAtX(), nodeDataMap.get().getAtY(),
                                                    nodeDataMap.get().getToX(), nodeDataMap.get().getToY()))
                                    .collect(Collectors.toList())));
        }
        return wells;
    }

    public Optional<DataMap> findNodeDataMap(NodeBean nodeBean) {
        List<DataMap> atIndexDataMaps = networkNodesAtIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex());
        if (atIndexDataMaps == null || atIndexDataMaps.size() == 0) {
            List<DataMap> toIndexDataMaps = networkNodesToIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex());
            return (toIndexDataMaps == null || toIndexDataMaps.size() == 0) ? Optional.empty() :
                    Optional.of(toIndexDataMaps.get(0));
        }
        return Optional.of(atIndexDataMaps.get(0));
    }

    public Optional<NodeSemanticBean> findWellsNodeSemanticBean(NodeBean nodeBean, Long applicationDataId) {
        Optional<DataMap> dataMap = findNodeDataMap(nodeBean);
        if (dataMap.isPresent() == false)
            return Optional.empty();
        ApplicationData applicationData = applicationDataRepositoryService
                .getApplicationData(applicationDataId, dataMap.get());
        return Optional.of(new NodeSemanticBean(nodeBean, applicationData.getSemantic(), applicationData.getSemanticDetails()));
    }

    public Optional<EdgeSemanticBean> findWellsNodeSemanticBean(EdgeBean edgeBean, Long applicationDataId) {
        Optional<DataMap> dataMap = findEdgeDataMap(edgeBean);
        if (dataMap.isPresent() == false)
            return Optional.empty();
        ApplicationData applicationData = applicationDataRepositoryService
                .getApplicationData(applicationDataId, dataMap.get());
        return Optional.of(new EdgeSemanticBean(edgeBean, applicationData.getSyntax(), applicationData.getSyntaxDetails()));
    }

    public Optional<DataMap> findEdgeDataMap(EdgeBean edgeBean) {
        List<DataMap> edgeDataMaps = getEdgeDataMaps(edgeBean);
        return (edgeDataMaps == null || edgeDataMaps.size() == 0) ? Optional.empty() :
                Optional.of(edgeDataMaps.get(0));
    }

    public Optional<DataMap> findEdgeClusterDataMap(EdgeBean edgeBean, ClusterBean clusterBean) {
        List<DataMap> edgeDataMaps = getEdgeDataMaps(edgeBean).stream()
                .filter(DataMapFilterUtil.byClusterIndex(clusterBean.getClusterIndex()))
                .collect(Collectors.toList());
        return (edgeDataMaps == null || edgeDataMaps.size() == 0) ? Optional.empty() :
                Optional.of(edgeDataMaps.get(0));
    }

    @PostConstruct
    private void buildNetwork() {
        for (int i = 0; i <= deepMax; i++) {
            buildDeep(i);
        }
    }

    private List<HexavalentLogic> getHexavalentLogicGroup(String network) {
        List<HexavalentLogic> hexavalentLogicGroup =
                Optional.ofNullable(hexavalentLogicGroups.get(network)).orElseGet(() -> new ArrayList<>());
        if (hexavalentLogicGroup.size() > 0)
            return hexavalentLogicGroup;



        NetworkFactory nf = new NetworkFactory() {
        };
        String[] nettingDeep = network.split("::");
        int deep = Integer.parseInt(nettingDeep[1]);
        if (deep == 0 || deep == 1) {
	        NetworkBuilder networkBuilder =
	                nf.getNetworkBuilder(NettingType.valueOf(nettingDeep[0]),
	                100_000_000, deep, 1, 1);
	        hexavalentLogicGroup = nf.getHexavalentLogic(networkBuilder);
        }else {
        	NetworkBuilder networkBuilder =
	                nf.getNetworkBuilderStarNorthWest(NettingType.valueOf(nettingDeep[0]),
	                100_000_000, deep, 1, 1);
	        hexavalentLogicGroup = nf.getHexavalentLogic(networkBuilder);
        }
//        getNetworkClusters(network).stream()
//                .map(this::bindHexavalentLogic)
//                .forEach(hexavalentLogic -> hexavalentLogicGroup.add(hexavalentLogic));

        hexavalentLogicGroups.put(network, hexavalentLogicGroup);
        return hexavalentLogicGroups.get(network);
    }

    private List<DataMap> getClusterDataMaps(ClusterBean clusterBean) {
        List<DataMap> clusterRelated = new ArrayList<>();
        clusterRelated.addAll(Optional.ofNullable(networksCluster.get(clusterBean.getNetwork()).get(clusterBean.getClusterIndex()))
                .orElseGet(() -> new ArrayList<>()));
        return clusterRelated;
    }

    private List<DataMap> getNodeDataMaps(NodeBean nodeBean) {
        List<DataMap> nodeRelated = new ArrayList<>();
        nodeRelated.addAll(Optional.ofNullable(networkNodesAtIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex()))
                .orElseGet(() -> new ArrayList<>()));
        nodeRelated.addAll(Optional.ofNullable(networkNodesToIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex()))
                .orElseGet(() -> new ArrayList<>()));
        return nodeRelated;
    }

    private List<DataMap> getNodeClusterDataMaps(NodeBean nodeBean, ClusterBean clusterBean) {
        List<DataMap> nodeClusterRelated = new ArrayList<>();
        nodeClusterRelated.addAll(networkNodesAtIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex()).stream()
                .filter(DataMapFilterUtil.byClusterIndex(clusterBean.getClusterIndex())).collect(Collectors.toList()));
        nodeClusterRelated.addAll(networkNodesToIndex.get(nodeBean.getNetwork()).get(nodeBean.getAddressIndex()).stream()
                .filter(DataMapFilterUtil.byClusterIndex(clusterBean.getClusterIndex())).collect(Collectors.toList()));
        return nodeClusterRelated;
    }

    private List<DataMap> getEdgeDataMaps(EdgeBean edgeBean) {
        List<DataMap> edgeRelated = new ArrayList<>();
        edgeRelated.addAll(getNetworkDataMaps(edgeBean.getFromNode().getNetwork())
                .stream()
                .filter(DataMapFilterUtil.byAddressIndex(edgeBean.getFromNode().getAddressIndex()))
                .filter(DataMapFilterUtil.byToAddressIndex(edgeBean.getToNode().getAddressIndex()))
                .collect(Collectors.toList()));
        return edgeRelated;
    }

    private HexavalentLogic bindHexavalentLogic(ClusterBean clusterBean) {
        HexavalentLogic hexavalentLogic = new HexavalentLogic();
        Optional<DataMap> clusterDataMap = findClusterDataMap(clusterBean);
        if (clusterDataMap.isPresent()) {
            hexavalentLogic.setBeltIndex(clusterDataMap.get().getBeltIndex());
            hexavalentLogic.setClusterType(ClusterType.valueOf(clusterDataMap.get().getClusterType()));
            hexavalentLogic.setCenter(getAlgebraicCoordinates(clusterBean).get());
            hexavalentLogic.setColumn(clusterDataMap.get().getColumnIndex());
            hexavalentLogic.setRow(clusterDataMap.get().getRowIndex());
            hexavalentLogic.setIndex(clusterBean.getClusterIndex().intValue());
            hexavalentLogic.setCognitiveOuter(bindTrivalentLogic(clusterBean, TrivalentLogicType.COGNITIVE_OUTER));
            hexavalentLogic.setSocialOuter(bindTrivalentLogic(clusterBean, TrivalentLogicType.SOCIAL_OUTER));
            hexavalentLogic.setCognitiveInner(bindTrivalentLogic(clusterBean, TrivalentLogicType.COGNITIVE_INNER));
            hexavalentLogic.setSocialInner(bindTrivalentLogic(clusterBean, TrivalentLogicType.SOCIAL_INNER));
            hexavalentLogic.setOuterVerticesConnectors(bindOuterVerticesConnectors(clusterBean));
            hexavalentLogic.setInnerVerticesConnectors(bindInnerVerticesConnectors(clusterBean));
        }
        return hexavalentLogic;
    }

    private List<Connector> bindOuterVerticesConnectors(ClusterBean clusterBean) {
        List<DataMap> clusterDataMaps = networksCluster.get(clusterBean.getNetwork())
                .get(clusterBean.getClusterIndex());
        return
                clusterDataMaps.stream()
                        .filter(DataMapFilterUtil.byOuterRoutes())
                        .map(dataMap -> bindConnector(bindVertex(new NodeBean(clusterBean.getNetwork(), dataMap.getAddressIndex()),
                                VertexType.valueOf(dataMap.getTrivalentLogic())),
                                bindVertex(new NodeBean(clusterBean.getNetwork(), dataMap.getToAddressIndex()),
                                        VertexType.valueOf(dataMap.getToTrivalentLogic()))))
                        .collect(Collectors.toList());

    }

    private List<Connector> bindInnerVerticesConnectors(ClusterBean clusterBean) {
        List<DataMap> clusterDataMaps = networksCluster.get(clusterBean.getNetwork())
                .get(clusterBean.getClusterIndex());
        return
                clusterDataMaps.stream()
                        .filter(DataMapFilterUtil.byInnerRoutes())
                        .map(dataMap -> bindConnector(bindVertex(new NodeBean(clusterBean.getNetwork(), dataMap.getAddressIndex()),
                                VertexType.valueOf(dataMap.getTrivalentLogic())),
                                bindVertex(new NodeBean(clusterBean.getNetwork(), dataMap.getToAddressIndex()),
                                        VertexType.valueOf(dataMap.getToTrivalentLogic()))))
                        .collect(Collectors.toList());

    }

    private Connector bindConnector(Vertex head, Vertex tail) {
        return new Connector(head, tail);
    }

    private TrivalentLogic bindTrivalentLogic(ClusterBean clusterBean, TrivalentLogicType trivalentLogicType) {
        List<DataMap> clusterDataMaps = networksCluster.get(clusterBean.getNetwork())
                .get(clusterBean.getClusterIndex());
        NodeBean sourceNode = null;
        Optional<DataMap> source = clusterDataMaps.stream()
                .filter(DataMapFilterUtil.byTrivalentLogicType(trivalentLogicType.name()))
                .filter(DataMapFilterUtil.bySourceAddresses())
                .findAny();
        if (source.isPresent() == false) {
            source = clusterDataMaps.stream()
                    .filter(DataMapFilterUtil.byTrivalentLogicType(trivalentLogicType.name()))
                    .filter(DataMapFilterUtil.byToSourceAddresses())
                    .findAny();
            sourceNode = new NodeBean(clusterBean.getNetwork(), source.get().getToAddressIndex());
        } else {
            sourceNode = new NodeBean(clusterBean.getNetwork(), source.get().getAddressIndex());
        }
        NodeBean sensorNode = null;
        Optional<DataMap> sensor = clusterDataMaps.stream()
                .filter(DataMapFilterUtil.byTrivalentLogicType(trivalentLogicType.name()))
                .filter(DataMapFilterUtil.bySensorAddresses())
                .findAny();
        if (sensor.isPresent() == false) {
            sensor = clusterDataMaps.stream()
                    .filter(DataMapFilterUtil.byTrivalentLogicType(trivalentLogicType.name()))
                    .filter(DataMapFilterUtil.byToSensorAddresses())
                    .findAny();
            sensorNode = new NodeBean(clusterBean.getNetwork(), sensor.get().getToAddressIndex());
        } else {
            sensorNode = new NodeBean(clusterBean.getNetwork(), sensor.get().getAddressIndex());
        }
        NodeBean deciderNode = null;
        Optional<DataMap> decider = clusterDataMaps.stream()
                .filter(DataMapFilterUtil.byTrivalentLogicType(trivalentLogicType.name()))
                .filter(DataMapFilterUtil.byDeciderAddresses())
                .findAny();
        if (decider.isPresent() == false) {
            decider = clusterDataMaps.stream()
                    .filter(DataMapFilterUtil.byTrivalentLogicType(trivalentLogicType.name()))
                    .filter(DataMapFilterUtil.byToDeciderAddresses())
                    .findAny();
            deciderNode = new NodeBean(clusterBean.getNetwork(), decider.get().getToAddressIndex());
        } else {
            deciderNode = new NodeBean(clusterBean.getNetwork(), decider.get().getAddressIndex());
        }

        TrivalentLogic trivalentLogic = new TrivalentLogic(trivalentLogicType,
                bindVertex(sourceNode, VertexType.SOURCE),
                bindVertex(sensorNode, VertexType.SENSOR),
                bindVertex(deciderNode, VertexType.DECIDER));
        return trivalentLogic;
    }

    private Vertex bindVertex(NodeBean nodeBean, VertexType vertexType) {
        return new Vertex(nodeBean.getAddressIndex().intValue(), vertexType, getAlgebraicCoordinates(nodeBean).get());
    }

    private NodeBean bindAtNodeBean(DataMap dataMap) {
        return new NodeBean(dataMap.getNetwork(), dataMap.getAddressIndex());
    }

    private NodeBean bindToNodeBean(DataMap dataMap) {
        return new NodeBean(dataMap.getNetwork(), dataMap.getToAddressIndex());
    }

    private ClusterBean bindClusterBean(DataMap dataMap) {
        return new ClusterBean(dataMap.getNetwork(), dataMap.getClusterIndex());
    }

    private EdgeBean bindEdgeBean(DataMap dataMap) {
        return new EdgeBean(bindAtNodeBean(dataMap), bindToNodeBean(dataMap));
    }

    private List<DataMap> putAndGetNetworkDataMaps(String network) {
        List<DataMap> routes = new ArrayList<>();
        networksCluster.get(network).values().stream()
                .forEach(clusterDataMaps -> routes.addAll(clusterDataMaps));
        networksDataMaps.put(network, routes);
        return routes;
    }

    private Optional<Point> getAlgebraicCoordinates(NodeBean nodeBean) {
        return findNodeDataMap(nodeBean)
                .map(dataMap -> {
                    if (dataMap.getAddressIndex().compareTo(nodeBean.getAddressIndex()) == 0) {
                        return dataMap.getAtAddressCoordinates();
                    }
                    return dataMap.getToAddressCoordinates();
                });
    }

    private Optional<Point> getAlgebraicCoordinates(ClusterBean clusterBean) {
        Optional<DataMap> clusterDataMap = findClusterDataMap(clusterBean);
        return (clusterDataMap.isPresent()) ? Optional.of(clusterDataMap.get().getClusterCoordinates()) :
                Optional.empty();
    }

    private Optional<NodeBean> findNetworkAtNodeByScreenCoordinate(String network, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        return getNetworkNodesAtIndex(network).stream().filter(nodeBean -> isNodeBeanTouched(nodeBean, radius, x, y, mrt))
                .findFirst();
    }

    private Optional<EdgeBean> findNetworkAtEdgeByScreenCoordinate(String network, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        List<EdgeBean> edgeBeans = getNetworkDataMaps(network).stream().map(dm ->
                new EdgeBean(
                        new NodeBean(network, dm.getAddressIndex()),
                        new NodeBean(network, dm.getToAddressIndex())
                )).filter(eb -> isEdgeBeanTouched(eb, radius, x, y, mrt)).
                collect(Collectors.toList()).stream().
                sorted((eb1, eb2) -> (int) (distanceBetweenNodes(eb1.getFromNode(), eb1.getToNode(), mrt) -
                        distanceBetweenNodes(eb2.getFromNode(), eb2.getToNode(), mrt))
                ).collect(Collectors.toList());
        if (edgeBeans.size() > 0) {
            return Optional.of(edgeBeans.get(0));
        }
        return Optional.empty();
//        return getNetworkNodesAtIndex(network).stream().filter(nodeBean -> isNodeBeanTouched(nodeBean, radius, x, y, mrt))
//                .findAny();
    }

    private double distanceBetweenNodes(NodeBean from, NodeBean to, MediaRendererTransform mrt) {
        Optional<Point> fromNodeBeanCoordiantes = getRequestGraphicsCoordinates(from, mrt);
        Optional<Point> toNodeBeanCoordiantes = getRequestGraphicsCoordinates(to, mrt);
        if (!fromNodeBeanCoordiantes.isPresent() || !toNodeBeanCoordiantes.isPresent()) {
            return 0;
        }
        return GeometryUtil.getDistance(fromNodeBeanCoordiantes.get(), toNodeBeanCoordiantes.get());
    }

    private boolean isEdgeBeanTouched(EdgeBean edgeBean, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        Optional<Point> fromNodeBeanCoordiantes = getRequestGraphicsCoordinates(edgeBean.getFromNode(), mrt);
        Optional<Point> toNodeBeanCoordiantes = getRequestGraphicsCoordinates(edgeBean.getToNode(), mrt);
        if (!fromNodeBeanCoordiantes.isPresent() || !toNodeBeanCoordiantes.isPresent()) {
            return false;
        }

        Point perpendicularPoint = GeometryUtil.getPerpendicularPoint(fromNodeBeanCoordiantes.get(), toNodeBeanCoordiantes.get(), new Point(x, y));
        return /*
         * GeometryUtil.isOnSegmentBigDecimal(fromNodeBeanCoordiantes.get(),
         * toNodeBeanCoordiantes.get(), new Point(x, y)) &&
         */
                GeometryUtil.isOnSegmentBigDecimal(fromNodeBeanCoordiantes.get(), toNodeBeanCoordiantes.get(), perpendicularPoint, radius)
                        && GeometryUtil.getDistance(perpendicularPoint, new Point(x, y)) < radius;
    }

    private Optional<NodeBean> findNetworkToNodeByScreenCoordinate(String network, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        return getNetworkNodesToIndex(network).stream().filter(nodeBean -> isNodeBeanTouched(nodeBean, radius, x, y, mrt))
                .findAny();
    }

    private boolean isNodeBeanTouched(NodeBean nodeBean, Double radius, Double x, Double y, MediaRendererTransform mrt) {
        Optional<Point> nodeBeanCoordiantes = getRequestGraphicsCoordinates(nodeBean, mrt);
        return getRequestGraphicsCoordinates(nodeBean, mrt).isPresent() ?
                GeometryUtil.getDistance(nodeBeanCoordiantes.get(), new Point(x, y)) < radius : false;
    }

    private Optional<DataMap> findNetworkMinRoute(String network) {
        return getNetworkDataMaps(network).stream()
                .filter(DataMapFilterUtil.byInnerRoutes())
                .filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex)).findAny();

    }

    private Double findNetworkMinRouteDistance(String network, MediaRendererTransform mrt) {
        //if networksAtIndex.get(network) != null ???
        Optional<DataMap> minRouteDataMap = Optional.ofNullable(networkMinRoute.get(network));
        if (minRouteDataMap.isPresent()) {
            return findRouteDistance(minRouteDataMap.get(), mrt);
        }
        minRouteDataMap = findNetworkMinRoute(network);
        if (minRouteDataMap.isPresent()) {
            networkMinRoute.put(network, minRouteDataMap.get());
            return findRouteDistance(minRouteDataMap.get(), mrt);
        }
        return 0D;
    }

    private Double findRouteDistance(DataMap dataMap, MediaRendererTransform mrt) {
        Point a = getRequestGraphicsCoordinates(bindAtNodeBean(dataMap), mrt).get();
        Point b = getRequestGraphicsCoordinates(bindToNodeBean(dataMap), mrt).get();
        return GeometryUtil.getDistance(a, b);

    }

    public Double findNetworkNodeSelectionRangeDistance(String network, MediaRendererTransform mrt) {
        return findNetworkMinRouteDistance(network, mrt) / 2;
    }

    private void buildNetwork(NettingType nettingType, int deep) {
        buildNetworkDataMap("Central" + nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get("Central" + nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get("Central" + nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add("Central" + nettingType.name() + "::" + deep);
        }
        //NorthCentral
        buildNetworkDataMap(nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get(nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get(nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add(nettingType.name() + "::" + deep);
        }
        buildNetworkDataMap("SouthCentral" + nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get("SouthCentral" + nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get("SouthCentral" + nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add("SouthCentral" + nettingType.name() + "::" + deep);
        }

        buildNetworkDataMap("NorthWest" + nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get("NorthWest" + nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get("NorthWest" + nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add("NorthWest" + nettingType.name() + "::" + deep);
        }
        buildNetworkDataMap("SouthWest" + nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get("SouthWest" + nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get("SouthWest" + nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add("SouthWest" + nettingType.name() + "::" + deep);
        }
        buildNetworkDataMap("NorthEst" + nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get("NorthEst" + nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get("NorthEst" + nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add("NorthEst" + nettingType.name() + "::" + deep);
        }
        buildNetworkDataMap("SouthEst" + nettingType.name() + "::" + deep);
        if(networkNodesAtIndex.get("SouthEst" + nettingType.name() + "::" + deep) != null
                && networkNodesAtIndex.get("SouthEst" + nettingType.name() + "::" + deep).size() > 0) {
            availableNetworks.add("SouthEst" + nettingType.name() + "::" + deep);
        }
    }

    private void buildDeep(int deep) {
        buildNetwork(NettingType.SUSTAINABLE_EDGES, deep);
        buildNetwork(NettingType.SUSTAINABLE_VERTICES, deep);
        buildNetwork(NettingType.METABOLIC_VERTICES, deep);
        buildNetwork(NettingType.METABOLIC_EDGES, deep);
    }

    private void buildNetworkDataMap(String network) {
        dataMapRepository.findAllByNetwork(network).stream()
                .forEach(dataMap -> buildCache(network, dataMap));
    }

    public List<NodeBean> getInformationContainedNetworkNodeBeans(String network, Long applicationId) {
        return dataMapRepository.findAllNodesContainingInformationByNetwork(network, applicationId).stream()
                .map(projection -> new NodeBean(network, projection.getAddressIndex()))
                .collect(Collectors.toList());
    }

    public List<EdgeBean> getInformationContainedNetworkEdgeBeans(String network, Long applicationId) {
        return dataMapRepository.findAllEdgesContainingInformationByNetwork(network, applicationId).stream()
                .map(projection -> new EdgeBean(new NodeBean(network, projection.getAddressIndex()), new NodeBean(network, projection.getToAddressIndex())))
                .collect(Collectors.toList());
    }

    private void buildCache(String network, DataMap dataMap) {
        putNetworksAtIndex(network, dataMap);
        putNetworksToIndex(network, dataMap);
        putNetworksClusterIndex(network, dataMap);
    }

    private void putNetworksAtIndex(String network, DataMap dataMap) {
        Optional.ofNullable(networkNodesAtIndex.get(network))
                .orElseGet(() -> networkNodesAtIndex.put(network, new ConcurrentHashMap<>()));
        Optional.ofNullable(networkNodesAtIndex.get(network).get(dataMap.getAddressIndex()))
                .orElseGet(() -> networkNodesAtIndex.get(network).put(dataMap.getAddressIndex(), new ArrayList<>()));
        networkNodesAtIndex.get(network).get(dataMap.getAddressIndex()).add(dataMap);
    }

    private void putNetworksToIndex(String network, DataMap dataMap) {
        Optional.ofNullable(networkNodesToIndex.get(network))
                .orElseGet(() -> networkNodesToIndex.put(network, new ConcurrentHashMap<>()));
        Optional.ofNullable(networkNodesToIndex.get(network).get(dataMap.getToAddressIndex()))
                .orElseGet(() -> networkNodesToIndex.get(network).put(dataMap.getToAddressIndex(), new ArrayList<>()));
        networkNodesToIndex.get(network).get(dataMap.getToAddressIndex()).add(dataMap);
    }

    private void putNetworksClusterIndex(String network, DataMap dataMap) {
        Optional.ofNullable(networksCluster.get(network))
                .orElseGet(() -> networksCluster.put(network, new ConcurrentHashMap<>()));
        Optional.ofNullable(networksCluster.get(network).get(dataMap.getClusterIndex()))
                .orElseGet(() -> networksCluster.get(network).put(dataMap.getClusterIndex(), new ArrayList<>()));
        networksCluster.get(network).get(dataMap.getClusterIndex()).add(dataMap);
    }


    public Point splitPoint(Point head, Point tail, int part, int totalParts) {
        return new Point(head.getX() + ( tail.getX() - head.getX() ) / totalParts * part ,
                head.getY() + ( tail.getY() - head.getY() ) / totalParts * part);
    }

    public Optional<DataMap> findDataMapByNetworkAndAddressIndex(String network, Long addressIndex) {
        return networkNodesAtIndex.get(network).get(addressIndex).stream().findFirst();
    }


}
