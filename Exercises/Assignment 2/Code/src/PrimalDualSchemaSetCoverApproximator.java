import java.util.*;

public class PrimalDualSchemaSetCoverApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        int[] x = new int[instance.n], y = new int[instance.m];

        //noinspection unchecked
        final Set<Integer>[] S = (Set<Integer>[]) new Set[instance.n];

        for (IsIn cover : instance.covers) {
            if (S[cover.set] == null) S[cover.set] = new HashSet<>();
            S[cover.set].add(cover.element);
        }

        Set<Integer> elements = new HashSet<>();

        while(elements.size() != instance.m) {
            int uncovered;
            for (uncovered = 0; uncovered < instance.m; uncovered++) {
                if (!elements.contains(uncovered)) break;
            }

            while (!isSomeSetTight(S, instance.costs, y, uncovered)) {
                y[uncovered]++;
            }

            List<Integer> tightSets = tightSets(S, instance.costs, y);

            for (Integer tightSet : tightSets) {
                x[tightSet] = 1;
                elements.addAll(S[tightSet]);
            }
        }

        int result = 0;
        for (int i = 0; i < x.length; i++) {
            result += x[i] * instance.costs[i];
        }

        return result;
    }

    private List<Integer> tightSets(final Set<Integer>[] S, final int[] c, final int[] y) {
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < S.length; i++) {
            if (isTight(S[i], c[i], y)) res.add(i);
        }

        return res;
    }

    private boolean isSomeSetTight(final Set<Integer>[] S, final int[] c, final int[] y, int e) {
        for (int i = 0; i < S.length; i++) {
            if (S[i].contains(e) && isTight(S[i], c[i], y)) return true;
        }
        return false;
    }

    private boolean isTight(final Set<Integer> s, final int c_S, final int[] y) {
        return s.stream().mapToInt(e -> y[e]).sum() == c_S;
    }
}
