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

            for (String fileName : fileNames) {
                System.out.println("Instance: " + fileName);
                SetCoverInstance instance = SetCoverParser.ParseSetCover(fileName);
                System.out.println("m = " + instance.m + ". n = " + instance.n);

                System.out.println("Solution: " + new SetCoverSimplex().solveSetCover(instance));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
