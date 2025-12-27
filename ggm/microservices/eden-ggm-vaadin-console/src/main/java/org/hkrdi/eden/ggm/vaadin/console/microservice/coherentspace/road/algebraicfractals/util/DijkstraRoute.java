package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;

import org.hkrdi.eden.ggm.algebraic.Connector;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

public class DijkstraRoute {


	public List<Integer> getShortestPath(List<HexavalentLogic> hexavalentLogicFullSpace, 
		Set<Integer> skipVertexs, Set<Integer> skipHexavalentLogic,
		Set<Integer> includeVertex, int startIndex, int endIndex){ 

		List<Integer> includeVertexL = new ArrayList<>(includeVertex.stream().collect(Collectors.toList()));
		includeVertexL.add(endIndex);
		int previousIndex = startIndex;
		List<Integer> finalResult = new ArrayList<>();
		for (int i = 0; i < includeVertexL.size(); i++) {
			if (finalResult.contains(includeVertexL.get(i))) {
				continue;
			}
//			Set<Integer> skipVertexsExtendedToAvoidCycle = new HashSet<>(skipVertexs);
//			skipVertexsExtendedToAvoidCycle.addAll(finalResult);
//			if (finalResult.size() > 0) {
//				skipVertexsExtendedToAvoidCycle.remove(finalResult.get(finalResult.size()-1));
//			}
//			List<Integer> result = getShortestPathBetween(hexavalentLogicFullSpace, skipVertexsExtendedToAvoidCycle, skipHexavalentLogic, 
//					previousIndex, includeVertexL.get(i));
			List<Integer> result = getShortestPathBetween(hexavalentLogicFullSpace, skipVertexs, skipHexavalentLogic, 
					previousIndex, includeVertexL.get(i));
			if (!finalResult.isEmpty()) {
				result.remove(0);
			}
//				List<Integer> checkForCycle = new ArrayList<>(finalResult);
//				checkForCycle.addAll(result);
//				if (checkForCycle.stream().distinct().count() != checkForCycle.size()) {
//					Integer cycle = checkForCycle.stream().filter(c->Collections.frequency(checkForCycle, c) > 1).distinct().findFirst().get();
//					System.err.println("Cycle detected for:"+cycle);
////					return new ArrayList<>();
//				}
			
			finalResult.addAll(result);
			if (finalResult.contains(endIndex)) {
				break;
			}
			previousIndex = includeVertexL.get(i);
		}
		
		
		if (includeVertex.stream().filter(v->finalResult.contains(v)).count() != includeVertex.size()) {
			System.err.println("Not all includeVertex where touched");
			return new ArrayList<>();
		}
		if (finalResult.stream().distinct().count() != finalResult.size()) {
			System.err.println("We have a cycle!!!");
			return new ArrayList<>();
		}
		return finalResult;
	}
	
	private List<Integer> getShortestPathBetween(List<HexavalentLogic> hexavalentLogicFullSpace, 
    		Set<Integer> skipVertexs, Set<Integer> skipHexavalentLogic,
    		int startIndex, int endIndex){ 
    	
		Map<Integer, Node> nodes = new HashMap<>();
		
		Set<Integer> vertexes = new TreeSet<>();
    	hexavalentLogicFullSpace.stream().filter(hlf->!skipHexavalentLogic.contains(hlf.getIndex())).forEach(
    			hl->vertexes.addAll(
    					hl.getVertices().stream().filter(vertex->!skipVertexs.contains(vertex.getIndex()))
    					.map(v->v.getIndex()).collect(Collectors.toSet())));
    	
    	nodes.putAll(vertexes.stream().collect(Collectors.toMap(v->v, v->new Node(v))));
    	
    	hexavalentLogicFullSpace.stream().filter(hlf->!skipHexavalentLogic.contains(hlf.getIndex())).forEach(hl->{
    		List<Connector> connectors = hl.getConnectors();
    		hl.getVertices().stream().filter(vertex->!skipVertexs.contains(vertex.getIndex())).forEach(vertex->{
    			connectors.stream().filter(c->c.getHead().getIndex() == vertex.getIndex() &&
    					!skipVertexs.contains(c.getHead().getIndex()) && 
    					!skipVertexs.contains(c.getTail().getIndex())
    					).forEach(conn->{
    				Node node = nodes.get(conn.getHead().getIndex());
					node.getAdjacencies().add(new Edge(nodes.get(conn.getTail().getIndex()),
							/* 10_000_000d * */
							new BigDecimal(
									GeometricRoute.segmentLength(conn.getHead().getPoint(), conn.getTail().getPoint()), MathContext.DECIMAL128)
							));
    			});
    			
//    			connectors.stream().filter(c->c.getTail().getIndex() == vertex.getIndex()).forEach(conn->{
//    				Node node = nodes.get(conn.getTail().getIndex());
//    				if (includeVertex.contains(conn.getHead().getIndex())) {
//    					node.getAdjacencies().add(new Edge(nodes.get(conn.getHead().getIndex()), -100_000));
//    				}else {
//    					node.getAdjacencies().add(new Edge(nodes.get(conn.getHead().getIndex()),
//    							GeometricRoute.segmentLength(
//	    								conn.getTail().getPoint(), conn.getHead().getPoint())
//    							));
//    				}
//    			});
    		});
    	});
    	
    	computePaths(nodes.get(startIndex));
		return getShortestPathTo(nodes.get(endIndex)).stream().map(n->n.getIndexx()).collect(Collectors.toList());
    }
	
