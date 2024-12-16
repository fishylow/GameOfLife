import java.io.*;
public enum RuleSet implements Serializable {
    CONWAY((alive, neighbors) ->
            alive ? (neighbors == 2 || neighbors == 3) : (neighbors == 3)),
    HIGHLIFE((alive, neighbors) ->
            alive ? (neighbors == 2 || neighbors == 3) : (neighbors == 3 || neighbors == 6)),
    DAYNIGHT((alive, neighbors) ->
            alive ? (neighbors == 3 || neighbors == 4 || neighbors == 6 || neighbors == 7 || neighbors == 8)
                    : (neighbors == 3 || neighbors == 6 || neighbors == 7 || neighbors == 8));

    private final RuleFunction rule;

    RuleSet(RuleFunction rule) {
        this.rule = rule;
    }

    public boolean apply(boolean alive, int neighbors) {
        return rule.apply(alive, neighbors);
    }
}