package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals;

import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util.*;
import org.springframework.data.geo.Point;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class RouteBuilder{

	private List<HexavalentLogic> hexavalentLogicFullSpace; 
	private Map<Integer, List<HexavalentLogic>> hexavalentLogicFullSpaceAsMapByNodeIndex; 
	private Map<String, List<List<Integer>>>  allPossibleRoutesUnmappedOdd;
	private Map<String, List<List<Integer>>>  allPossibleRoutesUnmappedEven;
	private int combinationsNo = 20000;
	private int combinationsNoToBeReturned = 1000;
	
	private int oddClusterIndex = 0;
	private int evenClusterIndex = 1;
	private String networkName;
	
	
	public RouteBuilder(List<HexavalentLogic> hexavalentLogicFullSpace,
			Map<Integer, List<HexavalentLogic>> hexavalentLogicFullSpaceAsMapByNodeIndex,
			Map<String, List<List<Integer>>> allPossibleRoutesUnmappedOdd,
			Map<String, List<List<Integer>>> allPossibleRoutesUnmappedEven) {
		this.hexavalentLogicFullSpace = hexavalentLogicFullSpace;
		this.hexavalentLogicFullSpaceAsMapByNodeIndex = hexavalentLogicFullSpaceAsMapByNodeIndex;
		this.allPossibleRoutesUnmappedOdd = allPossibleRoutesUnmappedOdd;
		this.allPossibleRoutesUnmappedEven = allPossibleRoutesUnmappedEven;		
	}

	public RouteBuilder(List<HexavalentLogic> hexavalentLogicFullSpace, String networkName) {
		this.networkName = networkName;
		
		//optimize HexxavalentLogic for faster vertices access
		this.hexavalentLogicFullSpace = hexavalentLogicFullSpace.stream().map(hl->new HexavalentLogicWrapper(hl)).collect(Collectors.toList());
		this.hexavalentLogicFullSpaceAsMapByNodeIndex = HexavalentLogicUtil.getHexavalentLogicFullSpaceAsMapByNodeIndex(hexavalentLogicFullSpace);
		this.allPossibleRoutesUnmappedOdd = RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogicFullSpace.stream().filter(hl->hl.getIndex() == oddClusterIndex).findFirst().get());
		
		if (networkName.equals("SUSTAINABLE_VERTICES::0") ||
				networkName.equals("METABOLIC_VERTICES::0")) {
//			it only happens on SV::0
			evenClusterIndex = 2;
		}
		this.allPossibleRoutesUnmappedEven = RoutesInsideHexavalentLogic.getAllPossibleRoutes(hexavalentLogicFullSpace.stream().filter(hl->hl.getIndex() == evenClusterIndex).findFirst().get());
	}

	
	public List<List<Integer>> computeHexavalentLogicRoute(int addressIndexStart, int addressIndexEnd,
			Set<Integer> skipVertexs, 
			Set<Integer> skipHexavalentLogic,
			Set<Integer> includeVertex){
		
		Pair<HexavalentLogic, Vertex> clusterStart = RoutesUtil.getHexavalentLogicContainingAddressIndex(addressIndexStart, hexavalentLogicFullSpace);
		Pair<HexavalentLogic, Vertex> clusterEnd = RoutesUtil.getHexavalentLogicContainingAddressIndex(addressIndexEnd, hexavalentLogicFullSpace);
		
		//for central hexagon
				
		List<List<Integer>> allPossibleRoutes = new ArrayList<>();
		
		if (false && skipVertexs.size() == 0 && skipHexavalentLogic.size() == 0 && includeVertex.size() == 0) {
			
			//step 1 draw line with Bresenham and a stroke using only column&row indexes
			List<Pair<Integer, Integer>> routeByRowColumn = BresenhamRoute.drawBresenhamLine(clusterStart.getFirst().getColumn(), clusterStart.getFirst().getRow(), clusterEnd.getFirst().getColumn(), clusterEnd.getFirst().getRow(), 0);
			
			//step 2 reduce hexavalent space to the one described by Bresenham
			Set<String> routeByRowColumnSet = routeByRowColumn.stream().map(p->p.getFirst()+"_"+p.getSecond()).collect(Collectors.toSet()); 
			List<HexavalentLogic> hexavalentLogicReducedWithBresenham = hexavalentLogicFullSpace.stream().filter
					(hl->routeByRowColumnSet.contains(hl.getColumn()+"_"+hl.getRow())).
					collect(Collectors.toList());
		
			//step 3 draw geometrical line and keep only hexagons intersecting the line
			List<HexavalentLogic> route = computeMinimalGeometricalHexavalentLogicRoute(clusterStart, clusterEnd, hexavalentLogicReducedWithBresenham);
			
			//step 4 compute some possible routes
			allPossibleRoutes = computeSomePossibleRoutes(clusterStart, clusterEnd, route, combinationsNo, true);
		}
		
		if (allPossibleRoutes == null || allPossibleRoutes.size() == 0) {
			allPossibleRoutes = new ArrayList<>();
			//apply djikstra
			List<Integer> dijkstraShortestRoute = new DijkstraRoute().getShortestPath(hexavalentLogicFullSpace, skipVertexs, skipHexavalentLogic, includeVertex, 
					addressIndexStart, addressIndexEnd);
			if (dijkstraShortestRoute.isEmpty()) {
				return allPossibleRoutes;
			}
			
			allPossibleRoutes.add(dijkstraShortestRoute);
			List<HexavalentLogic> route = getHexavalentRouteFromVertexRoute(dijkstraShortestRoute);
			
			List<List<Integer>> otehrRoutes = computeSomePossibleRoutes(
					RoutesUtil.getHexavalentLogicContainingAddressIndex(addressIndexStart, hexavalentLogicFullSpace, route.get(0)), 
					RoutesUtil.getHexavalentLogicContainingAddressIndex(addressIndexEnd, hexavalentLogicFullSpace, route.get(route.size()-1)),
					route, combinationsNo, false);
			allPossibleRoutes.addAll(otehrRoutes);
			
			return keepMaxCombinationsNo(orderByNodesLength(allPossibleRoutes));
		}
		return allPossibleRoutes;
	}
	
	private List<List<Integer>> orderByNodesLength(List<List<Integer>> allPossibleRoutes){
		if (allPossibleRoutes == null || allPossibleRoutes.isEmpty()) {
			return allPossibleRoutes;
		}
		//the first entry is dijkstra and the shortest as space distance
		List<List<Integer>> result = new ArrayList<List<Integer>>() {{add(allPossibleRoutes.get(0));}};
		allPossibleRoutes.remove(0);
		result.addAll(allPossibleRoutes.stream().sorted((l1, l2)->(l1.size()-l2.size())).collect(Collectors.toList()));
		return result;
	}
	
	private List<List<Integer>> keepMaxCombinationsNo(List<List<Integer>> allPossibleRoutes){
		if (allPossibleRoutes == null) {
			return null;
		}
		if (allPossibleRoutes.size() <= combinationsNoToBeReturned) {
			return allPossibleRoutes;
		}
		List<List<Integer>> result = new ArrayList<List<Integer>>(combinationsNoToBeReturned);
		for (int i=0; i < combinationsNoToBeReturned; i++) {
			result.add(allPossibleRoutes.get(i));
		}
		return result;
	}
	
	private List<HexavalentLogic> getHexavalentRouteFromVertexRoute(List<Integer> dijkstraShortestRoute) {
		List<HexavalentLogic> route = new ArrayList<>();
		
		
		Integer previousNode = dijkstraShortestRoute.get(0);
		List<HexavalentLogic> startHeagons = RoutesUtil.getHexavalentLogicContainingAddressIndex(previousNode, hexavalentLogicFullSpaceAsMapByNodeIndex);
		HexavalentLogic previousHexagon = startHeagons.stream().filter(hl->hl.getVertices().stream().filter(v->v.getIndex() == dijkstraShortestRoute.get(1)).count() == 1).findFirst().get();
		route.add(previousHexagon);
		for (int index = 1; index < dijkstraShortestRoute.size(); index++) {	
			List<HexavalentLogic> currentHeagons = RoutesUtil.getHexavalentLogicContainingAddressIndex(dijkstraShortestRoute.get(index), hexavalentLogicFullSpaceAsMapByNodeIndex);
			final HexavalentLogic previousHexagonF = previousHexagon;
			//check if the nodes are in the same hexagon
			Optional<HexavalentLogic> theSameHexagon = currentHeagons.stream().filter(chl->chl.getIndex() == previousHexagonF.getIndex()).findFirst();
			if (theSameHexagon.isPresent()) {
				previousNode = dijkstraShortestRoute.get(index);
				continue;
			}
			List<HexavalentLogic> previousHeagons = RoutesUtil.getHexavalentLogicContainingAddressIndex(previousNode, hexavalentLogicFullSpaceAsMapByNodeIndex);
			Optional<HexavalentLogic> theSameHexagonByNode = currentHeagons.stream().filter(chl-> previousHeagons.stream().filter(pcl-> pcl.getIndex() == chl.getIndex()).count() == 1).findFirst();
			if (theSameHexagonByNode.isPresent()) {
				previousNode = dijkstraShortestRoute.get(index);
				previousHexagon = theSameHexagonByNode.get();
				route.add(theSameHexagonByNode.get());
				continue;
			}
			
			final int indexF = index;
			if (index < dijkstraShortestRoute.size() -1) {
				Optional<HexavalentLogic> nextHexagon = currentHeagons.stream().filter(hl->hl.getVertices().stream().filter(v->v.getIndex() == dijkstraShortestRoute.get(indexF + 1)).count() == 1).findFirst();
				if (nextHexagon.isPresent()) {
					previousNode = dijkstraShortestRoute.get(index);
					previousHexagon = nextHexagon.get();
					route.add(nextHexagon.get());
				}
			}else {
				previousNode = dijkstraShortestRoute.get(index);
				previousHexagon = currentHeagons.get(0);
				route.add(currentHeagons.get(0));
			}
		}
		return route;
	}
	
	private List<List<Integer>> computeSomePossibleRoutes(Pair<HexavalentLogic, Vertex> start, Pair<HexavalentLogic, Vertex> end, List<HexavalentLogic> route, int combinationsNo, boolean withShortest) {
		
		Map<Integer, List<List<Integer>>> mapByRouteIndexWithAllInternalSubRoutes = new HashMap<>();
		List<Vertex> previousAdjacentPoints = Arrays.asList(new Vertex[] {start.getSecond()});
		for (int index = 0; index < route.size() -1; index++) {
			List<Vertex> adjacentPoints = RoutesUtil.getAdjacentVertex(route.get(index), route.get(index+1));
			if (adjacentPoints == null || adjacentPoints.size() == 0) {
				continue;
			}
			Map<String, List<List<Integer>>>  allPossibleRoutes = RoutesInsideHexavalentLogic.getAllPossibleRoutesMapped(route.get(index), hexavalentLogicFullSpace, allPossibleRoutesUnmappedOdd, allPossibleRoutesUnmappedEven,
					oddClusterIndex, evenClusterIndex);
			for (Vertex previousAdjacentPoint:previousAdjacentPoints) {
				for (Vertex adjacentPoint:adjacentPoints) {
					int internalCombinationsNo = (int) Math.floor(Math.log(combinationsNo) / Math.log(route.size()) / previousAdjacentPoints.size() / adjacentPoints.size());
					if (internalCombinationsNo < 0) {
						internalCombinationsNo = 1;
					}
					while (Math.pow(internalCombinationsNo, route.size()) > combinationsNo) {
						internalCombinationsNo--;
						if (internalCombinationsNo < 1) break;
					}
					if (internalCombinationsNo < 1) {
						internalCombinationsNo = 1;
					}
					addPossibleRoutes(route, index, previousAdjacentPoint, adjacentPoint, mapByRouteIndexWithAllInternalSubRoutes, 
							allPossibleRoutes,internalCombinationsNo, withShortest);
				}
			}
			previousAdjacentPoints = adjacentPoints;
		}
		
		//last hexagon
		Map<String, List<List<Integer>>>  allPossibleRoutes = RoutesInsideHexavalentLogic.getAllPossibleRoutesMapped(end.getFirst(), hexavalentLogicFullSpace, allPossibleRoutesUnmappedOdd, allPossibleRoutesUnmappedEven,
				oddClusterIndex, evenClusterIndex);
		for (Vertex previousAdjacentPoint:previousAdjacentPoints) {
			
			int internalCombinationsNo = combinationsNo;
			if (route.size() > 1) {
				internalCombinationsNo = (int) Math.ceil(Math.log(combinationsNo) / Math.log(route.size()) / previousAdjacentPoints.size());
			}
			if (internalCombinationsNo < 0) {
				internalCombinationsNo = 1;
			}
			
			addPossibleRoutes(route, route.size() -1, previousAdjacentPoint, end.getSecond(), mapByRouteIndexWithAllInternalSubRoutes, allPossibleRoutes, internalCombinationsNo, withShortest);
		}
		
		if (mapByRouteIndexWithAllInternalSubRoutes.keySet().size() != route.size()) {
			//route not found
			return null;
		}
		
		List<List<Integer>> result = combineRoutesFromHexagons(mapByRouteIndexWithAllInternalSubRoutes);
		return result;
	}

	
	private List<List<Integer>> combineRoutesFromHexagons(Map<Integer, List<List<Integer>>> mapByRouteIndexWithAllInternalSubRoutes){
		List<List<Integer>> result = new ArrayList<>();
		List<List<Integer>> previousRoutes = mapByRouteIndexWithAllInternalSubRoutes.get(0);
		result.addAll(previousRoutes);
		for (int index = 1; index < mapByRouteIndexWithAllInternalSubRoutes.keySet().size(); index++) {
			List<List<Integer>> curentRoutes = mapByRouteIndexWithAllInternalSubRoutes.get(index);
			List<List<Integer>> resultLocal = new ArrayList<>(result);
			result.clear();
			for (List<Integer> resultPart : resultLocal) {
				for (List<Integer> curentRoute : curentRoutes) {
					if (resultPart.get(resultPart.size()-1).intValue() == curentRoute.get(0).intValue()) {
						List<Integer> local = new ArrayList<>(resultPart);
						List<Integer> currentRouteLocal = new ArrayList<>(curentRoute);
						currentRouteLocal.remove(0);
						local.addAll(currentRouteLocal);
						result.add(local);
					}
				}
			}
			if (result.isEmpty()) {
				result.addAll(resultLocal);
			}
			previousRoutes = curentRoutes;
		}
		return result;
	}
	
	private void addPossibleRoutes(List<HexavalentLogic> route, int index, Vertex previousAdjacentPoint, Vertex adjacentPoint, 
			Map<Integer, List<List<Integer>>> mapByRouteIndexWithAllInternalSubRoutes, Map<String, List<List<Integer>>>  allPossibleRoutes,
			int combinationsNo, boolean withShortest) {
		
		if (mapByRouteIndexWithAllInternalSubRoutes.get(index) == null){
			mapByRouteIndexWithAllInternalSubRoutes.put(index, new ArrayList<>());
		}

		if (withShortest) {
			List<Integer> currentProposal = shortestRoute(allPossibleRoutes, route.get(index)).
					get(previousAdjacentPoint.getIndex()+"_"+adjacentPoint.getIndex());
			if (currentProposal == null) {
				return;
			}
			
			mapByRouteIndexWithAllInternalSubRoutes.get(index).add(currentProposal);
		}
		
		//add a fixed combinationsNo
		List<List<Integer>> allPossibleRoutesFor = allPossibleRoutes.get(previousAdjacentPoint.getIndex()+"_"+adjacentPoint.getIndex());
		if (allPossibleRoutesFor == null || allPossibleRoutesFor.isEmpty()) {
			return;
		}
		for (int howMany = 0; howMany < combinationsNo && /* howMany < Math.ceil(Math.log(combinationsNo) / Math.log(route.size())) && */ 
				howMany < allPossibleRoutesFor.size(); howMany++) {
			mapByRouteIndexWithAllInternalSubRoutes.get(index).
							add(
									allPossibleRoutesFor.get(howMany));
		}
	}
	
	
	private List<HexavalentLogic> computeMinimalGeometricalHexavalentLogicRoute(Pair<HexavalentLogic, Vertex> clusterStart, Pair<HexavalentLogic, Vertex> clusterEnd, List<HexavalentLogic> hexavalentLogicReducedWithBresenham){
		List<HexavalentLogic> min = null;
		List<Vertex> startVertex = clusterStart.getFirst().getInnerVertices();
	
		for (int startIndex = 0; startIndex < startVertex.size(); startIndex++) {
			try {
				List<HexavalentLogic> result = computeGeometricalHexavalentLogicRoute(
						startVertex.get(startIndex).getPoint(), 
						clusterEnd.getFirst().getCenter(),
						clusterStart.getFirst(), clusterEnd.getFirst(), 
						hexavalentLogicReducedWithBresenham);
				if (min == null || min.size() > result.size()) {
					min = result;
				}
			}catch(IllegalStateException e) {
				//hen the distance from start point is equal
				e.printStackTrace();
				continue;
			}
		}
		return min;
	}
	
	private List<HexavalentLogic> computeGeometricalHexavalentLogicRoute(Point start, Point end, HexavalentLogic clusterStart, HexavalentLogic clusterEnd,
			List<HexavalentLogic> hexavalentLogicReducedWithBresenham){
		
		List<HexavalentLogic> result = GeometricRoute.draw(start, end, hexavalentLogicReducedWithBresenham);
		Map<Integer, HexavalentLogic> whatToAdd = new HashMap<Integer, HexavalentLogic>(); 
		for (int i=0; i < result.size()-1; i++) {
			if (checkIfMissingAdjacentPoints(result.get(i), result.get(i+1))){
				System.err.println("missing adjacent point");
			}
		}
		
		return result;
	}
	
	
	private boolean checkIfMissingAdjacentPoints(HexavalentLogic start, HexavalentLogic end) {
		return RoutesUtil.getAdjacentPoints(start, end).size() == 0;
	}
	
