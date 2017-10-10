package TSP;

import TSP.DisjointSet.DSNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of branch-and-bound for TSP with the lower-bound method missing.
 */
public class BranchAndBound_TSP {
    /**
     * Finds the minimum spanning tree using Kruskals algorithm
     */
    private final Kruskal kruskal = new Kruskal();
    private final Graph graph;
    /**
     * The number of BnBNodes generated
     */
    private long nodesGenerated = 0;
    private double upperBound = Double.POSITIVE_INFINITY;

    /**
     * Construct a problem instance
     */
    public BranchAndBound_TSP(Graph g) {
        graph = g;
    }

    /**
     * Find the shortest tour visiting all nodes exactly once and return the result as a BnBNode.
     */
    public BnBNode solve() {
        //Sorting edges by length might or might not help
//		Collections.sort(graph.edges, new Comparator<Edge>(){
//			public int compare(Edge arg0, Edge arg1) {
//				return -Double.compare(graph.getLength(arg0),graph.getLength(arg1));
//			}});

        //The ordering in the nodePool determines which nodes gets polled first.
        PriorityQueue<BnBNode> nodePool = new PriorityQueue<BnBNode>(10000, (n0, n1) -> {
            //return Double.compare(n0.lowerBound, n1.lowerBound);//Best-first
            //return (n0.depth-n1.depth);//Breadth-first
            return (n1.depth - n0.depth);//Depth-first
        });

        BnBNode root = new BnBNode(null, null, false);
        root.lowerBound = lowerBound(root);

        double u = upperGreedySimple(root);
        double u2 = upperBound(root);

        upperBound = upperGreedy(root);
        nodePool.add(root);

        BnBNode best = root;

        while (!nodePool.isEmpty()) {
            BnBNode node = nodePool.poll();


            if (node.lowerBound <= upperBound) {
                // Possible improvement


                // If solution
                if (node.edgesDefined == (graph.getVertices())) {
                    double upper = objectiveValue(node);
                    if (upper == node.lowerBound && upper <= upperBound) {
                        best = node;
                        upperBound = upper;
                    }
                } else {
                    branch(node, nodePool);
                }
            }
        }

        System.out.printf("Finished branch-and-bound. Path-length: %.3f, %d nodes generated\n", best.lowerBound, nodesGenerated);
        return best;
    }

    /**
     * Branch on a node, generating either 0, 1 or 2 new children of the node. A child is only generated
     * if it does not result in a sub-tour, a vertex with degree more than 2, or a vertex where the degree
     * can never become 2.
     */
    private void branch(BnBNode node, PriorityQueue<BnBNode> nodePool) {
        if (graph.edges.indexOf(node.edge) == graph.edges.size() - 1) return;

        //Choose next edge to branch on. Uses the ordering in graph.edges.
        Edge nextEdge = graph.edges.get(graph.edges.indexOf(node.edge) + 1);

        //Represent graph components as sets (used to detect subtours)
        DisjointSet ds = new DisjointSet();
        DSNode[] vertexSets = new DSNode[graph.getVertices()];
        for (int i = 0; i < graph.getVertices(); i++)
            vertexSets[i] = ds.makeSet(i);
        BnBNode n = node;
        while (n.parent != null) {
            if (n.edgeIncluded) {
                ds.union(vertexSets[n.edge.u], vertexSets[n.edge.v]);
            }
            n = n.parent;
        }


        //Check out-degree<=2 and in-degree>=2
        //Find maximum adjacent edges (could be optimized away using an array)
        int uAdjMax = graph.incidentEdges[nextEdge.u].size();
        int vAdjMax = graph.incidentEdges[nextEdge.v].size();
        int uAdj = 0, vAdj = 0;
        //Find length of defined edges
        n = node;
        while (n.parent != null) {
            if (n.edgeIncluded) {
                if (n.edge.u == nextEdge.u || n.edge.v == nextEdge.u) uAdj++;
                if (n.edge.u == nextEdge.v || n.edge.v == nextEdge.v) vAdj++;
            } else {
                if (n.edge.u == nextEdge.u || n.edge.v == nextEdge.u) uAdjMax--;
                if (n.edge.u == nextEdge.v || n.edge.v == nextEdge.v) vAdjMax--;
            }
            n = n.parent;
        }

        //Exclude nextEdge (assuming constraints can be met)
        if (uAdjMax > 2 && vAdjMax > 2) {
            n = new BnBNode(node, nextEdge, false);
            n.lowerBound = lowerBound(n);
            if (n.lowerBound <= upperBound) {
                nodePool.add(n);
                nodesGenerated++;
            }
        }

        //Include nextEdge (assuming constraints can be met)
        if ((node.edgesDefined == graph.getVertices() - 1 || ds.find(vertexSets[nextEdge.u]) != ds.find(vertexSets[nextEdge.v])) && uAdj < 2 && vAdj < 2) {
            n = new BnBNode(node, nextEdge, true);
            n.lowerBound = lowerBound(n);
            if (n.lowerBound <= upperBound) {
                nodePool.add(n);
                nodesGenerated++;
            }
        }

    }


    /**
     * Return a lower-bound for the node
     */
    public double lowerBound(BnBNode node) {
        if (node.edgesDefined == graph.getVertices()) {
            return objectiveValue(node);
        }

        List<Edge> mst = kruskal.oneTree(graph, node, 0);

        double sum = 0;
        for (Edge edge : mst) {
            sum += graph.getLength(edge);
        }

        return sum;
    }

