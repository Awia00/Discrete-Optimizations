import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SetCoverParser {
    public static SetCoverInstance ParseSetCover(String fileName) throws IOException, ParseException {
        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            int m = -1, n = -1;
            int[] c = new int[0];
            List<IsIn> covers = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String id = line.split(" ", 2)[0];

                switch (id) {
                    case "m":
                        m = Integer.parseInt(line.replace("m = ", "").replace(";", ""));
                        break;
                    case "n":
                        n = Integer.parseInt(line.replace("n = ", "").replace(";", ""));
                        c = new int[n];
                        break;
                    case "c":
                        String[] cStrings = line.replace("c = [ ", "").replace("];", "").split(" ");
                        assert cStrings.length == n;
                        for (int i = 0; i < cStrings.length; i++) {
                            c[i] = Integer.parseInt(cStrings[i]);
                        }
                        break;
                    case "covers":
                        while (scanner.hasNextLine()) {
                            String cover = scanner.nextLine();
                            if (cover.startsWith("<")) {
                                String[] elems = cover.replace("<", "").replace(">", "").split(" ");
                                covers.add(new IsIn(Integer.parseInt(elems[0]) - 1, Integer.parseInt(elems[1]) - 1));
                            } else {
                                break;
                            }
                        }
                        break;
                    default:
                        throw new ParseException(line, 0);
                }
            }

            return new SetCoverInstance(fileName, n, m, covers.toArray(new IsIn[covers.size()]), c);
        }
    }
}
