import java.io.IOException;
import java.text.ParseException;

public class Program {
    
    public static void main(String[] args){
        try {
            String fileName = "res/scpa3.dat";

            if (args.length > 0) {
                fileName = args[0];
            }

            SetCoverInstance instance = SetCoverParser.ParseSetCover(fileName);

            System.out.println("Solution: " + new SetCoverSimplex().solveSetCover(instance));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
