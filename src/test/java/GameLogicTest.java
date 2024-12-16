import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
    }

    @Test
    void testInitialState() {
        // Check that the board is empty on initialization
        for (int y = 0; y < gameLogic.getRows(); y++) {
            for (int x = 0; x < gameLogic.getCols(); x++) {
                assertFalse(gameLogic.getCell(x, y),
                        "Cell at " + x + "," + y + " should be dead initially");
            }
        }
    }

    @Test
    void testSetAndGetCell() {
        // Test setting and getting cell states
        gameLogic.setCell(5, 5, true);
        assertTrue(gameLogic.getCell(5, 5), "Cell should be alive after setting to true");

        gameLogic.setCell(5, 5, false);
        assertFalse(gameLogic.getCell(5, 5), "Cell should be dead after setting to false");
    }

    @Test
    void testClearCells() {
        // Set some cells alive
        gameLogic.setCell(1, 1, true);
        gameLogic.setCell(2, 2, true);
        gameLogic.setCell(3, 3, true);

        // Clear the board
        gameLogic.clearCells();

        // Check that all cells are dead
        for (int y = 0; y < gameLogic.getRows(); y++) {
            for (int x = 0; x < gameLogic.getCols(); x++) {
                assertFalse(gameLogic.getCell(x, y),
                        "Cell at " + x + "," + y + " should be dead after clearing");
            }
        }
    }

    @Test
    void testConwayRules() {
        // Test Conway's Game of Life rules
        gameLogic.setRuleSet(RuleSet.CONWAY);

        // Create a blinker pattern
        gameLogic.setCell(10, 10, true);
        gameLogic.setCell(11, 10, true);
        gameLogic.setCell(12, 10, true);

        // Update state
        gameLogic.updateState();

        // Check vertical blinker state
        assertTrue(gameLogic.getCell(11, 9), "Top cell of blinker should be alive");
        assertTrue(gameLogic.getCell(11, 10), "Middle cell of blinker should be alive");
        assertTrue(gameLogic.getCell(11, 11), "Bottom cell of blinker should be alive");
        assertFalse(gameLogic.getCell(10, 10), "Left cell should be dead");
        assertFalse(gameLogic.getCell(12, 10), "Right cell should be dead");
    }

    @Test
    void testCustomRules() {
        // Test custom ruleset (example: only 2 neighbors for survival and birth)
        int[] survive = {2};
        int[] birth = {2};
        gameLogic.setCustomRuleSet(survive, birth);

        // Create a simple pattern
        gameLogic.setCell(10, 10, true);
        gameLogic.setCell(11, 10, true);
        gameLogic.setCell(12, 10, true);

        // Update state
        gameLogic.updateState();

        // With these rules, only cells with exactly 2 neighbors survive or are born
        assertTrue(gameLogic.getCell(11, 10), "Middle cell should survive with 2 neighbors");
        assertFalse(gameLogic.getCell(10, 10), "Edge cells should die with only 1 neighbor");
        assertFalse(gameLogic.getCell(12, 10), "Edge cells should die with only 1 neighbor");
    }

    @Test
    void testBoundaryConditions() {
        // Test wrapping at boundaries
        gameLogic.setCell(0, 0, true);
        gameLogic.setCell(1, 0, true);
        gameLogic.setCell(gameLogic.getCols() - 1, 0, true);

        gameLogic.updateState();

        // The cell at (0,1) should be born due to wrapping
        assertTrue(gameLogic.getCell(0, 1),
                "Cell should be born due to wrapped neighbor");
    }
}