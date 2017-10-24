import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

public class Program {

    public static void main(String[] args) {
        String[] fileNames = new String[]{
                "res/scpa3.dat",
                "res/scpc3.dat",
                "res/scpnrf1.dat",
                "res/scpnrg5.dat"
        };

        if (args.length > 0) {
            fileNames = new String[]{args[0]};
        }

        SetCoverSolver[] solvers = new SetCoverSolver[]{
                new SetCoverSimplex(),
                new SimpleRoundingSetCoverApproximator(),
                new RandomizedRoundingSetCoverApproximator(),
                new PrimalDualSchemaSetCoverApproximator()
        };

        System.out.println("Parsing");
        SetCoverInstance[] instances = Arrays.stream(fileNames).map(fileName -> {
            try {
                return SetCoverParser.ParseSetCover(fileName);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }).toArray(SetCoverInstance[]::new);
        System.out.println("Parsing done." + System.lineSeparator());

        for (SetCoverSolver solver : solvers) {

            System.out.println("Solver: " + solver.getClass().getName());

            for (SetCoverInstance instance : instances) {
                System.out.println("Instance: " + instance.fileName);
                System.out.println("m = " + instance.m + ". n = " + instance.n);

                long time = System.currentTimeMillis();
                System.out.println("Solution: " + solver.solveSetCover(instance));
                System.out.println("Time: " + (System.currentTimeMillis() - time) + " ms" + System.lineSeparator());
            }

            System.out.println(System.lineSeparator());
        }
    }
}
