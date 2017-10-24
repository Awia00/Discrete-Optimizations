import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomizedRoundingSetCoverApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        SetCoverSimplex simplexSolver = new SetCoverSimplex();
        SetCoverResult setCoverResult = simplexSolver.solveLPSetCover(instance);
        Random random = new Random();

        int c = 1; // calculate c
        while (Math.exp(-c * Math.log(instance.m)) > 1 / (4*instance.m)) {
            c++;
        }

        int rounds = (int) ((c)*Math.log(instance.n));

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

        // check if every element is in the cover and that the cost is less 4*c*log(n)
        if (coveredElements.size() != instance.m || result >= 4 * c * Math.log(instance.m)) {
            // if not just recursively call solveSetCover
            return solveSetCover(instance);
        }

        return result;
    }
}
