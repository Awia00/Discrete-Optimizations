import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class SetCoverSimplex {

    public int solveSetCover(SetCoverInstance instance) {
        try {
            IloCplex cplex = new IloCplex();
            IloIntVar[] x = cplex.boolVarArray(instance.n);

            // add objective
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < instance.n; i++) {
                obj.addTerm(instance.costs[i], x[i]);
            }
            cplex.addMinimize(obj);

            // add constraints
            for (int i = 0; i < instance.m; i++) {
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int j = 0; j < instance.n; j++) {
                    if(instance.covers[j].set == i){
                        expr.addTerm(1, x[j]);
                    }
                }
                cplex.addGe(expr, 1);
            }

            int result = 0;
            if(cplex.solve()){
                double r = cplex.getObjValue();
                result = (int)r;
                if(r == result){
                    return result;
                }
            }
            throw new RuntimeException("Something went wrong");
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }

    public double solveLPSetCover(SetCoverInstance instance) {
        try {
            IloCplex cplex = new IloCplex();
            IloNumVar[] x = cplex.numVarArray(instance.n,0,1);

            // add objective
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < instance.n; i++) {
                obj.addTerm(instance.costs[i], x[i]);
            }
            cplex.addMinimize(obj);

            // add constraints
            for (int i = 0; i < instance.m; i++) {
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int j = 0; j < instance.n; j++) {
                    if(instance.covers[j].set == i){
                        expr.addTerm(1, x[j]);
                    }
                }
                cplex.addGe(expr, 1);
            }

            if(cplex.solve()){
                return cplex.getObjValue();
            }
            throw new RuntimeException("Something went wrong");
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }
}
