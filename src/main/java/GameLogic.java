public class GameLogic {
    private boolean[][] cells;
    private RuleSet currentRuleSet;
    private final int rows = 1000;
    private final int cols = 1000;
    private CustomRuleSet customRuleSet;

    public GameLogic() {
        cells = new boolean[rows][cols];
        currentRuleSet = RuleSet.CONWAY;
    }

    public void clearCells() {
        cells = new boolean[rows][cols];
    }

    public void setCustomRuleSet(int[] survive, int[] birth) {
        customRuleSet = new CustomRuleSet(survive, birth);
        currentRuleSet = null;  // Indicates we're using custom rules
    }

    public void updateState() {
        boolean[][] newCells = new boolean[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int neighbors = countNeighbors(x, y);
                if (currentRuleSet != null) {
                    newCells[y][x] = currentRuleSet.apply(cells[y][x], neighbors);
                } else {
                    newCells[y][x] = customRuleSet.apply(cells[y][x], neighbors);
                }
            }
        }

        cells = newCells;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue;

                int nx = (x + dx + cols) % cols;
                int ny = (y + dy + rows) % rows;

                if (cells[ny][nx]) count++;
            }
        }
        return count;
    }

    public void setCell(int x, int y, boolean state) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            cells[y][x] = state;
        }
    }

    public boolean getCell(int x, int y) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            return cells[y][x];
        }
        return false;
    }

    public boolean[][] getCells() {
        return cells;
    }

    public void setCells(boolean[][] newCells) {
        if (newCells != null && newCells.length == rows && newCells[0].length == cols) {
            cells = newCells;
        }
    }

    public RuleSet getRuleSet() {
        return currentRuleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.currentRuleSet = ruleSet;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}