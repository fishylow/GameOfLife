import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
    @Test
    void testGameStateSerialization() {
        // Create a test pattern
        boolean[][] cells = new boolean[3][3];
        cells[1][1] = true;

        // Create GameState
        GameState state = new GameState(cells, RuleSet.CONWAY);

        // Test getters
        assertArrayEquals(cells, state.getCells(), "Cells array should match");
        assertEquals(RuleSet.CONWAY, state.getRuleSet(), "RuleSet should match");
    }
}