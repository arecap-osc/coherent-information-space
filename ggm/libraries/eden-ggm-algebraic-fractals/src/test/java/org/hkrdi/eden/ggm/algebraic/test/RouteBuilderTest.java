//package org.hkrdi.eden.ggm.algebraic.test;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeSet;
//import java.util.stream.Collectors;
//
//import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
//import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
//import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.NetworkBuilder;
//import org.hkrdi.eden.ggm.algebraic.netting.factory.NetworkFactory;
//import org.hkrdi.eden.ggm.algebraic.routes.HexavalentLogicWrapper;
//import org.hkrdi.eden.ggm.algebraic.routes.RouteBuilder;
//import org.hkrdi.eden.ggm.algebraic.routes.util.HexavalentLogicUtil;
//import org.hkrdi.eden.ggm.algebraic.routes.util.RoutesInsideHexavalentLogic;
//import org.junit.Test;
//
//
//public class RouteBuilderTest {
//
////    @Test
//    public void testRouteSearchBetweenNodes() {
//        
//        NetworkFactory nf = new NetworkFactory() {};
//        NetworkBuilder networkBuilder = nf.getNetworkBuilder(NettingType.SUSTAINABLE_EDGES, 100_000_000, 1, 1,1);
//		List<HexavalentLogic> hexavalentLogic = nf.getHexavalentLogic(networkBuilder);
//		
//		Map<Integer, List<HexavalentLogic>> hexavalentLogicFullSpaceAsMapByNodeIndex = HexavalentLogicUtil.getHexavalentLogicFullSpaceAsMapByNodeIndex(hexavalentLogic);
//		
//		hexavalentLogic = hexavalentLogic.stream().map(hl->new HexavalentLogicWrapper(hl)).collect(Collectors.toList());
//		
//		Map<String, List<List<Integer>>>  allPossibleRoutesUnmappedOdd = RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogic.stream().filter(hl->hl.getIndex() == 0).findFirst().get());
//		Map<String, List<List<Integer>>>  allPossibleRoutesUnmappedEven = RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogic.stream().filter(hl->hl.getIndex() == 1).findFirst().get());
//		
//		RouteBuilder routeBuilder = new RouteBuilder(hexavalentLogic, hexavalentLogicFullSpaceAsMapByNodeIndex, allPossibleRoutesUnmappedOdd, allPossibleRoutesUnmappedEven);
//		//1,5
//		List<List<Integer>> result = routeBuilder.computeHexavalentLogicRoute(1, 5, 
//				new HashSet<Integer>(Arrays.asList(new Integer[] {})), 
//				new HashSet<Integer>(Arrays.asList(new Integer[] {})),
//				new HashSet<Integer>(Arrays.asList(new Integer[] {}))//{171, 159})),);
//		);
//		System.out.println(result.size());
////		System.out.println(result.stream().map(list->list.stream().map(i->i+"").collect(Collectors.joining(","))).collect(Collectors.joining("\n")));
//    }
//
//    @Test
//    public void testRouteSearchForAllNodes() {
//        NetworkFactory nf = new NetworkFactory() {};
//        NetworkBuilder networkBuilder = nf.getNetworkBuilder(NettingType.SUSTAINABLE_EDGES, 100_000_000, 1, 1,1);
//		List<HexavalentLogic> hexavalentLogic = nf.getHexavalentLogic(networkBuilder);
//		
//		hexavalentLogic = hexavalentLogic.stream().map(hl->new HexavalentLogicWrapper(hl)).collect(Collectors.toList());
//		
//		Set<Integer> nodes = new TreeSet<>();
//		hexavalentLogic.stream().forEach(hl->nodes.addAll(hl.getVertices().stream().map(v->v.getIndex()).collect(Collectors.toSet())));
//		
////		Map<String, List<List<Integer>>>  allPossibleRoutesUnmappedOdd = RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogic.stream().filter(hl->hl.getIndex() == 0).findFirst().get());
////		Map<String, List<List<Integer>>>  allPossibleRoutesUnmappedEven = RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogic.stream().filter(hl->hl.getIndex() == 1).findFirst().get());
//		
//		
////		int combinationsNo = (Math.log(1000) / Math.log(route.size())) +1;
//		RouteBuilder routeBuilder = new RouteBuilder(hexavalentLogic, HexavalentLogicUtil.getHexavalentLogicFullSpaceAsMapByNodeIndex(hexavalentLogic), 
//				RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogic.stream().filter(hl->hl.getIndex() == 0).findFirst().get()), 
//				RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogic.stream().filter(hl->hl.getIndex() == 1).findFirst().get()));
//		for (Integer startNode:nodes) {
//			for (Integer endNode: nodes) {
//				if (startNode == endNode) {
//					continue;
//				}
//				List<List<Integer>> result = routeBuilder.computeHexavalentLogicRoute(startNode, endNode, 
//						new HashSet<Integer>(Arrays.asList(new Integer[] {})), 
//						new HashSet<Integer>(Arrays.asList(new Integer[] {})),
//						new HashSet<Integer>(Arrays.asList(new Integer[] {}))//{171, 159})),);
//				);
//				if (result.size() == 1) {
//					System.err.print("[1:::"+startNode+"->"+endNode+"]=");
//					System.err.println(result.stream().map(list->list.stream().map(i->i+"").collect(Collectors.joining(","))).collect(Collectors.joining("\n")));
//				}else if (result.size() >1000) {
//					System.out.println("[1000:::"+startNode+"->"+endNode+"]="+result.size());
//				}else if (result.size() <100) {
////					System.err.println("[100:::"+startNode+"->"+endNode+"]="+result.size());
//				}else{
////					System.out.println("["+startNode+"->"+endNode+"]="+result.size());
////					System.out.println(result.size());
//				}
//			}
//		}
//		
////		System.out.println(result.stream().map(list->list.stream().map(i->i+"").collect(Collectors.joining(","))).collect(Collectors.joining("\n")));
//    }
//}
