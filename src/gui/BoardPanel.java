package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Transient;
import java.util.Random;

/**
 * Created by pawel on 07.05.16.
 */
public class BoardPanel extends JPanel implements MouseListener {

    private static final Random rnd = new Random();
    private static final int CELLSIZE = 5;
    private static final Color COLOR = Color.BLUE;
    private int[][] grid;
    private int generationCounter;
    private int width, height;
    private int selectedIndex = 0;
    private int[][] secondGrid;
    private boolean period = false;

    public BoardPanel(int width, int height) {
        this.addMouseListener(this);
        this.width = width;
        this.height = height;
        clearAll();
    }

    public void clearAll() {
        this.grid = new int[this.width / CELLSIZE][this.height / CELLSIZE];
//        setupGrid();
        generationCounter = 0;
        repaint();
    }

    private void setupGrid() {
        for (int[] row : grid) {
            for (int j = 0; j < row.length; j++) {
                if (rnd.nextDouble() < 0.92)
                    continue;
                row[j] = rnd.nextInt(2);
            }
        }
    }

    public void addLife(int x, int y) {

        switch (selectedIndex) {
            case 0:
                grid[x][y] = 1;
                break;

            case 1:
                grid[x][y] = 1;
                grid[x][y + 1] = 1;
                grid[x][y + 2] = 1;
                grid[x + 1][y] = 1;
                grid[x + 2][y + 1] = 1;
                break;

            case 2:
                grid[x - 1][y] = 1;
                grid[x + 1][y] = 1;
                grid[x][y + 1] = 1;
                grid[x][y - 1] = 1;
                break;

            case 3:
                grid[x][y] = 1;
                grid[x][y + 1] = 1;
                grid[x][y + 2] = 1;
                break;
        }

        repaint();

    }

    public void updateGrid() {

        secondGrid = cloneArray(grid);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                applyRule(i, j);
            }
        }

        grid = cloneArray(secondGrid);
    }

    public int[][] cloneArray(int[][] toCLone) {
        int[][] clone = new int[toCLone.length][];
        for (int i = 0; i < toCLone.length; i++)
            clone[i] = toCLone[i].clone();

        return clone;
    }


    private void applyRule(int i, int j) {
        int left = 0, right = 0, up = 0, down = 0;
        int dUpperLeft = 0, dUpperRight = 0, dLowerLeft = 0, dLowerRight = 0;

        if (j < grid.length - 1) {
            right = grid[i][j + 1];
            if (i > 0)
                dUpperRight = grid[i - 1][j + 1];
            if (i < grid.length - 1)
                dLowerRight = grid[i + 1][j + 1];
        }

        if (j > 0) {
            left = grid[i][j - 1];
            if (i > 0)
                dUpperLeft = grid[i - 1][j - 1];
            if (i < grid.length - 1)
                dLowerLeft = grid[i + 1][j - 1];
        }

        if (i > 0)
            up = grid[i - 1][j];
        if (i < grid.length - 1)
            down = grid[i + 1][j];


        int sum = left + right + up + down + dUpperLeft + dUpperRight
                + dLowerLeft
                + dLowerRight;
        if (period) {
            int tmpSum = calculatePeriod(i, j);
            if(tmpSum!=0)
                sum =  tmpSum;
        }

        if (grid[i][j] == 1) {
            if (sum < 2)
                secondGrid[i][j] = 0;
            if (sum > 3)
                secondGrid[i][j] = 0;
        } else {
            if (sum == 3)
                secondGrid[i][j] = 1;
        }

    }

    private int calculatePeriod(int i, int j) {
        int addToSum = 0;


        //We break operation if (i,j) isn't on a border
        if (!((i == 0 || i == grid.length - 1) || (j == 0 || j == grid.length - 1)))
            return addToSum;

        int n, m;

        for (int k = i - 1; k < i + 2; k++) {
            for (int l = j - 1; l < j + 2; l++) {
                if (k == i && l == j)
                    continue;

                n = k;
                m = l;

                if (k < 0)
                    n = grid.length - 1;

                if (k > grid.length - 1)
                    n = 0;

                if (l < 0)
                    m = grid[0].length - 1;

                if (l > grid[0].length - 1)
                    m = 0;

                addToSum += grid[n][m];

            }
        }
        if (addToSum != 0)
            System.out.println("Period : " + addToSum);
        return addToSum;
    }

    @Override
    @Transient
    public Dimension getPreferredSize() {
        return new Dimension(grid.length * CELLSIZE, grid[0].length * CELLSIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color gColor = g.getColor();

        g.drawString("Pokolenie: " + generationCounter++, 0, 10);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    g.setColor(COLOR);
                    g.fillRect(j * CELLSIZE, i * CELLSIZE, CELLSIZE, CELLSIZE);
                }
            }
        }

        g.setColor(gColor);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println(e.getX() + " " + e.getY());
        int x = e.getY() / CELLSIZE;
        int y = e.getX() / CELLSIZE;

        addLife(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public boolean isPeriod() {
        return period;
    }

    public void setPeriod(boolean period) {
        this.period = period;
    }
}
