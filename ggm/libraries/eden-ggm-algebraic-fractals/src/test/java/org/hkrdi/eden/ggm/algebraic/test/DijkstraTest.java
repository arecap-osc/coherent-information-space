//package org.hkrdi.eden.ggm.algebraic.test;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
//import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
//import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.NetworkBuilder;
//import org.hkrdi.eden.ggm.algebraic.netting.factory.NetworkFactory;
//import org.hkrdi.eden.ggm.algebraic.routes.util.DijkstraRoute;
//import org.junit.Test;
//
//
//public class DijkstraTest {
//
//    @Test
//    public void constructHexavalentNetwork() {
//        
//        NetworkFactory nf = new NetworkFactory() {};
//        NetworkBuilder networkBuilder = nf.getNetworkBuilder(NettingType.METABOLIC_EDGES, 100_000_000, 1, 1,1);
//		List<HexavalentLogic> hexavalentLogic = nf.getHexavalentLogic(networkBuilder);
//		
//		List<Integer> result = new DijkstraRoute().getShortestPath(hexavalentLogic, 
//				new HashSet<Integer>(Arrays.asList(new Integer[] {})), 
//				new HashSet<Integer>(Arrays.asList(new Integer[] {})),
//				new HashSet<Integer>(Arrays.asList(new Integer[] {})),//{171, 159})),
//				2, 1);
//		
////		System.out.println(result);
//		System.out.println(result.stream().map(hl->hl+"").collect(Collectors.joining(",")));
//    }
//}
