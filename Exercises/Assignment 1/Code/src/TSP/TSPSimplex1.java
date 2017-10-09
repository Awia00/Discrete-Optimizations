package TSP;

import TSP.Set.Set;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;

public class TSPSimplex1 {
    public List<Edge> solve(Graph graph) {
        try {
            IloCplex cplex = new IloCplex();
            int N = graph.getVertices();
            IloNumVar[] x =  cplex.boolVarArray(N*N);

            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if(graph.hasEdge(i,j))
                        obj.addTerm(graph.getDistance(i,j), x[i*N + j]);
                }
            }
            cplex.addMinimize(obj);

            IloLinearNumExpr expr;
            for (int i = 0; i < N; i++) {
                expr = cplex.linearNumExpr();
                for (int j = 0; j < N; j++) {
                    if(graph.hasEdge(i,j))
                        expr.addTerm(1, x[i*N + j]);

                }
                cplex.addEq(expr, 1);
            }
            for (int j = 0; j < N; j++) {
                expr = cplex.linearNumExpr();
                for (int i = 0; i < N; i++) {
                    if(graph.hasEdge(i,j))
                        expr.addTerm(1, x[i*N + j]);
                }
                cplex.addEq(expr, 1);
            }

            IloNumVar[] T = cplex.intVarArray(N,0,N);
            for (int i = 0; i < N; i++) {
                for (int j = 1; j < N; j++) {
                    expr = cplex.linearNumExpr(1 - N);
                    expr.addTerm(1, T[i]);
                    expr.addTerm(N, x[i*N + j]);


                    cplex.addGe(T[j], expr);
                }
            }

            boolean solved = cplex.solve();
            List<Edge> result = new ArrayList<>();
            double sum = 0;
            if(solved){
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if(graph.hasEdge(i, j) && cplex.getValue(x[i*N+j]) == 1)
                        {
                            result.add(graph.getEdge(i,j));
                            sum += graph.getDistance(i,j);
                        }
                    }
                }
            }
            System.out.println("Simplex solved TSP with path-length: " + sum);
            return result;
        }  catch (IloException e) {
            e.printStackTrace();
            throw new RuntimeException("cplex dun goofed");
        }
    }
}
