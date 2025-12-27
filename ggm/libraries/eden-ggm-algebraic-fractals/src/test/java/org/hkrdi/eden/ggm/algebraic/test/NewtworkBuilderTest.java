package org.hkrdi.eden.ggm.algebraic.test;

import org.hkrdi.eden.ggm.algebraic.Connector;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.NetworkBuilder;
import org.hkrdi.eden.ggm.algebraic.netting.compute.path.FindPath;
import org.hkrdi.eden.ggm.algebraic.netting.factory.NetworkFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class NewtworkBuilderTest {

    @Test
    public void constructHexavalentNetwork() {
        
        NetworkFactory nf = new NetworkFactory() {};
        NetworkBuilder networkBuilder = nf.getNetworkBuilder(NettingType.SUSTAINABLE_EDGES, 300, 1, 1,1);
		List<HexavalentLogic> hexavalentLogic = nf.getHexavalentLogic(networkBuilder);
		
		List<Connector> allConnectors = new ArrayList<>();
		hexavalentLogic.stream().forEach(m->allConnectors.addAll(m.getConnectors()));
		List<List<Integer>> result = new FindPath<Integer>().findPath(1, 2, allConnectors);
		
		for(List<Integer> s:result) {
			for (int i=1;i<s.size();i++) {
				final int index = i;
				if (allConnectors.stream().filter(c->c.getHead().getIndex()==s.get(index-1)&&c.getTail().getIndex()==s.get(index)).count()==0) {
					System.out.println("wrong");
				}
			}
		}
		
//		System.out.println(result);
		System.out.println(result.size());
    }
}
