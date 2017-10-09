package TSP;

import TSP.Set.Set;
import TSP.Set.SubsetOfAtMostSizeIterable;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;

public class TSPSimplex {
    public List<Edge> solve(Graph graph) {
        try {
            IloCplex cplex = new IloCplex();
            int N = graph.getVertices();
            IloNumVar[] x =  cplex.boolVarArray(N*N);

            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if(graph.hasEdge(i,j))
                        obj.addTerm(graph.getLength(i,j), x[i]);
                }
            }
            cplex.addMinimize(obj);

            IloLinearNumExpr expr;
            for (int i = 0; i < N; i++) {
                expr = cplex.linearNumExpr();
                for (int j = 0; j < N; j++) {
                    if(graph.hasEdge(i,j))
                        expr.addTerm(1, x[i*N + j]);
                    else
                        expr.addTerm(0, x[i*N + j]);
                }
                cplex.addEq(expr, 1);
            }
            for (int j = 0; j < N; j++) {
                expr = cplex.linearNumExpr();
                for (int i = 0; i < N; i++) {
                    if(graph.hasEdge(i,j))
                        expr.addTerm(1, x[i*N + j]);
                    else
                        expr.addTerm(0, x[i*N + j]);
                }
                cplex.addEq(expr, 1);
            }

            Set<Integer> vertices = Set.of();
            for (int i = 0; i < N; i++) {
                vertices = vertices.add(i);
            }

            for (Set<Integer> S : Set.subsetsOfSizeAtMost(vertices, N - 2)) {
                if (S.size()>=2) {
                    expr = cplex.linearNumExpr();
                    for (Integer i : S) {
                        for (Integer j : S) {
                            expr.addTerm(1, x[i*N + j]);
                        }
                    }
                    cplex.addLe(expr, S.size()-1);
                }
            }

            boolean solved = cplex.solve();
            List<Edge> result = new ArrayList<>();
            double sum = 0;
            if(solved){
                for (int i = 0; i < N; i++) {
                    for (int j = i+1; j < N; j++) {
                        if(cplex.getValue(x[i*N+j]) == 1)
                        {
                            result.add(graph.getEdge(i,j));
                            sum += graph.getLength(i,j);
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
