//package org.hkrdi.eden.ggm.algebraic.test;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
//import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
//import org.hkrdi.eden.ggm.algebraic.netting.compute.builder.NetworkBuilder;
//import org.hkrdi.eden.ggm.algebraic.netting.factory.NetworkFactory;
//import org.hkrdi.eden.ggm.algebraic.routes.RouteBuilder;
//import org.junit.Test;
//
//
//public class AllPossibleRoutesTest {
//
//    @Test
//    public void constructHexavalentNetwork() {
//        
//        NetworkFactory nf = new NetworkFactory() {};
//        NetworkBuilder networkBuilder = nf.getNetworkBuilder(NettingType.SUSTAINABLE_VERTICES, 100_000_000, 1, 1,1);
//		List<HexavalentLogic> hexavalentLogic = nf.getHexavalentLogic(networkBuilder);
//		HexavalentLogic  centralHl = hexavalentLogic.stream().filter(hl->hl.getIndex() == 0).findFirst().get();
//		Map<String, List<List<Integer>>> result = RouteBuilder.
//				getAllPossibleRoutes(centralHl);
//		
//		result.keySet().stream().sorted().forEach(key->showRoutesFor(key, result));
////		showRoutesFor("4_2", result);
////		System.out.println("Longest="+result.get("4_2").stream().map(l->l.size()).max(Comparator.naturalOrder()));
////		System.out.println("Shortest="+result.get("4_2").stream().map(l->l.size()).min(Comparator.naturalOrder()));
//		
//		System.out.println(
//				"Total="+result.values().stream().map(
//						l->l.stream().map(ll->ll.size()
//						).reduce(Integer::sum)).map(oi->oi.orElse(0)).reduce(Integer::sum)
//				);
//		
//		System.out.println("NO routes for:" + result.keySet().stream().filter(key->result.get(key).size()==0).collect(Collectors.joining(",")));
//		
//		List<String> keys = Arrays.asList(new String[] {"1_2","1_3","6_2","6_3"});
//		System.out.println(
//				"Total[{1,6}->{2,3}]="+result.keySet().stream().filter(s->keys.contains(s)).map(key->result.get(key)).map(
//						l->l.stream().map(ll->ll.size()
//						).reduce(Integer::sum)).map(oi->oi.get()).reduce(Integer::sum)
//				);
//		
//		
////		result.keySet().stream().sorted().forEach(key->{
////			System.out.print("Key: "+key+": {");
////			System.out.print(
////			result.get(key).stream().count());
////			System.out.println("}");
////		});
//		
//		Map<String, List<Integer>> shortestRoutes = RouteBuilder.shortestRoute(result, centralHl);
//		shortestRoutes.keySet().stream().forEach(key->showShortestRoutesFor(key, shortestRoutes));
//    }
//    
//    private static void showRoutesFor(String key, Map<String, List<List<Integer>>> result) {
//		System.out.println("Key: "+key+": {");
//		System.out.println(
//		result.get(key).stream().map(
//				l->l.stream().map(ll->ll+"").collect(Collectors.joining(","))
//				).collect(Collectors.joining("\n"))
//		);
//		System.out.println("}");
//    }
//
//    private static void showShortestRoutesFor(String key, Map<String, List<Integer>> result) {
//		System.out.print("Shortest route for key: "+key+": {");
//		System.out.print(
//				result.get(key).stream().map(i->i+"").collect(Collectors.joining(","))
//		);
//		System.out.println("}");
//    }
//
//}
