import java.io.*;
public class CustomRuleSet implements Serializable {
    private final int[] survive;
    private final int[] birth;

    public CustomRuleSet(int[] survive, int[] birth) {
        this.survive = survive;
        this.birth = birth;
    }

    public boolean apply(boolean alive, int neighbors) {
        if (alive) {
            for (int n : survive) {
                if (n == neighbors) return true;
            }
            return false;
        } else {
            for (int n : birth) {
                if (n == neighbors) return true;
            }
            return false;
        }
    }
}