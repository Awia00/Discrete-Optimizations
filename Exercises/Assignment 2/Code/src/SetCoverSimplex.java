
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetCoverSimplex {

    public SetCoverResult solveSetCover(SetCoverInstance instance) {
        try {
            IloCplex cplex = new IloCplex();
            IloIntVar[] x = cplex.boolVarArray(instance.n);

            // add objective
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i = 0; i < instance.n; i++) {
                obj.addTerm(instance.costs[i], x[i]);
            }
            cplex.addMinimize(obj);

            Map<Integer, Set<Integer>> setsByElement = new HashMap<>();

            for (IsIn cover : instance.covers) {
                if (!setsByElement.containsKey(cover.element)) {
                    setsByElement.put(cover.element, new HashSet<>());
                }

                setsByElement.get(cover.element).add(cover.set);
            }

            // add constraints
            for (int i = 0; i < instance.m; i++) {
                IloLinearNumExpr expr = cplex.linearNumExpr();

                for (Integer set : setsByElement.get(i)) {
                    expr.addTerm(1, x[set]);
                }
                cplex.addGe(expr, 1);
            }

            int result = 0;
            if(cplex.solve()){
                double r = cplex.getObjValue();
                result = (int)r;
                assert (r==result);
                double[] covers = new double[instance.n];
                for (double cover : covers) {
                    assert ((int)cover==cover);
                }

                return new SetCoverResult(result, covers);
            }
            throw new RuntimeException("Something went wrong");
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }

    public SetCoverResult solveLPSetCover(SetCoverInstance instance) {
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
            Map<Integer, Set<Integer>> setsByElement = new HashMap<>();

            for (IsIn cover : instance.covers) {
                if (!setsByElement.containsKey(cover.element)) {
                    setsByElement.put(cover.element, new HashSet<>());
                }

                setsByElement.get(cover.element).add(cover.set);
            }

            // add constraints
            for (int i = 0; i < instance.m; i++) {
                IloLinearNumExpr expr = cplex.linearNumExpr();

                for (Integer set : setsByElement.get(i)) {
                    expr.addTerm(1, x[set]);
                }
                cplex.addGe(expr, 1);
            }

            if(cplex.solve()) {
                return new SetCoverResult(cplex.getObjValue(), cplex.getValues(x));
            }
            throw new RuntimeException("Something went wrong");
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }
}
