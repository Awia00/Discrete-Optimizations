public class SimpleRoundingSetCoverApproximator implements SetCoverApproximator {
    @Override
    public int approximateSetCover(SetCoverInstance instance) {
        SetCoverSimplex simplexSolver = new SetCoverSimplex();
        SetCoverResult setCoverResult = simplexSolver.solveLPSetCover(instance);

        int result = 0;
        for (int i = 0; i < setCoverResult.covers.length; i++) {
            if(setCoverResult.covers[i] > 0.5)
            {
                result += instance.costs[i];
            }
        }
        return result;
    }
}
