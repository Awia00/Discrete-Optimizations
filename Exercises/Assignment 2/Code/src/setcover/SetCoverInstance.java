package setcover;

public class SetCoverInstance {
    public final String fileName;
    public final int n; // number of sets
    public final int m; // number of elements in universe
    public final IsIn[] covers; //
    public final int[] costs;

    public SetCoverInstance(String fileName, int n, int m, IsIn[] covers, int[] costs) {
        this.fileName = fileName;
        this.n = n;
        this.m = m;
        this.covers = covers;
        this.costs = costs;
    }
}
