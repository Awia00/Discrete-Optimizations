package VertexCover;

import Knapsack.Item;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import javafx.util.Pair;

import java.util.Random;

public class VertexCover {

    public static void solve(Graph g) {
        try {
            IloCplex cplex = new IloCplex();

            // variables
            IloNumVar[] x =  cplex.boolVarArray(g.n);

            // objective
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < x.length; i++) {
                obj.addTerm(1, x[i]);
            }
            cplex.addMinimize(obj);

            // constraints
            for (int i = 0; i < g.n; i++) {
                for (int j = i+1; j < g.n; j++) {
                    if(g.edges[i][j]){
                        cplex.addGe(cplex.sum(x[i], x[j]), 1);
                    }
                }
            }

            boolean solved = cplex.solve();
            if(solved){
                for (int i = 0; i < x.length; i++) {
                    if(cplex.getValue(x[i]) == 1)
                        System.out.println("picked vertex" + i);
                }
            }

        } catch (IloException e) {
            e.printStackTrace();
        }
    }
}
