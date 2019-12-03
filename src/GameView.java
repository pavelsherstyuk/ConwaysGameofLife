import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GameView extends JPanel implements MouseListener, MouseMotionListener {
    private Cell[][] grid;
    private int gWidth, gHeight;
    private Cell tmpCell;
    private boolean setFlag;

    public GameView(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void updateGrid(Cell[][] grid) {
        this.grid = grid;
        gWidth = grid.length;
        gHeight = grid[0].length;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int cellWidth = this.getWidth() / gWidth;
        int cellHeight = this.getHeight() / gHeight;
//        double cellWidth = (double)this.getWidth() / gWidth;
//        double cellHeight = (double)this.getHeight() / gHeight;

        Cell cell;
        for (int x = 0; x < gWidth; x++) {
            for (int y = 0; y < gHeight; y++) {
                cell = grid[x][y];
                if (cell.isAlive())
                    g.setColor(Color.BLACK);
                else
                    g.setColor(Color.WHITE);

                g.fillRect(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
//                g.fillRect((int)(cellWidth*x), (int)(cellHeight*y), (int)cellWidth, (int)cellHeight);
            }
        }
    }

    private Cell getCell(MouseEvent e) {
        int x = e.getX() * gWidth / this.getWidth();
        int y = e.getY() * gHeight / this.getHeight();
        if (x < 0 || x >= gWidth || y < 0 || y >= gHeight)
            return tmpCell;
        return grid[x][y];
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Cell cell = getCell(e);
        cell.toggle();
        tmpCell = cell;
        setFlag = cell.isAlive();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Cell cell = getCell(e);
        if (cell != tmpCell) {
            tmpCell = cell;
            if (setFlag)
                cell.setAlive(true);
            else
                cell.setAlive(false);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        tmpCell = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
