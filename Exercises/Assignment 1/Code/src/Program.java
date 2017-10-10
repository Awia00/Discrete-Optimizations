import TSP.*;

import java.util.List;

public class Program {

    public static void main(String[] args){
        cplexSubTourSolveGraph(new Instance1());
//        cplexSubTourSolveGraph(new Instance2());
//        cplexSubTourSolveGraph(new Instance3());

        cplexCompactSolveGraph(new Instance1());
        cplexCompactSolveGraph(new Instance2());
        cplexCompactSolveGraph(new Instance3());

        solveByBranchAndBound(new Instance1());
        solveByBranchAndBound(new Instance2());
//        solveByBranchAndBound(new Instance3());
    }

    public static void cplexCompactSolveGraph(Graph g){
        TSPSimplex solver = new TSPSimplex();
        long start = System.nanoTime();
        List<Edge> solution = solver.solveByCompactFormulation(g);
        long end = System.nanoTime();
        System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
        System.out.println(solution);
        //Visualization.visualizeSolution(g, solution);
    }

    public static void cplexSubTourSolveGraph(Graph g){
        TSPSimplex solver = new TSPSimplex();
        long start = System.nanoTime();
        List<Edge> solution = solver.solveBySubTourFormulation(g);
        long end = System.nanoTime();
        System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
        System.out.println(solution);
        //Visualization.visualizeSolution(g, solution);
    }

    public static void solveByBranchAndBound(Graph g){
        BranchAndBound_TSP solver = new BranchAndBound_TSP(g);
        long start = System.nanoTime();
        BnBNode n = solver.solve();
        long end = System.nanoTime();
        System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		System.out.println(n);
		//Visualization.visualizeSolution(g, n);//Requires ProGAL (www.diku.dk/~rfonseca/ProGAL)
    }
}
