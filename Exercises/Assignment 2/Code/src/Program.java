import java.io.IOException;
import java.text.ParseException;

public class Program {
    
    public static void main(String[] args){
        try {
            String[] fileNames = new String[] {
                    "res/scpa3.dat",
                    "res/scpc3.dat",
                    "res/scpnrf1.dat",
                    "res/scpnrg5.dat"
            };

            if (args.length > 0) {
                fileNames = new String[] { args[0] };
            }

            SetCoverSolver[] solvers = new SetCoverSolver[] {
                    new SetCoverSimplex(),
                    new SimpleRoundingSetCoverApproximator(),
                    new RandomizedRoundingSetCoverApproximator(),
                    new PrimalDualSchemaSetCoverApproximator()
            };

            for (SetCoverSolver solver : solvers) {
                System.out.println("Solver: " + solver.getClass().getName());

                for (String fileName : fileNames) {
                    System.out.println("Instance: " + fileName);
                    SetCoverInstance instance = SetCoverParser.ParseSetCover(fileName);
                    System.out.println("m = " + instance.m + ". n = " + instance.n);

                    System.out.println("Solution: " + solver.solveSetCover(instance));
                }

                System.out.println();
                System.out.println();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
