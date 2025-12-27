package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

import org.hkrdi.eden.ggm.algebraic.VertexType;
import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.springframework.cop.support.BeanUtil;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DataMapFilterUtil {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static Predicate<DataMap> byInside(ClusterBean clusterBean) {
        return dataMap -> BeanUtil.getBean(CoherentSpaceService.class)
                .isNodeInCluster(new NodeBean(dataMap.getNetwork(), dataMap.getAddressIndex()), clusterBean);
    }

    public static Predicate<DataMap> byNotInside(ClusterBean clusterBean) {
        return dataMap -> !BeanUtil.getBean(CoherentSpaceService.class)
                .isNodeInCluster(new NodeBean(dataMap.getNetwork(), dataMap.getAddressIndex()), clusterBean);
    }

    public static Predicate<DataMap> byId(Long id) {
        return dataMap -> dataMap.getId().equals(id);
    }
    
    public static Predicate<DataMap> byAddressesAt(Long x, Long y) {
        return dataMap -> DataMap.toDoubleAddress(dataMap.getAtX())
                .compareTo(DataMap.toDoubleAddress(x)) == 0 &&
                DataMap.toDoubleAddress(dataMap.getAtY()).compareTo(DataMap.toDoubleAddress(y)) == 0;
    }

    public static Predicate<DataMap> byAddressesTo(Long x, Long y) {
        return dataMap -> DataMap.toDoubleAddress(dataMap.getToX())
                .compareTo(DataMap.toDoubleAddress(x)) == 0 &&
                DataMap.toDoubleAddress(dataMap.getToY()).compareTo(DataMap.toDoubleAddress(y)) == 0;
    }

    public static Predicate<DataMap> byRoute(Long x, Long y, Long tox, Long toy) {
        return dataMap -> DataMap.toDoubleAddress(dataMap.getAtX())
                .compareTo(DataMap.toDoubleAddress(x)) == 0 &&
                DataMap.toDoubleAddress(dataMap.getAtY()).compareTo(DataMap.toDoubleAddress(y)) == 0 &&
                DataMap.toDoubleAddress(dataMap.getToX())
                        .compareTo(DataMap.toDoubleAddress(tox)) == 0 &&
                DataMap.toDoubleAddress(dataMap.getToY()).compareTo(DataMap.toDoubleAddress(toy)) == 0;
    }

    public static Predicate<DataMap> byDataMapId(Long dataMapId) {
        return dataMap -> dataMap.getId().compareTo(dataMapId) == 0;
    }

    public static Predicate<DataMap> byAddressIndex(Long addressIndex) {
        return dataMap -> dataMap.getAddressIndex().compareTo(addressIndex) == 0;
    }

    public static Predicate<DataMap> byToAddressIndex(Long addressIndex) {
        return dataMap -> dataMap.getToAddressIndex().compareTo(addressIndex) == 0;
    }

    public static Predicate<DataMap> bySourceAddresses() {
        return dataMap -> dataMap.getTrivalentLogic().equalsIgnoreCase(VertexType.SOURCE.name());
    }

    public static Predicate<DataMap> byToSourceAddresses() {
        return dataMap -> dataMap.getToTrivalentLogic().equalsIgnoreCase(VertexType.SOURCE.name());
    }

    public static Predicate<DataMap> bySensorAddresses() {
        return dataMap -> dataMap.getTrivalentLogic().equalsIgnoreCase(VertexType.SENSOR.name());
    }

    public static Predicate<DataMap> byToSensorAddresses() {
        return dataMap -> dataMap.getToTrivalentLogic().equalsIgnoreCase(VertexType.SENSOR.name());
    }

    public static Predicate<DataMap> byDeciderAddresses() {
        return dataMap -> dataMap.getTrivalentLogic().equalsIgnoreCase(VertexType.DECIDER.name());
    }

    public static Predicate<DataMap> byToDeciderAddresses() {
        return dataMap -> dataMap.getToTrivalentLogic().equalsIgnoreCase(VertexType.DECIDER.name());
    }

    public static Predicate<DataMap> byTrivalentLogicType(String trivalentLogicType) {
        return dataMap -> dataMap.getTrivalentLogicType().equalsIgnoreCase(trivalentLogicType);
    }

    public static Predicate<DataMap> byToTrivalentLogicType(String trivalentLogicType) {
        return dataMap -> dataMap.getToTrivalentLogicType().equalsIgnoreCase(trivalentLogicType);
    }

    public static Predicate<DataMap> byCognitiveRoutes() {
        return dataMap -> dataMap.getTrivalentLogicType().contains("COGNITIVE") &&
                dataMap.getToTrivalentLogicType().contains("COGNITIVE");
    }

    public static Predicate<DataMap> bySocialRoutes() {
        return dataMap -> dataMap.getTrivalentLogicType().contains("SOCIAL") &&
                dataMap.getToTrivalentLogicType().contains("SOCIAL");
    }

    public static Predicate<DataMap> byOuterRoutes() {
        return dataMap -> dataMap.getTrivalentLogicType().contains("OUTER") &&
                dataMap.getToTrivalentLogicType().contains("OUTER");
    }

    public static Predicate<DataMap> byInnerRoutes() {
        return dataMap -> dataMap.getTrivalentLogicType().contains("INNER") &&
                dataMap.getToTrivalentLogicType().contains("INNER");
    }

    public static Predicate<DataMap> byAlternateTrivalentRoutes() {
        return dataMap -> !dataMap.getTrivalentLogicType().equalsIgnoreCase(dataMap.getToTrivalentLogicType());
    }

    public static Predicate<DataMap> bySemanticMapPoint(Point address) {
        return dataMap -> {
            Point fromAddressRoute = dataMap.getAtAddressCoordinates();
            Point toAddressRoute = dataMap.getToAddressCoordinates();
            return GeometryUtil.isOnSegment(fromAddressRoute, toAddressRoute, address) ; //&& tp >= ph && tp >= th;
            } ;
    }

    public static Predicate<DataMap> byInGrid(List<DataMap> grid) {
        return dataMap -> {
            Point address = dataMap.getAtAddressCoordinates();
            return grid
                    .stream()
                    .filter(DataMapFilterUtil.byAlternateTrivalentRoutes())
                    .filter(bySemanticMapPoint(address)).count() > 0;
        };
    }

    public static Predicate<DataMap> byNotInSemanticMap(List<DataMap> grid) {
        return dataMap -> {
            Point address = dataMap.getAtAddressCoordinates();
            return grid
                    .stream()
                    .filter(DataMapFilterUtil.byAlternateTrivalentRoutes())
                    .filter(bySemanticMapPoint(address)).count() == 0;
        };
    }

    public static Predicate<? super DataMap> byNetworkName(String network) {
        return dataMap -> dataMap.getNetwork().equalsIgnoreCase(network);
    }

	public static Predicate<? super DataMap> byClusterIndex(Long clusterIndex) {
		return dataMap -> dataMap.getClusterIndex().compareTo(clusterIndex) == 0;
	}

}