    private boolean createsNoIllegalCycle(List<Edge> tour, Edge edgeToAdd) {
        if (tour.size() == graph.getVertices() - 1) return true;

        Stack<Integer> stack = new Stack<>();
        stack.add(edgeToAdd.u);
        boolean[] visited = new boolean[graph.getVertices()];

        while (!stack.isEmpty()) {
            int v = stack.pop();

            if (!visited[v]) {
                visited[v] = true;

                for (Edge edge : tour.stream().filter(e -> e.u == v || e.v == v).collect(Collectors.toList())) {
                    int w = edge.u == v ? edge.v : edge.u;

                    if (w == edgeToAdd.v) return false;

                    if (!visited[w]) stack.push(w);
                }
            }
        }

        return true;
    }

    public double upperGreedySimple(BnBNode node) {
        Edge[] marked = new Edge[graph.getVertices()];
        Set<Edge> edges = new HashSet<>();

        BnBNode n = node;
        while (n.parent != null) {
            if (n.edgeIncluded) {
                if (marked[n.edge.v] == null) {
                    marked[n.edge.v] = n.edge;
                } else if (marked[n.edge.u] == null) {
                    marked[n.edge.u] = n.edge;
                } else {
                    System.err.println("Error :-(");
                }
                edges.add(n.edge);
            }
            n = n.parent;
        }

        for (int v = 0; v < marked.length; v++) {
            if (marked[v] == null) {
                Edge mostExpensive = null;
                for (Edge edge : graph.incidentEdges[v]) {
                    if (!edges.contains(edge)) {
                        if (mostExpensive == null || graph.getLength(mostExpensive) < graph.getLength(edge)) {
                            mostExpensive = edge;
                        }
                    }
                }

                if (mostExpensive != null) {
                    edges.add(mostExpensive);
                    marked[v] = mostExpensive;
                }
            }
        }

        if (edges.size() != graph.getVertices()) {
            List<Edge> remainingEdges = new ArrayList<>(graph.edges);
            remainingEdges.sort(Comparator.comparing(e -> -graph.getLength(e)));

            for (Edge remainingEdge : remainingEdges) {
                if (!edges.contains(remainingEdge)) {
                    edges.add(remainingEdge);
                    if (edges.size() == graph.getVertices()) break;
                }
            }
        }

        Visualization.visualizeSolution(graph, new ArrayList<>(edges));
        return edges.stream().mapToDouble(graph::getLength).sum();
    }


    public double upperGreedy(BnBNode node) {
        if (node.edgesDefined == graph.getVertices()) {
            return objectiveValue(node);
        }

        List<Edge> edges = new ArrayList<>(graph.edges);
        edges.sort(Comparator.comparing(graph::getLength));

        int[] degrees = new int[graph.getVertices()];

        List<Edge> tour = new ArrayList<>();

        BnBNode n = node;
        while (n.parent != null) {
            if (n.edgeIncluded) {
                tour.add(n.edge);
                degrees[n.edge.v]++;
                degrees[n.edge.u]++;
            } else {
                edges.remove(n.edge);
            }
            n = n.parent;
        }

        // Infeasible (?)
        if (edges.size() + tour.size() < graph.getVertices()) return Double.POSITIVE_INFINITY;

        if (tour.size() != graph.getVertices()) {
            for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
                Edge edge = iterator.next();
                if (createsNoIllegalCycle(tour, edge) && (degrees[edge.u] < 1 && degrees[edge.v] <= 1 || degrees[edge.u] <= 1 && degrees[edge.v] < 1))
                {
                    tour.add(edge);
                    degrees[edge.u]++;
                    degrees[edge.v]++;
                    iterator.remove();
                    if (tour.size() == graph.getVertices()) break;
                }
            }
        }

        if (tour.size() != graph.getVertices()) {
            for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
                Edge edge = iterator.next();
                if (createsNoIllegalCycle(tour, edge) && degrees[edge.u] <= 1 && degrees[edge.v] <= 1) {
                    tour.add(edge);
                    degrees[edge.u]++;
                    degrees[edge.v]++;
                    iterator.remove();
                    if (tour.size() == graph.getVertices()) break;
                }
            }
        }

        // Not a tour
        if (tour.size() != graph.getVertices()) return upperBound(node);

        double upper = 0;
        for (Edge edge : tour) {
            upper += graph.getLength(edge);
        }

        return upper;
    }

    public double upperBound(BnBNode node) {
        if (node.edgesDefined == graph.getVertices()) {
            return objectiveValue(node);
        }

        double sum = 0;
        int numberOfEdges = 0;
        BnBNode n = node;
        while (n.parent != null) {
            if (n.edgeIncluded) {
                sum += graph.getLength(n.edge);
                numberOfEdges++;
            }
            n = n.parent;
        }


        return sum + graph.edges
                .stream()
                .sorted(Comparator.comparing(edge -> -graph.getLength(edge)))
                .limit(graph.getVertices() - numberOfEdges)
                .mapToDouble(edge -> graph.distances[edge.u][edge.v])
                .sum();
    }

    /**
     * Assuming that n represents a valid hamiltonian tour return the length of the tour
     */
    public double objectiveValue(BnBNode n) {
        //Find length of defined edges
        double pathLength = 0;
        while (n.parent != null) {
            if (n.edgeIncluded)
                pathLength += graph.getDistance(n.edge.u, n.edge.v);
            n = n.parent;
        }
        return pathLength;
    }
}
