package org.hkrdi.eden.ggm.provider.conversion;

import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.hkrdi.eden.ggm.repository.entity.DataMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DataMapConversionUtil {

    public static List<DataMap> getNetworkDataMap(List<HexavalentLogic> hexavalentLogicNetwork, String network, Integer deep, Long resolution) {
        List<DataMap> hexavalentLogicDataMap = new ArrayList<>(hexavalentLogicNetwork.size()*42);
        hexavalentLogicNetwork.stream()
                .map(hexavalentLogic -> getConnectorsDataMap(hexavalentLogic, network, deep, resolution))
                .forEach(connectorsDataMap -> hexavalentLogicDataMap.addAll(connectorsDataMap));
        return hexavalentLogicDataMap;
    }

    public static List<DataMap> getConnectorsDataMap(HexavalentLogic hexavalentLogic, String network, Integer deep, Long resolution) {
        return hexavalentLogic.getConnectors().stream()
                .map(c -> constructDataMap(hexavalentLogic, c.getHead(), c.getTail(), network, deep, resolution))
                .collect(Collectors.toList());
    }


    public static DataMap constructDataMap(HexavalentLogic hexavalentLogic, Vertex at, Vertex to, String network, Integer deep, Long resolution) {
        DataMap dataMap = new DataMap();
        dataMap.setNetwork(network);
        dataMap.setClusterIndex(new Long(hexavalentLogic.getIndex()));
        dataMap.setBeltIndex(hexavalentLogic.getBeltIndex());
        dataMap.setRowIndex(hexavalentLogic.getRow());
        dataMap.setColumnIndex(hexavalentLogic.getColumn());
        dataMap.setClusterType(hexavalentLogic.getClusterType().name());
        dataMap.setX(DataMap.toLongDoubleRoundAddress(hexavalentLogic.getCenter().getX()));
        dataMap.setY(DataMap.toLongDoubleRoundAddress(hexavalentLogic.getCenter().getY()));
        dataMap.setResolution(resolution);
        dataMap.setAtX(DataMap.toLongDoubleRoundAddress(at.getPoint().getX()));
        dataMap.setAtY(DataMap.toLongDoubleRoundAddress(at.getPoint().getY()));
        dataMap.setToX(DataMap.toLongDoubleRoundAddress(to.getPoint().getX()));
        dataMap.setToY(DataMap.toLongDoubleRoundAddress(to.getPoint().getY()));
        dataMap.setAddressIndex(new Long(at.getIndex()));
        dataMap.setToAddressIndex(new Long(to.getIndex()));
        dataMap.setTrivalentLogic(at.getType().name());
        dataMap.setTrivalentLogicType(getTrivalentLogicType(hexavalentLogic, at));
        dataMap.setToTrivalentLogic(to.getType().name());
        dataMap.setToTrivalentLogicType(getTrivalentLogicType(hexavalentLogic, to));
        return dataMap;
    }


    public static String getTrivalentLogicType(HexavalentLogic hexavalentLogic, Vertex vertex) {
        if (hexavalentLogic.getCognitiveOuter().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
            return hexavalentLogic.getCognitiveOuter().getType().name();
        }
        if (hexavalentLogic.getSocialOuter().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
            return hexavalentLogic.getSocialOuter().getType().name();
        }
        if (hexavalentLogic.getCognitiveInner().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
            return hexavalentLogic.getCognitiveInner().getType().name();
        }
        if (hexavalentLogic.getSocialInner().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
            return hexavalentLogic.getSocialInner().getType().name();
        }
        return "";
    }

}
