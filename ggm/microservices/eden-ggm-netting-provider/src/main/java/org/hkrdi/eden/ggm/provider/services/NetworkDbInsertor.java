package org.hkrdi.eden.ggm.provider.services;


import org.aopalliance.intercept.MethodInvocation;
import org.contextualj.lang.annotation.expression.SourceClass;
import org.contextualj.lang.annotation.expression.SourceName;
import org.contextualj.lang.annotation.pointcut.Wrap;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.algebraic.netting.Network;
import org.hkrdi.eden.ggm.provider.conversion.DataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cop.annotation.ContextOriented;

import java.util.List;

@ContextOriented
@SourceClass(NetworkService.class)
public class NetworkDbInsertor {

    String[] nettingStart = {"Central", "SouthCentral", "NorthWest", "SouthWest", "NorthEst", "SouthEst"};

    int state = 0;

	@Autowired
	private DataMapService dataMapService;
	
    @Wrap
    @SourceName("getHexavalentLogic")
    public List<HexavalentLogic> getCachedNetwork(MethodInvocation hexavalentLogicFactory) {
        return proceedInsert(hexavalentLogicFactory);
    }

    protected List<HexavalentLogic> proceedInsert(MethodInvocation hexavalentLogicFactory)  {
        try {
            List<HexavalentLogic> network = (List<HexavalentLogic>) hexavalentLogicFactory.proceed();
            if(hexavalentLogicFactory.getArguments().length == 1) {
                Network networkBuilder = (Network) hexavalentLogicFactory.getArguments()[0];

                dataMapService.convert(networkBuilder.getNettingType().name(),
                        networkBuilder.getDeep(), network, new Long(Math.round((double) networkBuilder.getSpaceDistance())));
                if(state == nettingStart.length) {
                    state = 0;
                }
            } else {
                dataMapService.convert(((NettingType) hexavalentLogicFactory.getArguments()[0]).name(), (int) hexavalentLogicFactory.getArguments()[2], network, new Long(Math.round((double) hexavalentLogicFactory.getArguments()[1])));
            }
            return network;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
