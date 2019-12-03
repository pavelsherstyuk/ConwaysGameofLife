import javax.swing.*;
import java.awt.*;

public class GameOfLife {
    private Thread gameThread;
    public GameOfLife() {
        JFrame frame = new JFrame("Conway's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Game panel
        Grid grid = new Grid();
        GameView gameView = new GameView(800, 800);
        frame.add(gameView, BorderLayout.NORTH);
        GameController gameController = new GameController(grid, gameView);

        // Control panel
        JDialog toolKit = new JDialog(frame, "Tools", true);
        JButton toolKitButton = new JButton("Tools");
        toolKitButton.addActionListener(e -> toolKit.setVisible(!toolKit.isVisible()));

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
        stepButton.addActionListener(e -> gameController.step());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> gameController.reset());

        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(e -> gameController.random());

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(startButton);
        controlPanel.add(upsSlider);
        controlPanel.add(stopButton);
        controlPanel.add(stepButton);
        controlPanel.add(toolKitButton);
        controlPanel.add(resetButton);
        controlPanel.add(randomButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Frame setup
        frame.pack();
        frame.setLocation((dim.width - frame.getWidth())/2,
                (dim.height - frame.getHeight())/2);
        frame.setVisible(true);

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
            gameController.resizeGrid(width, height);
            gameController.setThresholds(lowB, highB, lowS, highS);
            toolKit.setVisible(false);
        });
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> toolKit.setVisible(false));

        toolKit.add(resize); toolKit.add(resizeRules);
        toolKit.add(widthLabel); toolKit.add(widthInput);
        toolKit.add(heightLabel); toolKit.add(heightInput);
        toolKit.add(thresholds); toolKit.add(thresholdRules);
        toolKit.add(lowBirth); toolKit.add(lowBirthInput);
        toolKit.add(highBirth); toolKit.add(highBirthInput);
        toolKit.add(lowSurvival); toolKit.add(lowSurvivalInput);
        toolKit.add(highSurvival); toolKit.add(highSurvivalInput);
        toolKit.add(applyButton); toolKit.add(closeButton);

        toolKit.pack();
        toolKit.setLocationRelativeTo(frame);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }
}
