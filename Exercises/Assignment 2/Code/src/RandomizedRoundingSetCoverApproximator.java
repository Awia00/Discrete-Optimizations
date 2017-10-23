import java.util.Random;

public class RandomizedRoundingSetCoverApproximator implements SetCoverSolver {
    @Override
    public int solveSetCover(SetCoverInstance instance) {
        SetCoverSimplex simplexSolver = new SetCoverSimplex();
        SetCoverResult setCoverResult = simplexSolver.solveLPSetCover(instance);
        Random random = new Random();

        double c = 10; // todo calculate c
        int rounds = (int) (c*Math.log(instance.n));
        for (int i = 0; i < rounds; i++) {
            for (int j = 0; j < setCoverResult.covers.length; j++) {
                if(random.nextDouble() < setCoverResult.covers[i]) // check for nextDouble include 1
                {
                    setCoverResult.covers[i] = 1;
                }
            }
        }

        int result = 0;
        for (int i = 0; i < setCoverResult.covers.length; i++) {
            if(setCoverResult.covers[i] == 1){
                result += instance.costs[i];
            }
        }

        // todo: do check if every element is in the cover and that the cost is less 4*c*log(n)
        // if not just recursively call approximateSetCover
        return result;
    }
}
