package TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import TSP.DisjointSet.DSNode;

/** Class for finding the minimum spanning tree using Kruskals algorithm */
public class Kruskal {
	private final DSNode[] nodes = new DSNode[200];//Hack
	private final DisjointSet ds = new DisjointSet();
	
	
	/** 
	 * Find the minimum spanning tree in a graph where included edges in <code>node</code> are 
	 * contracted and excluded edges in <code>node</code> are disregarded. 
	 */
	public List<Edge> minimumSpanningTree(final Graph graph, BnBNode node){
		for(Edge e: graph.edges){
			nodes[e.u] = ds.makeSet(e.u);
			nodes[e.v] = ds.makeSet(e.v); 
		}
		
		List<Edge> mstEdges = new LinkedList<Edge>(graph.edges);
		BnBNode n = node;
		while(n.parent!=null){
			if(n.edgeIncluded) 	ds.union(nodes[n.edge.u], nodes[n.edge.v]);		//Contract included edges
			else				mstEdges.remove(n.edge);						//Disregard excluded edges
			n=n.parent;
		}
		
		List<Edge> tmp = new ArrayList<Edge>(mstEdges);
		Collections.sort(tmp, new Comparator<Edge>(){	//Sort edges in nondescending order
			public int compare(Edge o1, Edge o2) {
				return Double.compare(graph.getDistance(o1.u, o1.v), graph.getDistance(o2.u, o2.v));
			}});
		
		for(Edge e: tmp){ //Main loop of Kruskal
			if(ds.find(nodes[e.u])!=ds.find(nodes[e.v])){
				ds.union(nodes[e.u], nodes[e.v]);
			}else{
				mstEdges.remove(e); 
			}
		}
		
		return mstEdges;
	}

	public List<Edge> oneTree(final Graph graph, BnBNode node, int oneVertex){

		List<Edge> mstEdges = new LinkedList<>();
		for(Edge e: graph.edges){
			if(e.u != oneVertex || e.v != oneVertex) {
				nodes[e.u] = ds.makeSet(e.u);
				nodes[e.v] = ds.makeSet(e.v);
				mstEdges.add(e);
			}
		}

		BnBNode n = node;
		while(n.parent!=null){
			if(n.edgeIncluded) 	ds.union(nodes[n.edge.u], nodes[n.edge.v]);		//Contract included edges
			else				mstEdges.remove(n.edge);						//Disregard excluded edges
			n=n.parent;
		}

		List<Edge> tmp = new ArrayList<Edge>(mstEdges);
		Collections.sort(tmp, new Comparator<Edge>(){	//Sort edges in nondescending order
			public int compare(Edge o1, Edge o2) {
				return Double.compare(graph.getDistance(o1.u, o1.v), graph.getDistance(o2.u, o2.v));
			}});

		for(Edge e: tmp){ //Main loop of Kruskal
			if(ds.find(nodes[e.u])!=ds.find(nodes[e.v])){
				ds.union(nodes[e.u], nodes[e.v]);
			}else{
				mstEdges.remove(e);
			}
		}

		Edge cheapest = null, nextCheapest = null;
		for (Edge e : graph.incidentEdges[oneVertex]) {
			if(cheapest == null) {
				cheapest = e;
			}
			else if (nextCheapest == null){
				nextCheapest = e;
			}
			else if(graph.getLength(e) < graph.getLength(nextCheapest)){
				if(graph.getLength(e) < graph.getLength(cheapest)){
					nextCheapest = cheapest;
					cheapest = e;
				}
				else {
					nextCheapest = e;
				}
			}
		}
		mstEdges.add(cheapest);
		mstEdges.add(nextCheapest);
		return mstEdges;
	}
	
	
//	public List<Edge> minimumSpanningTree(final Graph g, List<Edge> edges){
//		
//		List<Edge> ret = new ArrayList<Edge>(edges);
//		Collections.sort(edges, new Comparator<Edge>(){
//			public int compare(Edge o1, Edge o2) {
//				return Double.compare(g.getDistance(o1.u, o1.v), g.getDistance(o2.u, o2.v));
//			}});
//		
//		for(Edge e: edges){
//			nodes[e.u] = ds.makeSet(e.u);
//			nodes[e.v] = ds.makeSet(e.v); 
//		}
//		for(Edge e: edges){
//			if(ds.find(nodes[e.u])!=ds.find(nodes[e.v])){
//				ds.union(nodes[e.u], nodes[e.v]);
//			}else{
//				ret.remove(e);
//			}
//		}
//		
//		return ret;
//	}
	
}
