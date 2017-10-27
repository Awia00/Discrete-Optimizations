package setcover.solvers;

import setcover.IsIn;
import setcover.SetCoverInstance;
import setcover.SetCoverResult;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomizedRoundingApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        CPLEXSolver simplexSolver = new CPLEXSolver();
        SetCoverResult setCoverResult = simplexSolver.solveLPSetCover(instance);
        Random random = new Random();

        // calculate c
        double c = Math.log(4*instance.m) / Math.log(instance.m);

        int rounds = (int) (c*Math.log(instance.m));

        for (int i = 0; i < rounds; i++) {
            for (int j = 0; j < setCoverResult.covers.length; j++) {
                if(random.nextDouble() < setCoverResult.covers[j]) // check for nextDouble include 1
                {
                    setCoverResult.covers[j] = 1;
                }
            }
        }

        // noinspection unchecked
        Set<Integer>[] sets = (Set<Integer>[]) new Set[instance.n];

        for (IsIn cover : instance.covers) {
            if (sets[cover.set] == null) sets[cover.set] = new HashSet<>();
            sets[cover.set].add(cover.element);
        }

        int result = 0;
        Set<Integer> coveredElements = new HashSet<>();
        for (int i = 0; i < setCoverResult.covers.length; i++) {
            if(setCoverResult.covers[i] == 1){
                coveredElements.addAll(sets[i]);
                result += instance.costs[i];
            }
        }

        // check if every element is in the cover and that the cost is less OPT_f*4*c*log(n)
        if (coveredElements.size() != instance.m || result >= setCoverResult.cost * 4 * c * Math.log(instance.m)) {
            // if not just recursively call solveSetCover
            return solveSetCover(instance);
        }

        return result;
    }
}
