package VertexCover;

import javafx.util.Pair;

import java.util.Collection;

public class Graph {
    int n;
    boolean[][] edges;

    public Graph(boolean[][] edges) {
        this.n = edges.length;
        this.edges = edges;
    }
}
