package org.hkrdi.eden.ggm.algebraic.netting.factory;

import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.algebraic.netting.Network;
import org.hkrdi.eden.ggm.algebraic.netting.compute.GridPartition;
import org.hkrdi.eden.ggm.algebraic.netting.compute.MetabolicSpace;
import org.hkrdi.eden.ggm.algebraic.netting.compute.NetworkPartition;
import org.hkrdi.eden.ggm.algebraic.netting.compute.RectangularPartition;
import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.*;
import org.springframework.data.geo.Point;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;


public interface NetworkFactory {

    default NetworkBuilder getNetworkBuilder(NettingType nettingType) {
        switch (nettingType){
            case SUSTAINABLE_EDGES:
                return new SustainableEdgesNetworkBuilder();
            case SUSTAINABLE_VERTICES:
                return new SustainableVerticesNetworkBuilder();
            case METABOLIC_VERTICES:
                return new MetabolicVerticesNetworkBuilder();
            case METABOLIC_EDGES:
                return new MetabolicEdgesNetworkBuilder();
            default:
                throw new IllegalArgumentException();
        }
    }

    default NetworkBuilder getNetworkBuilder(NetworkPartition partition, NettingType nettingType, double spaceDistance, int deep) {
        ConfigurableNetworkBuilder builder = (ConfigurableNetworkBuilder) getNetworkBuilder(nettingType);
        builder.setSpaceDistance(spaceDistance);
        builder.setSpaceCenter(partition.getPartitionCenter());
        builder.setDeep(deep);
        builder.setCenter(partition.getPartitionCenter());
//        builder.setPartitionCenter(partition.getPartitionCenter());
//        builder.setPartitionTopLeft(partition.getPartitionTopLeft());
        return builder;
    }

    //for deep 0 si 1
    default NetworkBuilder getNetworkBuilder(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        return getNetworkBuilder(partition, nettingType, spaceDistance, deep);
    }

    default NetworkBuilder getNetworkBuilderStarCentral(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        NetworkBuilder network = getNetworkBuilder(partition, nettingType, spaceDistance, deep);
        if(MetabolicSpace.class.isAssignableFrom(network.getClass())) {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(0,
                            network.getHeightBigDecimal().doubleValue()));
        } else {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(0D,
                            network.getHeightBigDecimal().divide(new BigDecimal(2)).doubleValue()));
        }
        return network;
    }

    default NetworkBuilder getNetworkBuilderStarSouthCentral(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        NetworkBuilder network = getNetworkBuilder(partition, nettingType, spaceDistance, deep);
        if(MetabolicSpace.class.isAssignableFrom(network.getClass())) {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(0,
                            network.getHeightBigDecimal().multiply(new BigDecimal(2)).doubleValue()));
        } else {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(0D,
                            network.getHeightBigDecimal().doubleValue()));
        }
        return network;
    }

    //for deep 2
    default NetworkBuilder getNetworkBuilderStarNorthWest(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        NetworkBuilder network = getNetworkBuilder(partition, nettingType, spaceDistance, deep);
        if(MetabolicSpace.class.isAssignableFrom(network.getClass())) {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().multiply(new BigDecimal(-3)).divide(new BigDecimal(4)).doubleValue(),
                            network.getHeightBigDecimal().divide(new BigDecimal(2)).doubleValue()));
        } else {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().divide(new BigDecimal(-2)).doubleValue(),
                            network.getHeightBigDecimal().divide(new BigDecimal(4)).doubleValue()));
        }
        return network;
    }

    default NetworkBuilder getNetworkBuilderStarNorthEst(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        NetworkBuilder network = getNetworkBuilder(partition, nettingType, spaceDistance, deep);
        if(MetabolicSpace.class.isAssignableFrom(network.getClass())) {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(4)).doubleValue(),
                            network.getHeightBigDecimal().divide(new BigDecimal(2)).doubleValue()));
        } else {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().divide(new BigDecimal(2)).doubleValue(),
                            network.getHeightBigDecimal().divide(new BigDecimal(4)).doubleValue()));
        }
        return network;
    }

    default NetworkBuilder getNetworkBuilderStarSouthWest(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        NetworkBuilder network = getNetworkBuilder(partition, nettingType, spaceDistance, deep);
        if(MetabolicSpace.class.isAssignableFrom(network.getClass())) {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().multiply(new BigDecimal(-3)).divide(new BigDecimal(4)).doubleValue(),
                            network.getHeightBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(2)).doubleValue()));
        } else {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().divide(new BigDecimal(-2)).doubleValue(),
                            network.getHeightBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(4)).doubleValue()));
        }
        return network;
    }

    default NetworkBuilder getNetworkBuilderStarSouthEst(NettingType nettingType, double spaceDistance, int deep, int domain, int limit) {
        RectangularPartition partition = GridPartition.getRectangularPartition(spaceDistance, domain, limit);
        NetworkBuilder network = getNetworkBuilder(partition, nettingType, spaceDistance, deep);
        if(MetabolicSpace.class.isAssignableFrom(network.getClass())) {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(4)).doubleValue(),
                            network.getHeightBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(2)).doubleValue()));
        } else {
            ((ConfigurableNetworkBuilder)network)
                    .setSpaceCenter(new Point(network.getWidthBigDecimal().divide(new BigDecimal(2)).doubleValue(),
                            network.getHeightBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(4)).doubleValue()));
        }
        return network;
    }

    default List<HexavalentLogic> getHexavalentLogic(NetworkBuilder networkBuilder) {
        Assert.isAssignable(Network.class, networkBuilder.getClass());
        networkBuilder.build();
        return ((Network)networkBuilder).getHexavalentNetting();
    }

    default List<HexavalentLogic> getHexavalentLogic(NettingType nettingType, double spaceDistance, int deep) {
        ConfigurableNetworkBuilder builder = (ConfigurableNetworkBuilder) getNetworkBuilder(nettingType);
        builder.setSpaceDistance(spaceDistance);
        builder.setDeep(deep);
        builder.setCenter(new Point(0 , 0));
        builder.setSpaceCenter(new Point(0 , 0));
        builder.build();
        return builder.getHexavalentNetting();
    }

}
