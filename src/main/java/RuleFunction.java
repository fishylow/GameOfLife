import java.io.*;
@FunctionalInterface
interface RuleFunction extends Serializable {
    boolean apply(boolean alive, int neighbors);
}