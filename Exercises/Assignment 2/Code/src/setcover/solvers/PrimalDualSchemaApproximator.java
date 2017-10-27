package setcover.solvers;

import setcover.IsIn;
import setcover.SetCoverInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrimalDualSchemaApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        int[] x = new int[instance.n], y = new int[instance.m];

        // Build up the vector of sets.
        //noinspection unchecked
        final Set<Integer>[] S = (Set<Integer>[]) new Set[instance.n];

        for (IsIn cover : instance.covers) {
            if (S[cover.set] == null) S[cover.set] = new HashSet<>();
            S[cover.set].add(cover.element);
        }

        Set<Integer> coveredElements = new HashSet<>();

        // While there are still uncovered elements.
        while(coveredElements.size() != instance.m) {
            // Find an uncovered element.
            int uncovered;
            for (uncovered = 0; uncovered < instance.m; uncovered++) {
                if (!coveredElements.contains(uncovered)) break;
            }

            // Increment the dual variable corresponding to the element, until some set is tight.
            while (!isSomeSetTight(S, instance.costs, y, uncovered)) {
                y[uncovered]++;
            }

            // For each of the tight sets (this could probably be optimized to only return a set if
            // it was affected by the recent increment.
            for (Integer tightSet : tightSets(S, instance.costs, y)) {
                // Chose the set for the cover
                x[tightSet] = 1;
                // And mark all the elements of the set as covered.
                coveredElements.addAll(S[tightSet]);
            }
        }

        // The result is the costs of all chosen sets for the cover.
        int result = 0;
        for (int i = 0; i < x.length; i++) {
            result += x[i] * instance.costs[i];
        }

        return result;
    }

    /**
     * Returns the indices of the tight sets in S.
     * @param S The list of Sets to be checked.
     * @param c The cost vector. c[i] is the cost of S[i].
     * @param y The y vector, representing the dual variables.
     * @return A list containing indices of all tight sets.
     */
    private List<Integer> tightSets(final Set<Integer>[] S, final int[] c, final int[] y) {
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < S.length; i++) {
            if (isTight(S[i], c[i], y)) res.add(i);
        }

        return res;
    }

    /**
     * Checks whether some set containing e is tight.
     * @param S The list of sets to be checked.
     * @param c The cost vector. c[i] is the cost of S[i]
     * @param y The y vector, representing the dual variables.
     * @param e Only sets in S containing e will be checked for tightness.
     * @return true if some set containing e is tight. false otherwise.
     */
    private boolean isSomeSetTight(final Set<Integer>[] S, final int[] c, final int[] y, int e) {
        for (int i = 0; i < S.length; i++) {
            if (S[i].contains(e) && isTight(S[i], c[i], y)) return true;
        }
        return false;
    }

    /**
     * Checks whether a single set is tight in y with regards to its cost c_S.
     * @param s The set to check for tightness.
     * @param c_S The cost of picking this set for the Set setcover.Cover Problem.
     * @param y The y vector, representing the dual variables.
     * @return true if the set is tight with respect to c_S and y.
     */
    private boolean isTight(final Set<Integer> s, final int c_S, final int[] y) {
        int sum = 0;
        for (Integer e : s) {
            sum += y[e];
        }
        return sum == c_S;
    }
}
