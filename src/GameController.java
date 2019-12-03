public class GameController implements Runnable {
    private Grid modelGrid;
    private GameView gameView;

    private boolean run;
    private int UPDATES_PER_SECOND = 1;

    public GameController(Grid model, GameView gameView) {
        this.modelGrid = model;
        this.gameView = gameView;
        modelGrid.setupGrid(10, 10);
        gameView.updateGrid(modelGrid.getGrid());
        run = false;
    }

    public int getGridWidth() {
        return modelGrid.getWidth();
    }

    public int getGridHeight() {
        return modelGrid.getHeight();
    }

    public void resizeGrid(int width, int height) {
        modelGrid.resize(width, height);
        gameView.updateGrid(modelGrid.getGrid());
    }

    public void setThresholds(int lowBirth, int highBirth, int lowSurvival, int highSurvival) {
        modelGrid.setThresholds(lowBirth, highBirth, lowSurvival, highSurvival);
    }

    public void setTorus(boolean torus) {
        modelGrid.setTorus(torus);
    }

    public void setUPS(int updatesPerSecond) {
        UPDATES_PER_SECOND = updatesPerSecond;
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        while (run) {
            if (System.currentTimeMillis() - timer >= 1000/UPDATES_PER_SECOND) {
                timer = System.currentTimeMillis();
                modelGrid.updateGrid();
                gameView.updateGrid(modelGrid.getGrid());
            }
        }

        Thread.currentThread().interrupt();
    }

    public void start() {
        run = true;
    }

    public void step() {
        stop();
        modelGrid.updateGrid();
        gameView.updateGrid(modelGrid.getGrid());
    }

    public void stop() {
        run = false;
    }

    public void reset() {
        modelGrid.resetGrid();
        gameView.repaint();
    }

    public void random() {
        modelGrid.randomGrid();
        gameView.repaint();
    }
}
