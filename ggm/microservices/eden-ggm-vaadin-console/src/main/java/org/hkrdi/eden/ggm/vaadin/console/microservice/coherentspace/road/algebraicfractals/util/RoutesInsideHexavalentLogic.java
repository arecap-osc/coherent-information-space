package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;

import org.hkrdi.eden.ggm.algebraic.ClusterType;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class RoutesInsideHexavalentLogic {

	//this is it
	public static Map<String, List<List<Integer>>> getAllPossibleRoutes(HexavalentLogic hl){
		long start = System.currentTimeMillis();
		Map<String, List<List<Integer>>> graph = new HashMap<>();
		for (Vertex source :hl.getVertices()) {
			for (Vertex dest :hl.getVertices()) {
				if (source.getIndex() != dest.getIndex()) {
					Stack<Integer> route = new Stack<Integer>();
					route.push(source.getIndex());
					graph.put(source.getIndex()+"_"+dest.getIndex(), new ArrayList<List<Integer>>());
					findRoute(dest.getIndex(), route, graph, hl);
				}
			}
		}
		System.out.println("getAllPossibleRoutes:"+(System.currentTimeMillis()-start));
		return graph;
	}
	
	public static Map<String, List<List<Integer>>> getAllPossibleRoutesMapped(HexavalentLogic hl, 
			List<HexavalentLogic> hexavalentLogicFullSpace,
			Map<String, List<List<Integer>>> allPossibleRoutesUnmappedOdd,
			Map<String, List<List<Integer>>> allPossibleRoutesUnmappedEven, int oddClusterIndex, int evenClusterIndex) {
		Map<String, List<List<Integer>>>  allPossibleRoutesUnmapped = allPossibleRoutesUnmappedOdd;
		List<Vertex> orderedVertices = hexavalentLogicFullSpace.stream().filter(hll->hll.getIndex() == oddClusterIndex).findFirst().get().getVertices(); 
		if (hl.getClusterType() == ClusterType.EVEN) {
			allPossibleRoutesUnmapped = allPossibleRoutesUnmappedEven;
			orderedVertices = hexavalentLogicFullSpace.stream().filter(hll->hll.getIndex() == evenClusterIndex).findFirst().get().getVertices();
		}
		
		Map<Integer, Integer> nodesMappingOnOld = new HashMap<>();
		for (int i=0; i < orderedVertices.size(); i++) {
			nodesMappingOnOld.put(orderedVertices.get(i).getIndex(), hl.getVertices().get(i).getIndex());
		}
		
		Map<String, List<List<Integer>>>  allPossibleRoutesMapped = new HashMap<>();
		for (String key:allPossibleRoutesUnmapped.keySet()){
			List<List<Integer>> nodesUnmapped = allPossibleRoutesUnmapped.get(key);
			String newKey = nodesMappingOnOld.get(new Integer(key.split("_")[0]))+"_"+nodesMappingOnOld.get(new Integer(key.split("_")[1]));
			allPossibleRoutesMapped.put(newKey, nodesUnmapped.stream().map(ol-> ol.stream().map(oll->nodesMappingOnOld.get(oll)).collect(Collectors.toList())).collect(Collectors.toList()));
		};
		
//		Map<String, List<List<Integer>>>  allPossibleRoutesTested =  getAllPossibleRoutes(hl);
//		checkAllPossibleRoutes(allPossibleRoutesTested, allPossibleRoutesMapped);
//		return allPossibleRoutesTested;
		return allPossibleRoutesMapped;
	}
	
	private void checkAllPossibleRoutes(Map<String, List<List<Integer>>>  allPossibleRoutesTested, Map<String, List<List<Integer>>>  allPossibleRoutesMapped) {
		if (allPossibleRoutesTested.keySet().containsAll(allPossibleRoutesMapped.keySet())) {
			allPossibleRoutesTested.keySet().forEach(key->{
				if (!allPossibleRoutesTested.get(key).containsAll(allPossibleRoutesMapped.get(key))) {
					System.err.println("It's not good!");
				}
			});
		}else {
			System.out.println("Nope!");
		}
	}

	
	//TODO increase speed!!!!!
	private double getRouteLength(List<Integer> list, HexavalentLogic hl) {
		if (list.size() < 2) {
			return 0;
		}
		double length = 0;
		for (int index = 0 ; index < list.size() -1; index++) {
			int indexF = index;
			length += GeometricRoute.segmentLength(
					hl.getVertices().stream().filter(v->v.getIndex()==list.get(indexF)).findFirst().get().getPoint(),
					hl.getVertices().stream().filter(v->v.getIndex()==list.get(indexF+1)).findFirst().get().getPoint()
					);
		}
		return length;
	}

	//gets the shortest route
	public Map<String, List<Integer>> shortestRoute(Map<String, List<List<Integer>>> routes, HexavalentLogic hl){
		return routes.keySet().stream().collect(Collectors.toMap(key->key, key->{
			List<Integer> proposedForLength =  routes.get(key).stream().min(
					(o1,o2)->  (int)(getRouteLength(o1, hl) - getRouteLength(o2, hl)
							)).orElse(new ArrayList<>());
//			List<Integer> proposedForCount =   routes.get(key).stream().min((o1,o2)-> (int)(o1.size()-o2.size())).orElse(new ArrayList<>());
//			if (proposedForCount.size() != proposedForLength.size()){
//				System.out.println("strange");
//			}
			return proposedForLength;
		}));
	}
	
	private static void findRoute(int destIndex, Stack<Integer> route, Map<String, List<List<Integer>>>  graph, HexavalentLogic hl) {
//		System.err.println("show:"+Arrays.asList(route.toArray(new Integer[0])).stream().map(l->l+"").collect(Collectors.joining(",")));
		if (route.peek() == destIndex) {
			graph.get(route.get(0)+"_"+destIndex).add(Arrays.asList(route.toArray(new Integer[0])));
//			System.out.println("----------------Found:"+route.get(0)+"_"+destIndex+":"+Arrays.asList(route.toArray(new Integer[0])).stream().map(l->l+"").collect(Collectors.joining(",")));
			route.pop();
			return;
		}
		hl.getConnectors().stream().filter(connector->connector.getHead().getIndex() == route.peek()).
			filter(connector-> !route.contains(connector.getTail().getIndex())).
			forEach(connector-> {
				route.push(connector.getTail().getIndex());
//				System.out.println("try:"+Arrays.asList(route.toArray(new Integer[0])).stream().map(l->l+"").collect(Collectors.joining(",")));
				findRoute(destIndex, route, graph, hl);
			});
		route.pop();
	}
}
