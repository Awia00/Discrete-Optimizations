package setcover.solvers;

import setcover.IsIn;
import setcover.SetCoverInstance;
import setcover.SetCoverResult;

import java.util.HashSet;
import java.util.Set;

public class SimpleRoundingApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        CPLEXSolver simplexSolver = new CPLEXSolver();
        SetCoverResult setCoverResult = simplexSolver.solveLPSetCover(instance);

        //noinspection unchecked
        Set<Integer>[] sets = (Set<Integer>[]) new Set[instance.n];

        for (IsIn cover : instance.covers) {
            if (sets[cover.set] == null) {
                sets[cover.set] = new HashSet<>();
            }
            sets[cover.set].add(cover.element);
        }

        int f = 0;

        for (Set<Integer> set : sets) {
            if (f < set.size()) {
                f = set.size();
            }
        }

        int result = 0;
        for (int i = 0; i < setCoverResult.covers.length; i++) {
            if(setCoverResult.covers[i] > 1d/f)
            {
                result += instance.costs[i];
            }
        }
        return result;
    }
}
