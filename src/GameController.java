public class GameController implements Runnable, CellListener {
    private Grid modelGrid;
    private GameView gameView;
    private Cell tmpCell;
    private boolean setFlag;

    private boolean run;
    private int UPDATES_PER_SECOND = 1;

    public GameController(Grid model, GameView gameView) {
        this.modelGrid = model;
        this.gameView = gameView;
        modelGrid.setupGrid(10, 10);
        gameView.updateGrid(modelGrid.getGrid());
        gameView.setCellListener(this);
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

    @Override
    public void CellPressed(Cell cell) {
        if (cell == null)
            cell = tmpCell;
        cell.toggle();
        tmpCell = cell;
        setFlag = cell.isAlive();
        gameView.repaint();
    }

    @Override
    public void CellDragged(Cell cell) {
        if (cell == null)
            cell = tmpCell;
        if (cell != tmpCell) {
            tmpCell = cell;
            if (setFlag)
                cell.setAlive(true);
            else
                cell.setAlive(false);
            gameView.repaint();
        }
    }

    @Override
    public void CellReleased(Cell cell) {
        tmpCell = null;
    }
}
