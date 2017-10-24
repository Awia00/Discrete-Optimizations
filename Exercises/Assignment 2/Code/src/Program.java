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

        System.err.println("Parsing");
        SetCoverInstance[] instances = Arrays.stream(fileNames).map(fileName -> {
            try {
                return SetCoverParser.ParseSetCover(fileName);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }).toArray(SetCoverInstance[]::new);
        System.err.println("Parsing done.");

        for (SetCoverSolver solver : solvers) {
            for (SetCoverInstance instance : instances) {
                if (instance.fileName.equals(fileNames[fileNames.length - 1]) && solver.getClass() == SetCoverSimplex.class) {
                    System.out.printf("%s;%s;%d;%d;SKIPPED;SKIPPED\n", solver.getClass().getName(), instance.fileName, instance.m, instance.n);
                    continue;
                }

                long time = System.currentTimeMillis();
                int solution = solver.solveSetCover(instance);
                time = System.currentTimeMillis() - time;

                System.out.printf("%s;%s;%d;%d;%d;%d\n", solver.getClass().getName(), instance.fileName, instance.m, instance.n, solution, time);
            }

            System.out.println();
        }
    }
}
