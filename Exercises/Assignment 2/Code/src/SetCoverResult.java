import java.util.Arrays;

public class SetCoverResult {
    public final double cost;
    public final double[] covers;
    
    public SetCoverResult(double cost, double[] covers) {
        this.cost = cost;
        this.covers = covers;
    }

    @Override
    public String toString() {
        return "SetCoverResult{" +
                "cost=" + cost +
                ", covers=" + Arrays.toString(covers) +
                '}';
    }
}
