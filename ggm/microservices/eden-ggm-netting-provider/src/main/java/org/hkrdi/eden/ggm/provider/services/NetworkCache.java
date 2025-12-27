package org.hkrdi.eden.ggm.provider.services;


import org.aopalliance.intercept.MethodInvocation;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.algebraic.netting.Network;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//@ContextOriented
//@SourceClass(NetworkService.class)
public class NetworkCache {

    private Map<String, List<HexavalentLogic>> networks = new ConcurrentHashMap<>();

//    @Wrap
//    @SourceName("getHexavalentLogic")
    public List<HexavalentLogic> getCachedNetwork(MethodInvocation hexavalentLogicFactory) {
        return Optional.ofNullable(networks.get(getNetworkName(hexavalentLogicFactory)))
                .orElseGet(() -> proceedCache(hexavalentLogicFactory));
    }

    protected String getNetworkName(MethodInvocation hexavalentLogicFactory) {
        switch (hexavalentLogicFactory.getArguments().length) {
            case 1 :
                Network network = (Network) hexavalentLogicFactory.getArguments()[0];
                return getNetworkName(network.getNettingType(), network.getDeep());
            case 3 :
                return getNetworkName((NettingType) hexavalentLogicFactory.getArguments()[0],
                        (int) hexavalentLogicFactory.getArguments()[2]);
        }
        return "";
    }

    protected String getNetworkName(NettingType nettingType, int deep) {
        return nettingType + "::" + deep;
    }

    protected List<HexavalentLogic> proceedCache(MethodInvocation hexavalentLogicFactory)  {
        try {
            List<HexavalentLogic> network = (List<HexavalentLogic>) hexavalentLogicFactory.proceed();
            networks.put(getNetworkName(hexavalentLogicFactory), network);
            return network;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
