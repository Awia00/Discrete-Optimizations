package Knapsack;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Knapsack {
    public static void solve(Item[] items, float V){
        try {
            IloCplex cplex = new IloCplex();

            // variables
            IloNumVar[] x =  cplex.boolVarArray(items.length);

            // objective
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < x.length; i++) {
                obj.addTerm(items[i].profit,x[i]);
            }
            cplex.addMaximize(obj);

            // constraints
            IloLinearNumExpr expr = cplex.linearNumExpr();
            for (int i = 0; i < x.length; i++) {
                expr.addTerm(items[i].weight, x[i]);
            }
            cplex.addLe(expr, V);

            boolean solved = cplex.solve();
            if(solved){
                for (int i = 0; i < x.length; i++) {
                    if(cplex.getValue(x[i]) == 1){
                        System.out.println("picked item " + i);
                    }
                }
            }

        } catch (IloException e) {
            e.printStackTrace();
        }

    }
}
