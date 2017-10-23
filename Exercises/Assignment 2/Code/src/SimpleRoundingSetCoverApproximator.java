public class SimpleRoundingSetCoverApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        SetCoverSimplex simplexSolver = new SetCoverSimplex();
        SetCoverResult setCoverResult = simplexSolver.solveLPSetCover(instance);

        int result = 0;
        int f = 2; // todo find f
        for (int i = 0; i < setCoverResult.covers.length; i++) {
            if(setCoverResult.covers[i] > 1/f)
            {
                result += instance.costs[i];
            }
        }
        return result;
    }
}
