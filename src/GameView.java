import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GameView extends JPanel implements MouseListener, MouseMotionListener {
    private Cell[][] grid;
    private int gWidth, gHeight;
    private CellListener cellListener;

    public GameView(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void setCellListener(CellListener l) {
        cellListener = l;
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

        double cellWidth = (double)this.getWidth() / gWidth;
        double cellHeight = (double)this.getHeight() / gHeight;

        Cell cell;
        for (int x = 0; x < gWidth; x++) {
            for (int y = 0; y < gHeight; y++) {
                cell = grid[x][y];
                if (cell.isAlive())
                    g.setColor(Color.BLACK);
                else
                    g.setColor(Color.WHITE);

                g.fillRect((int)(cellWidth*x + 0.5), (int)(cellHeight*y + 0.5),
                        (int)(cellWidth + 0.5), (int)(cellHeight + 0.5));
            }
        }
    }

    private Cell getCell(MouseEvent e) {
        int x = e.getX() * gWidth / this.getWidth();
        int y = e.getY() * gHeight / this.getHeight();
        if (x < 0 || x >= gWidth || y < 0 || y >= gHeight)
            return null;
        return grid[x][y];
    }

    @Override
    public void mousePressed(MouseEvent e) {
        cellListener.CellPressed(getCell(e));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        cellListener.CellDragged(getCell(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        cellListener.CellReleased(getCell(e));
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
