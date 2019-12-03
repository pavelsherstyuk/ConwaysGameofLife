public class Grid {
    private int width, height;
    private Cell[][] grid;
    private int lowSurviveThreshold, highSurviveThreshold;
    private int lowBirthThreshold, highBirthThreshold;
    private double rndCutoff;
    private boolean torus;

    public void setupGrid(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }

        lowBirthThreshold = 3;
        highBirthThreshold = 3;
        lowSurviveThreshold = 2;
        highSurviveThreshold = 3;

        torus = false;
        rndCutoff = 0.5;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void resetGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].setAlive(false);
            }
        }
    }

    public void randomGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].setAlive(Math.random() < rndCutoff);
            }
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        Cell[][] newGrid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newGrid[x][y] = new Cell(x, y);
                if (x < grid.length && y < grid[0].length)
                    newGrid[x][y].setAlive(grid[x][y].isAlive());
                else
                    newGrid[x][y] = new Cell(x, y);
            }
        }
        grid = newGrid;
    }

    public void setThresholds(int lowBirth, int highBirth, int lowSurvival, int highSurvival) {
        lowBirthThreshold = lowBirth;
        highBirthThreshold = highBirth;
        lowSurviveThreshold = lowSurvival;
        highSurviveThreshold = highSurvival;
    }

    public void setTorus(boolean torus) {
        this.torus = torus;
    }

    public void updateGrid() {
        Cell[][] newGrid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newGrid[x][y] = new Cell(x, y);
                int aliveNeighbours = 0;
                //Check for neighbours
                // [8] [1] [2]
                // [7] [ ] [3]
                // [6] [5] [4]
                if (y > 0) {                            //[1]
                    if (grid[x][y - 1].isAlive())
                        aliveNeighbours++;
                } else if (torus && grid[x][height-1].isAlive())
                    aliveNeighbours++;

                if (x < width - 1 && y > 0) {           //[2]
                    if (grid[x + 1][y - 1].isAlive())
                        aliveNeighbours++;
                } else if (torus) {
                    int i = x + 1, j = y - 1;
                    if (x == width - 1)
                        i = 0;
                    if (y == 0)
                        j = height - 1;
                    if (grid[i][j].isAlive())
                        aliveNeighbours++;
                }

                if (x < width - 1) {                    //[3]
                    if (grid[x + 1][y].isAlive())
                        aliveNeighbours++;
                } else if (torus && grid[0][y].isAlive())
                    aliveNeighbours++;

                if (x < width - 1 && y < height - 1) {  //[4]
                    if (grid[x + 1][y + 1].isAlive())
                        aliveNeighbours++;
                } else if (torus) {
                    int i = x + 1, j = y + 1;
                    if (x == width - 1)
                        i = 0;
                    if (y == height - 1)
                        j = 0;
                    if (grid[i][j].isAlive())
                        aliveNeighbours++;
                }

                if (y < height - 1) {                   //[5]
                    if (grid[x][y + 1].isAlive())
                        aliveNeighbours++;
                } else if (torus && grid[x][0].isAlive())
                    aliveNeighbours++;

                if (x > 0 && y < height - 1) {          //[6]
                    if (grid[x - 1][y + 1].isAlive())
                        aliveNeighbours++;
                } else if (torus) {
                    int i = x - 1, j = y + 1;
                    if (x == 0)
                        i = width - 1;
                    if (y == height - 1)
                        j = 0;
                    if (grid[i][j].isAlive())
                        aliveNeighbours++;
                }

                if (x > 0) {                            //[7]
                    if (grid[x - 1][y].isAlive())
                        aliveNeighbours++;
                } else if (torus && grid[width-1][y].isAlive())
                    aliveNeighbours++;

                if (x > 0 && y > 0) {                   //[8]
                    if (grid[x - 1][y - 1].isAlive())
                        aliveNeighbours++;
                } else if (torus) {
                    int i = x - 1, j = y - 1;
                    if (x == 0)
                        i = width - 1;
                    if (y == 0)
                        j = height - 1;
                    if (grid[i][j].isAlive())
                        aliveNeighbours++;
                }

                // Interactions
//                System.out.println(grid[x][y] + " " + aliveNeighbours);
                if (aliveNeighbours >= lowSurviveThreshold &&
                        aliveNeighbours <= highSurviveThreshold) {
                    newGrid[x][y].setAlive(grid[x][y].isAlive());
//                    System.out.println(newGrid[x][y] + " survive");
                } else
                    newGrid[x][y].setAlive(false);

                if (aliveNeighbours >= lowBirthThreshold &&
                        aliveNeighbours <= highBirthThreshold) {
                    newGrid[x][y].setAlive(true);
//                    System.out.println(newGrid[x][y] + " birth");
                }
            }
        }
        grid = newGrid;
    }
}
