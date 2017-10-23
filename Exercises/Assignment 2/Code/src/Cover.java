import java.util.List;

public class Cover {
    public final float cost;
    public final List<Integer> coveredElements;

    public Cover(float cost, List<Integer> coveredElements) {
        this.cost = cost;
        this.coveredElements = coveredElements;
    }
}