//	//this is it
//	public Map<String, List<List<Integer>>> getAllPossibleRoutes(HexavalentLogic hl){
//		long start = System.currentTimeMillis();
//		Map<String, List<List<Integer>>> graph = new HashMap<>();
//		for (Vertex source :hl.getVertices()) {
//			for (Vertex dest :hl.getVertices()) {
//				if (source.getIndex() != dest.getIndex()) {
//					Stack<Integer> route = new Stack<Integer>();
//					route.push(source.getIndex());
//					graph.put(source.getIndex()+"_"+dest.getIndex(), new ArrayList<List<Integer>>());
//					findRoute(dest.getIndex(), route, graph, hl);
//				}
//			}
//		}
//		System.out.println("getAllPossibleRoutes:"+(System.currentTimeMillis()-start));
//		return graph;
//	}
//	
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
//	
//	private void findRoute(int destIndex, Stack<Integer> route, Map<String, List<List<Integer>>>  graph, HexavalentLogic hl) {
////		System.err.println("show:"+Arrays.asList(route.toArray(new Integer[0])).stream().map(l->l+"").collect(Collectors.joining(",")));
//		if (route.peek() == destIndex) {
//			graph.get(route.get(0)+"_"+destIndex).add(Arrays.asList(route.toArray(new Integer[0])));
////			System.out.println("----------------Found:"+route.get(0)+"_"+destIndex+":"+Arrays.asList(route.toArray(new Integer[0])).stream().map(l->l+"").collect(Collectors.joining(",")));
//			route.pop();
//			return;
//		}
//		hl.getConnectors().stream().filter(connector->connector.getHead().getIndex() == route.peek()).
//			filter(connector-> !route.contains(connector.getTail().getIndex())).
//			forEach(connector-> {
//				route.push(connector.getTail().getIndex());
////				System.out.println("try:"+Arrays.asList(route.toArray(new Integer[0])).stream().map(l->l+"").collect(Collectors.joining(",")));
//				findRoute(destIndex, route, graph, hl);
//			});
//		route.pop();
//	}
	
}
