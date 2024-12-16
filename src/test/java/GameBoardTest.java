import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;

public class GameBoardTest {
    private GameBoard gameBoard;
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
        gameBoard = new GameBoard(800, 600, gameLogic);
    }

    @Test
    void testInitialZoomAndPan() {
        // Test that the board starts with default zoom and pan
        Point gridPoint = gameBoard.screenToGrid(new Point(400, 300));
        assertEquals(40, gridPoint.x, "Initial X coordinate conversion should match");
        assertEquals(30, gridPoint.y, "Initial Y coordinate conversion should match");
    }
}