	private List<Integer> getShortestPath(int startIndex, int endIndex, Map<Integer, Node> nodes){
		computePaths(nodes.get(startIndex));
		return getShortestPathTo(nodes.get(endIndex)).stream().map(n->n.getIndexx()).collect(Collectors.toList());
	}
	
	private void computePaths(Node source){
		source.shortestDistance= new BigDecimal(0, MathContext.DECIMAL128);

		//implement a priority queue
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(source);

		while(!queue.isEmpty()){
			Node u = queue.poll();

			/*visit the adjacencies, starting from 
			the nearest node(smallest shortestDistance)*/
			
			for(Edge e: u.adjacencies){

				Node v = e.target;
				BigDecimal weight = e.weight;

				//relax(u,v,weight)
				BigDecimal distanceFromU = u.shortestDistance.add(weight);
				if(distanceFromU.compareTo(v.shortestDistance) < 0){

					/*remove v from queue for updating 
					the shortestDistance value*/
					queue.remove(v);
					v.shortestDistance = distanceFromU;
					v.parent = u;
					queue.add(v);

				}
			}
		}
	}

	
//	List<Node> path = getShortestPathTo(n13);
	
	private List<Node> getShortestPathTo(Node target){

		//trace path from target to source
		List<Node> path = new ArrayList<Node>();
		for(Node node = target; node!=null; node = node.parent){
			path.add(node);
		}


		//reverse the order such that it will be from source to target
		Collections.reverse(path);

		return path;
	}
	
	//define Node
	class Node implements Comparable<Node>{
		
		public final int index;
		public Set<Edge> adjacencies = new TreeSet<>();
		
		public BigDecimal shortestDistance = new BigDecimal(Long.MAX_VALUE, MathContext.DECIMAL128);
		public Node parent;

		public Node(int index){
			this.index = index;
		}

		public int getIndexx() {
			return index;
		}

		public Set<Edge> getAdjacencies() {
			return adjacencies;
		}

		public void setAdjacencies(Set<Edge> adjacencies) {
			this.adjacencies = adjacencies;
		}

		public String toString(){
				return index+"";
		}

		public int compareTo(Node other){
			return shortestDistance.compareTo(other.shortestDistance);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (index != other.index)
				return false;
			return true;
		}

		private DijkstraRoute getEnclosingInstance() {
			return DijkstraRoute.this;
		}

		public void setShortestDistance(BigDecimal shortestDistance) {
			this.shortestDistance = shortestDistance;
		}

	}

	//define Edge
	class Edge implements Comparable<Edge>{
		public final Node target;
		public final BigDecimal weight;
		public Edge(Node targetNode, BigDecimal weightVal){
			target = targetNode;
			weight = weightVal;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((target == null) ? 0 : target.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge other = (Edge) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (target == null) {
				if (other.target != null)
					return false;
			} else if (!target.equals(other.target))
				return false;
			return true;
		}
		private DijkstraRoute getEnclosingInstance() {
			return DijkstraRoute.this;
		}
		
		public int compareTo(Edge other){
			return Integer.compare(target.getIndexx(), other.target.getIndexx());
		}
	}
}
