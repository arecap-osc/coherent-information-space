package org.hkrdi.eden.ggm.provider.services;

import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.NetworkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NetworkBuilderService {

    @Value("${hexagonal.trivalent.dimension}")
    private double spaceDistance;

    @Autowired
    private NetworkService networkService;

    public void buildSomething(NettingType nettingType, int deep) {
    	networkService.getHexavalentLogic(nettingType, spaceDistance, deep);
//        NetworkBuilder networkBuilder = networkService.getNetworkBuilderStarNorthWest(nettingType,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilder);
    }
    
    public void buildCacheDeep(int deep) {
        if(deep < 2) {
            networkService.getHexavalentLogic(NettingType.SUSTAINABLE_EDGES, spaceDistance, deep);
            networkService.getHexavalentLogic(NettingType.SUSTAINABLE_VERTICES, spaceDistance, deep);
            networkService.getHexavalentLogic(NettingType.METABOLIC_VERTICES, spaceDistance, deep);
            networkService.getHexavalentLogic(NettingType.METABOLIC_EDGES, spaceDistance, deep);
        } else if (deep == 2) {
            NetworkBuilder networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.SUSTAINABLE_EDGES,
                    spaceDistance, deep, 1, 1);
            networkService.getHexavalentLogic(networkBuilderStarNorthWest);
            networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.SUSTAINABLE_VERTICES,
                    spaceDistance, deep, 1, 1);
            networkService.getHexavalentLogic(networkBuilderStarNorthWest);
            networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.METABOLIC_VERTICES,
                    spaceDistance, deep, 1, 1);
            networkService.getHexavalentLogic(networkBuilderStarNorthWest);
            networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.METABOLIC_EDGES,
                    spaceDistance, deep, 1, 1);
            networkService.getHexavalentLogic(networkBuilderStarNorthWest);
        }

//        NetworkBuilder networkBuilderStarCentral = networkService.getNetworkBuilderStarCentral(NettingType.SUSTAINABLE_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarCentral);
//        NetworkBuilder networkBuilderStarSouthCentral = networkService.getNetworkBuilderStarSouthCentral(NettingType.SUSTAINABLE_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthCentral);
//        NetworkBuilder networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.SUSTAINABLE_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthWest);
//        NetworkBuilder networkBuilderStarSouthWest = networkService.getNetworkBuilderStarSouthWest(NettingType.SUSTAINABLE_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthWest);
//        NetworkBuilder networkBuilderStarNorthEst = networkService.getNetworkBuilderStarNorthEst(NettingType.SUSTAINABLE_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthEst);
//        NetworkBuilder networkBuilderStarSouthEst = networkService.getNetworkBuilderStarSouthEst(NettingType.SUSTAINABLE_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthEst);
//
//
//        networkService.getHexavalentLogic(NettingType.SUSTAINABLE_VERTICES, spaceDistance, deep);
//        networkBuilderStarCentral = networkService.getNetworkBuilderStarCentral(NettingType.SUSTAINABLE_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarCentral);
//        networkBuilderStarSouthCentral = networkService.getNetworkBuilderStarSouthCentral(NettingType.SUSTAINABLE_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthCentral);
//        networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.SUSTAINABLE_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthWest);
//        networkBuilderStarSouthWest = networkService.getNetworkBuilderStarSouthWest(NettingType.SUSTAINABLE_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthWest);
//        networkBuilderStarNorthEst = networkService.getNetworkBuilderStarNorthEst(NettingType.SUSTAINABLE_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthEst);
//        networkBuilderStarSouthEst = networkService.getNetworkBuilderStarSouthEst(NettingType.SUSTAINABLE_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthEst);
//
//        networkService.getHexavalentLogic(NettingType.METABOLIC_EDGES, spaceDistance, deep);
//        networkBuilderStarCentral = networkService.getNetworkBuilderStarCentral(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarCentral);
//        networkBuilderStarSouthCentral = networkService.getNetworkBuilderStarSouthCentral(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthCentral);
//        networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthWest);
//        networkBuilderStarSouthWest = networkService.getNetworkBuilderStarSouthWest(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthWest);
//        networkBuilderStarNorthEst = networkService.getNetworkBuilderStarNorthEst(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthEst);
//        networkBuilderStarSouthEst = networkService.getNetworkBuilderStarSouthEst(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthEst);
//
//        networkService.getHexavalentLogic(NettingType.METABOLIC_VERTICES, spaceDistance, deep);
//        networkBuilderStarCentral = networkService.getNetworkBuilderStarCentral(NettingType.METABOLIC_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarCentral);
//        networkBuilderStarSouthCentral = networkService.getNetworkBuilderStarSouthCentral(NettingType.METABOLIC_EDGES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthCentral);
//        networkBuilderStarNorthWest = networkService.getNetworkBuilderStarNorthWest(NettingType.METABOLIC_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthWest);
//        networkBuilderStarSouthWest = networkService.getNetworkBuilderStarSouthWest(NettingType.METABOLIC_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthWest);
//        networkBuilderStarNorthEst = networkService.getNetworkBuilderStarNorthEst(NettingType.METABOLIC_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarNorthEst);
//        networkBuilderStarSouthEst = networkService.getNetworkBuilderStarSouthEst(NettingType.METABOLIC_VERTICES,
//                spaceDistance, deep, 1, 1);
//        networkService.getHexavalentLogic(networkBuilderStarSouthEst);
    }

}

