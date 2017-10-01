import Knapsack.Item;
import Knapsack.Knapsack;
import VertexCover.Graph;
import VertexCover.VertexCover;

import java.util.Random;

public class Program {

    public static void main(String[] args){

        // Knapsack
        System.out.println("===========>> Calculating Knapsack <<===========");
        knapsack(150000,150000);

        // Vertex Cover
        System.out.println("===========>> Calculating Vertex Cover <<===========");
        vertexCover(140);
    }

    public static void knapsack(int n, int V){
        Random random = new Random();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(random.nextInt(1000), random.nextInt(1000));
        }
        Knapsack.solve(items, V);
    }

    public static void vertexCover(int n){
        Random random = new Random();
        boolean[][] edges = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                edges[i][j] = random.nextBoolean() && random.nextBoolean();
            }
        }
        Graph graph = new Graph(edges);
        VertexCover.solve(graph);
    }
}
