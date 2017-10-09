import TSP.*;

import java.util.List;

public class Program {

    public static void main(String[] args){
        //cplexSolveGraph(new Instance1());
        //cplexSolveGraph(new Instance2());
        //cplexSolveGraph(new Instance3());

        solveGraph(new Instance1());
        solveGraph(new Instance2());
        solveGraph(new Instance3());
    }

    public static void cplexSolveGraph(Graph g){
        TSPSimplex solver = new TSPSimplex();
        long start = System.nanoTime();
        List<Edge> solution = solver.solve(g);
        long end = System.nanoTime();
        System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
        System.out.println(solution);
        Visualization.visualizeSolution(g, solution);
    }

    public static void solveGraph(Graph g){
        BranchAndBound_TSP solver = new BranchAndBound_TSP(g);
        long start = System.nanoTime();
        BnBNode n = solver.solve();
        long end = System.nanoTime();
        System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		System.out.println(n);
		Visualization.visualizeSolution(g, n);//Requires ProGAL (www.diku.dk/~rfonseca/ProGAL)
    }
}
