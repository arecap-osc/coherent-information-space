package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;
// Java implementation of Dijkstra's Algorithm  
// using Priority Queue 

import org.hkrdi.eden.ggm.algebraic.Connector;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;

import java.util.*;
import java.util.stream.Collectors;


public class DijkstraRouteOld { 
    private Map<Integer, Long> dist; 
    private Set<Integer> settled; 
    private PriorityQueue<Node> pq; 
    private int verticesNo; // Number of vertices 
    Map<Integer, List<Node> > adj; 
    
    private static final int NO_PARENT = -1;
    
    // Parent array to store shortest 
    // path tree 
    Map<Integer, Integer> parents;
    
    Set<Integer> vertexes;
    
    public DijkstraRouteOld(List<HexavalentLogic> hexavalentLogicFullSpace, 
    		Set<Integer> skipVertexs, Set<Integer> skipHexavalentLogic,
    		Set<Integer> includeVertex){ 
    	
    	vertexes = new HashSet<>();
    	hexavalentLogicFullSpace.stream().filter(hlf->!skipHexavalentLogic.contains(hlf.getIndex())).forEach(
    			hl->vertexes.addAll(
    					hl.getVertices().stream().filter(vertex->!skipVertexs.contains(vertex.getIndex()))
    					.map(v->v.getIndex()).collect(Collectors.toSet())));
    	
    	this.verticesNo = vertexes.size();
    	vertexes = new TreeSet<>(vertexes);
    	
    	this.adj = new HashMap<Integer, List<Node>>(verticesNo);
    	
//    	for(int i=0;i)
    	this.parents = new HashMap<>(verticesNo);
    	
    	hexavalentLogicFullSpace.stream().filter(hlf->!skipHexavalentLogic.contains(hlf.getIndex())).forEach(hl->{
    		List<Connector> connectors = hl.getConnectors();
    		hl.getVertices().stream().filter(vertex->!skipVertexs.contains(vertex.getIndex())).forEach(vertex->{
    			connectors.stream().filter(c->c.getHead().getIndex() == vertex.getIndex()).forEach(conn->{
    				List<Node> adjs = adj.get(conn.getHead().getIndex());
    				if (adjs == null) {
    					adj.put(conn.getHead().getIndex(), adjs = new ArrayList<>());
    				}
    				if (includeVertex.contains(conn.getTail().getIndex())) {
    					adjs.add(new Node(conn.getTail().getIndex(), -100_000L));
    				}else {
	    				adjs.add(new Node(conn.getTail().getIndex(), 
	    						(long) GeometricRoute.segmentLength(
	    								conn.getHead().getPoint(), conn.getTail().getPoint())));
    				}
    			});
    			
    			connectors.stream().filter(c->c.getTail().getIndex() == vertex.getIndex()).forEach(conn->{
    				List<Node> adjs = adj.get(conn.getTail().getIndex());
    				if (adjs == null) {
    					adj.put(conn.getTail().getIndex(), adjs = new ArrayList<>());
    				}
    				if (includeVertex.contains(conn.getHead().getIndex())) {
    					adjs.add(new Node(conn.getHead().getIndex(), -100_000L));
    				}else {
    					adjs.add(new Node(conn.getHead().getIndex(), 
    						(long) GeometricRoute.segmentLength(
    								conn.getHead().getPoint(), conn.getTail().getPoint())));
    				}
    			});
    		});
    	});
    	
         
        dist = new HashMap<>(verticesNo); 
        settled = new HashSet<Integer>(); 
        pq = new PriorityQueue<Node>(verticesNo, new Node()); 
    } 
  
    // Function for Dijkstra's Algorithm 
    public List<Integer> dijkstra(int startIndex, int endIndex){ 

    	// The starting vertex does not  
        // have a parent 
        parents.put(startIndex, NO_PARENT); 
        
        vertexes.forEach(v->dist.put(v, Long.MAX_VALUE));
//        for (int i = 0; i < verticesNo; i++) { 
//            dist.put(i, Long.MAX_VALUE);
//        }
  
        // Add source node to the priority queue 
        pq.add(new Node(startIndex, 0)); 
  
        // Distance to the source is 0 
        dist.put(startIndex, 0L);
        while (settled.size() != verticesNo) { 
  
            // remove the minimum distance node  
            // from the priority queue  
            int u = pq.remove().node; 
  
            // adding the node whose distance is 
            // finalized 
            settled.add(u); 
  
            e_Neighbours(u); 
        } 
        
//        dist[endIndex];
        List<Integer> result = new ArrayList<>();
        printPath(endIndex, parents, result);
        return result;
    } 
  
    // Function to process all the neighbours  
    // of the passed node 
    private void e_Neighbours(int u) 
    { 
        long edgeDistance = -1; 
        long newDistance = -1; 
  
        // All the neighbors of v 
        for (int i = 0; i < adj.get(u).size(); i++) { 
            Node v = adj.get(u).get(i); 
  
            // If current node hasn't already been processed 
            if (!settled.contains(v.node)) { 
                edgeDistance = v.cost; 
                newDistance = dist.get(u) + edgeDistance; 
  
                // If new distance is cheaper in cost 
                if (newDistance < 
                		dist.get(v.node)) 
                    dist.put(v.node,  newDistance); 

                parents.put(v.node, u); 
                // Add the current node to the queue 
                pq.add(new Node(v.node, dist.get(v.node))); 
            } 
        } 
    } 
  
	private static void printPath(int currentVertex, Map<Integer, Integer> parents, List<Integer> result) {

		// Base case : Source node has
		// been processed
		if (currentVertex == NO_PARENT) {
			return;
		}
		printPath(parents.get(currentVertex), parents, result);
		result.add(currentVertex);
//		System.out.print(currentVertex + " ");
	}
    
//    // Driver code 
//    public static void main(String arg[]) 
//    { 
//        int V = 5; 
//        int source = 0; 
//  
//        // Adjacency list representation of the  
//        // connected edges 
//        List<List<Node> > adj = new ArrayList<List<Node> >(); 
//  
//        // Initialize list for every node 
//        for (int i = 0; i < V; i++) { 
//            List<Node> item = new ArrayList<Node>(); 
//            adj.add(item); 
//        } 
//  
//        // Inputs for the DPQ graph 
//        adj.get(0).add(new Node(1, 9)); 
//        adj.get(0).add(new Node(2, 6)); 
//        adj.get(0).add(new Node(3, 5)); 
//        adj.get(0).add(new Node(4, 3)); 
//  
//        adj.get(2).add(new Node(1, 2)); 
//        adj.get(2).add(new Node(3, 4)); 
//  
//        // Calculate the single source shortest path 
//        DijkstraRoute dpq = new DijkstraRoute(V); 
//        dpq.dijkstra(adj, source); 
//  
//        // Print the shortest path to all the nodes 
//        // from the source node 
//        System.out.println("The shorted path from node :"); 
//        for (int i = 0; i < dpq.dist.length; i++) 
//            System.out.println(source + " to " + i + " is "
//                               + dpq.dist[i]); 
//    } 
} 
  
// Class to represent a node in the graph 
class Node implements Comparator<Node> { 
    public int node; 
    public long cost; 
  
    public Node() 
    { 
    } 
  
    public Node(int node, long cost) 
    { 
        this.node = node; 
        this.cost = cost; 
    } 
  
    @Override
    public int compare(Node node1, Node node2) 
    { 
        if (node1.cost < node2.cost) 
            return -1; 
        if (node1.cost > node2.cost) 
            return 1; 
        return 0; 
    } 
} 