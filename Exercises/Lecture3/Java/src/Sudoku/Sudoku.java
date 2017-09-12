package Sudoku;

import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Sudoku {

    /// Not Working
    public static void solve(){
        try{
            IloCplex cplex = new IloCplex();

            IloNumVar[][][] x = new IloNumVar[9][9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int k = 0; k < 9; k++) {
                        x[i][j][k] = cplex.boolVar();
                    }
                }
            }

            // objective
            IloLinearNumExpr expr = cplex.linearNumExpr();
            cplex.addMinimize(expr);

            // constraint
            for (int i = 0; i < 9; i++) {
                for (int k = 0; k < 9; k++) {
                    expr = cplex.linearNumExpr();
                    for (int j = 0; j < 9; j++) {
                        expr.addTerm(1, x[i][j][k]); // only one of the k's in this i, j
                    }
                    cplex.addEq(expr, 1); // expr == 1 one of them == 1
                }
            }
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    expr = cplex.linearNumExpr();
                    for (int i = 0; i < 9; i++) {
                        expr.addTerm(1, x[i][j][k]); // only one of the k's in this i, j
                    }
                    cplex.addEq(expr, 1); // expr == 1 one of them == 1
                }
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    expr = cplex.linearNumExpr();
                    for (int k = 0; k < 9; k++) {
                        expr.addTerm(1, x[i][j][k]); // only one of the k's in this i, j
                    }
                    cplex.addEq(expr, 1); // expr == 1 one of them == 1
                }
            }

            boolean solved = cplex.solve();
            if (solved)
                cplex.readSolution("");

            cplex.end();


        } catch (Exception e){
            System.out.println(e);
        }
    }
}
