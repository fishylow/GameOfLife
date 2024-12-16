import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class GameBoard extends JPanel {
    private final GameLogic gameLogic;
    private final int cellSize = 10;
    private final int width;
    private final int height;
    private boolean isDrawing = false;
    private boolean isErasing = false;

    // Zoom and pan variables
    private double zoom = 1.0;
    private double panX = 0;
    private double panY = 0;
    private Point lastMousePos;
    private boolean isPanning = false;

    public GameBoard(int width, int height, GameLogic gameLogic) {
        this.width = width;
        this.height = height;
        this.gameLogic = gameLogic;
        setBackground(Color.BLACK);
        setupMouseListeners();
    }

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isDrawing = true;
                    handleDraw(e);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    isErasing = true;
                    handleDraw(e);
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    isPanning = true;
                    lastMousePos = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isDrawing = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    isErasing = false;
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    isPanning = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPanning) {
                    double dx = e.getX() - lastMousePos.x;
                    double dy = e.getY() - lastMousePos.y;
                    panX += dx;
                    panY += dy;
                    lastMousePos = e.getPoint();
                    repaint();
                } else if (isDrawing || isErasing) {
                    handleDraw(e);
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        // Add mouse wheel listener for zooming
        addMouseWheelListener(e -> {
            double factor = e.getWheelRotation() < 0 ? 1.1 : 0.9;
            Point mouse = e.getPoint();

            // Adjust zoom and pan to keep the mouse position fixed
            double newZoom = zoom * factor;
            if (newZoom >= 0.1 && newZoom <= 10.0) {
                double scale = factor - 1;
                panX -= (mouse.x - panX) * scale;
                panY -= (mouse.y - panY) * scale;
                zoom = newZoom;
                repaint();
            }
        });
    }

    private void handleDraw(MouseEvent e) {
        // Convert screen coordinates to grid coordinates
        Point gridPoint = screenToGrid(e.getPoint());
        int gridX = gridPoint.x;
        int gridY = gridPoint.y;

        if (gridX >= 0 && gridX < gameLogic.getCols() &&
                gridY >= 0 && gridY < gameLogic.getRows()) {
            gameLogic.setCell(gridX, gridY, isDrawing);
            repaint();
        }
    }

    Point screenToGrid(Point screenPoint) {
        // Convert screen coordinates to grid coordinates
        double gridX = (screenPoint.x - panX) / (zoom * cellSize);
        double gridY = (screenPoint.y - panY) / (zoom * cellSize);
        return new Point((int)gridX, (int)gridY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply zoom and pan transformation
        AffineTransform transform = new AffineTransform();
        transform.translate(panX, panY);
        transform.scale(zoom, zoom);
        g2d.transform(transform);

        // Draw cells
        g2d.setColor(Color.GREEN);
        Rectangle clipBounds = g2d.getClipBounds();
        int startX = Math.max(0, (int)(clipBounds.x / cellSize));
        int startY = Math.max(0, (int)(clipBounds.y / cellSize));
        int endX = Math.min(gameLogic.getCols(), (int)((clipBounds.x + clipBounds.width) / cellSize) + 1);
        int endY = Math.min(gameLogic.getRows(), (int)((clipBounds.y + clipBounds.height) / cellSize) + 1);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (gameLogic.getCell(x, y)) {
                    g2d.fillRect(x * cellSize, y * cellSize, cellSize-1, cellSize-1);
                }
            }
        }
    }
}