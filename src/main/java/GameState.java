import java.io.*;
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private final boolean[][] cells;
    private final RuleSet ruleset;

    public GameState(boolean[][] cells, RuleSet ruleset) {
        this.cells = cells;
        this.ruleset = ruleset;
    }

    public boolean[][] getCells() {
        return cells;
    }

    public RuleSet getRuleSet() {
        return ruleset;
    }
}