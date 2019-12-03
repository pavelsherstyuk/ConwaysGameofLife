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
        modelGrid.setupGrid(50, 50);
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
            // Line algorithm
            int x1 = tmpCell.getX();
            int x2 = cell.getX();
            int y1 = tmpCell.getY();
            int y2 = cell.getY();

            int d = 0;
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);

            int dx2 = 2 * dx; // slope scaling factors to
            int dy2 = 2 * dy; // avoid floating point

            int ix = x1 < x2 ? 1 : -1; // increment direction
            int iy = y1 < y2 ? 1 : -1;

            int x = x1;
            int y = y1;

            if (dx >= dy) {
                while (true) {
                    if (setFlag)
                        modelGrid.getGrid()[x][y].setAlive(true);
                    else
                        modelGrid.getGrid()[x][y].setAlive(false);

                    if (x == x2)
                        break;
                    x += ix;
                    d += dy2;
                    if (d > dx) {
                        y += iy;
                        d -= dx2;
                    }
                }
            } else {
                while (true) {
                    if (setFlag)
                        modelGrid.getGrid()[x][y].setAlive(true);
                    else
                        modelGrid.getGrid()[x][y].setAlive(false);

                    if (y == y2)
                        break;
                    y += iy;
                    d += dx2;
                    if (d > dy) {
                        x += ix;
                        d -= dy2;
                    }
                }
            }

            tmpCell = cell;
            gameView.repaint();
        }
    }

    @Override
    public void CellReleased(Cell cell) {
        tmpCell = null;
    }
}
