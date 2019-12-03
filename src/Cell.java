public class Cell {
    private int x, y;
    private boolean isAlive;
    private boolean isHighlighted;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        isAlive = false;
        isHighlighted = false;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public void toggle() {
        isAlive = !isAlive;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
