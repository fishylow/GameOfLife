import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainWindow extends JFrame {
    private GameBoard gameBoard;
    private GameLogic gameLogic;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private volatile boolean isRunning = false;
    private int simulationSpeed = 100;

    public MainWindow() {
        setTitle("Game of Life");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupDarkTheme();

        gameLogic = new GameLogic();
        gameBoard = new GameBoard(WIDTH - 100, HEIGHT - 50, gameLogic);

        setupMenuBar();
        setupLayout();

        Thread simulationThread = new Thread(this::runSimulation);
        simulationThread.start();
    }

    private void setupDarkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Panel.background", Color.BLACK);
            UIManager.put("Menu.background", Color.BLACK);
            UIManager.put("Menu.foreground", Color.WHITE);
            UIManager.put("MenuItem.background", Color.BLACK);
            UIManager.put("MenuItem.foreground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.WHITE);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveGame());

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> loadGame());

        JMenuItem clearItem = new JMenuItem("Clear Board");
        clearItem.addActionListener(e -> clearBoard());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();  // Add a separator line
        fileMenu.add(clearItem);


        // Settings Menu
        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setForeground(Color.WHITE);

        JMenu presetMenu = new JMenu("Presets");
        for (RuleSet preset : RuleSet.values()) {
            JMenuItem presetItem = new JMenuItem(preset.name());
            presetItem.addActionListener(e -> gameLogic.setRuleSet(preset));
            presetMenu.add(presetItem);
        }

        JMenuItem customRuleItem = new JMenuItem("Custom Rules...");
        customRuleItem.addActionListener(e -> showCustomRuleDialog());

        settingsMenu.add(presetMenu);
        settingsMenu.add(customRuleItem);

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);
    }

    private void showCustomRuleDialog() {
        JDialog dialog = new JDialog(this, "Custom Rules", true);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField surviveField = new JTextField();
        JTextField birthField = new JTextField();

        panel.add(new JLabel("Survive (comma-separated numbers):"));
        panel.add(surviveField);
        panel.add(new JLabel("Birth (comma-separated numbers):"));
        panel.add(birthField);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            try {
                int[] survive = parseNumbers(surviveField.getText());
                int[] birth = parseNumbers(birthField.getText());
                gameLogic.setCustomRuleSet(survive, birth);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter comma-separated numbers.");
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(applyButton, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private int[] parseNumbers(String input) {
        String[] parts = input.split(",");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            numbers[i] = Integer.parseInt(parts[i].trim());
        }
        return numbers;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(gameBoard, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.BLACK);
        controlPanel.setLayout(new BorderLayout());

        // Speed slider
        JSlider speedSlider = new JSlider(JSlider.VERTICAL, 1, 200, 100);
        speedSlider.setBackground(Color.BLACK);
        speedSlider.setForeground(Color.WHITE);
        speedSlider.addChangeListener(e -> simulationSpeed = 201 - speedSlider.getValue());

        JLabel speedLabel = new JLabel("Speed");
        speedLabel.setForeground(Color.WHITE);

        // Start/Stop button
        JButton startStopButton = new JButton("Start/Stop");
        startStopButton.addActionListener(e -> {
            isRunning = !isRunning;
            startStopButton.setText(isRunning ? "Stop" : "Start");
        });

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBackground(Color.BLACK);
        sliderPanel.add(speedLabel, BorderLayout.NORTH);
        sliderPanel.add(speedSlider, BorderLayout.CENTER);

        controlPanel.add(sliderPanel, BorderLayout.CENTER);
        controlPanel.add(startStopButton, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.EAST);
    }

    private void clearBoard() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear the board?",
                "Clear Board",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            gameLogic.clearCells();
            gameBoard.repaint();
        }
    }

    private void runSimulation() {
        while (true) {
            if (isRunning) {
                gameLogic.updateState();
                gameBoard.repaint();
                try {
                    Thread.sleep(simulationSpeed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("gamestate.ser"))) {
            GameState state = new GameState(gameLogic.getCells(), gameLogic.getRuleSet());
            out.writeObject(state);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + e.getMessage());
        }
    }

    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("gamestate.ser"))) {
            GameState state = (GameState) in.readObject();
            gameLogic.setCells(state.getCells());
            gameLogic.setRuleSet(state.getRuleSet());
            gameBoard.repaint();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow game = new MainWindow();
            game.setVisible(true);
        });
    }
}