import javax.swing.*;
import java.awt.*;

public class GameOfLife {
    private Thread gameThread;
    public GameOfLife() {
        JFrame frame = new JFrame("Conway's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Game panel
        Grid grid = new Grid();
        GameView gameView = new GameView(
                (int)((double)dim.height * 0.9 / 100 + 0.5) * 100,
                (int)((double)dim.height * 0.9 / 100 + 0.5) * 100);
        frame.add(gameView, BorderLayout.NORTH);
        GameController gameController = new GameController(grid, gameView);

        // Control panel
        JSlider upsSlider = new JSlider(0, 99, 0);
        upsSlider.addChangeListener(e -> {
            int value = (upsSlider.getValue() + 1);
            gameController.setUPS(value);
        });
        upsSlider.setVisible(false);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            gameController.start();
            upsSlider.setVisible(true);
            if (gameThread == null) {
                gameThread = new Thread(gameController);
                gameThread.start();
            }
        });

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> {
            gameController.stop();
            upsSlider.setVisible(false);
            gameThread = null;
        });

        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(e -> {
            stopButton.doClick();
            gameController.step();
        });

        JDialog toolKit = new JDialog(frame, "Tools", true);
        JButton toolKitButton = new JButton("Tools");
        toolKitButton.addActionListener(e -> {
            stopButton.doClick();
            toolKit.setVisible(!toolKit.isVisible());
        });

        JToggleButton torusButton = new JToggleButton("Torus");
        torusButton.addActionListener(e -> gameController.setTorus(torusButton.isSelected()));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> gameController.reset());

        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(e -> gameController.random());

        JDialog about = new JDialog(frame, "About", true);
        JButton aboutButton = new JButton("About");
        aboutButton.addActionListener(e -> {
            stopButton.doClick();
            about.setVisible(true);
        });

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(startButton);
        controlPanel.add(upsSlider);
        controlPanel.add(stopButton);
        controlPanel.add(stepButton);
        controlPanel.add(torusButton);
        controlPanel.add(toolKitButton);
        controlPanel.add(resetButton);
        controlPanel.add(randomButton);
        controlPanel.add(aboutButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Frame setup
        frame.pack();
        frame.setLocation((dim.width - frame.getWidth())/2,
                (dim.height - frame.getHeight())/2);
        frame.setVisible(true);

        //About menu
        about.setResizable(false);
        about.setUndecorated(true);
        about.setAlwaysOnTop(true);
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.PAGE_AXIS));

        JTextPane aboutGame = new JTextPane();
        aboutGame.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        aboutGame.setEditable(false);
        aboutGame.setText("The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970.\nRules: (can be changed in toolKit menu)\n" +
                "Any live cell with fewer than two live neighbours dies, as if by underpopulation.\n" +
                "Any live cell with two or three live neighbours lives on to the next generation.\n" +
                "Any live cell with more than three live neighbours dies, as if by overpopulation.\n" +
                "Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.");
        JTextPane aboutGrid = new JTextPane();
        aboutGrid.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        aboutGrid.setEditable(false);
        aboutGrid.setText("[How to: set/remove cells]\n" +
                "White cell = dead\nBlack cell = alive\n" +
                "Click on a dead cell to set it alive and vice versa\n" +
                "Drag mouse starting from a dead cell to continuously set cells alive, from alive cell - to set dead");
        JTextPane aboutControls = new JTextPane();
        aboutControls.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        aboutControls.setEditable(false);
        aboutControls.setText("[How to: controls]\n" +
                "Start button: starts simulation. Using the appeared slider you may change the simulation speed, UPS (updates per second).\n    Range: from 1 (1 second delay) to 100 (10 milliseconds delay).\n" +
                "Stop button: pauses simulation, pressing start will continue.\n" +
                "Step button: pauses simulation and progresses by steps.\n" +
                "Torus toggle: wraps the borders around, creating closed space.\n" +
                "Reset button: sets all cells to dead state.\n" +
                "Random button: resets the grid and randomly sets alive cells with 50% chance.");
        JTextPane aboutToolKit = new JTextPane();
        aboutToolKit.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        aboutToolKit.setEditable(false);
        aboutToolKit.setText("[How to: Tools]\n" +
                "Tools allow user to manipulate the grid.\n" +
                "First option is resizing the grid without resetting the it (previous state is saved).\n" +
                "Second option is changing birth/survival constants (See game description).");

        JButton aboutCloseButton = new JButton("Close");
        aboutCloseButton.setSize(100, 20);
        aboutCloseButton.addActionListener(e -> about.setVisible(false));

        aboutPanel.add(aboutGame);
        aboutPanel.add(aboutGrid);
        aboutPanel.add(aboutControls);
        aboutPanel.add(aboutToolKit);
        aboutPanel.add(aboutCloseButton);

        about.add(aboutPanel);
        about.pack();
        about.setLocationRelativeTo(frame);

        //Toolkit menu
        toolKit.setResizable(false);
        toolKit.setUndecorated(true);
        toolKit.setAlwaysOnTop(true);
        toolKit.setLayout(new GridLayout(9,2));

        JLabel resize = new JLabel("Resize grid");
        JLabel resizeRules = new JLabel("[10-500]");
        JLabel widthLabel = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");
        JTextField widthInput = new JTextField(String.valueOf(gameController.getGridWidth()));
        JTextField heightInput = new JTextField(String.valueOf(gameController.getGridHeight()));

        JLabel thresholds = new JLabel("Thresholds");
        JLabel thresholdRules = new JLabel("[no range]");
        JLabel lowBirth = new JLabel("Low birth");
        JLabel highBirth = new JLabel("High birth");
        JLabel lowSurvival = new JLabel("Low survival");
        JLabel highSurvival = new JLabel("High survival");
        JTextField lowBirthInput = new JTextField("3");
        JTextField highBirthInput = new JTextField("3");
        JTextField lowSurvivalInput = new JTextField("2");
        JTextField highSurvivalInput = new JTextField("3");

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            String widthString = widthInput.getText();
            String heightString = heightInput.getText();
            int width = -1, height = -1;
            int lowB = -1, highB = -1;
            int lowS = -1, highS = -1;
            try {
                width = Integer.parseInt(widthString);
                height = Integer.parseInt(heightString);
                if (width < 10 || width > 500 || height < 0 || height > 500)
                    throw new IllegalArgumentException("Incorrect input");
                lowB = Integer.parseInt(lowBirthInput.getText());
                highB = Integer.parseInt(highBirthInput.getText());
                lowS = Integer.parseInt(lowSurvivalInput.getText());
                highS = Integer.parseInt(highSurvivalInput.getText());
            } catch (Exception ignore) {
                System.err.println("Incorrect input");
            }
            gameController.stop();
            upsSlider.setVisible(false);
            gameThread = null;
            gameController.resizeGrid(width, height);
            gameController.setThresholds(lowB, highB, lowS, highS);
            toolKit.setVisible(false);
        });
        JButton toolKitCloseButton = new JButton("Close");
        toolKitCloseButton.addActionListener(e -> toolKit.setVisible(false));

        toolKit.add(resize); toolKit.add(resizeRules);
        toolKit.add(widthLabel); toolKit.add(widthInput);
        toolKit.add(heightLabel); toolKit.add(heightInput);
        toolKit.add(thresholds); toolKit.add(thresholdRules);
        toolKit.add(lowBirth); toolKit.add(lowBirthInput);
        toolKit.add(highBirth); toolKit.add(highBirthInput);
        toolKit.add(lowSurvival); toolKit.add(lowSurvivalInput);
        toolKit.add(highSurvival); toolKit.add(highSurvivalInput);
        toolKit.add(applyButton); toolKit.add(toolKitCloseButton);

        toolKit.pack();
        toolKit.setLocationRelativeTo(frame);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }
}
