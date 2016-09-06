package view;



import controller.GridPanelMouseListener;
import model.MyRectangle;
import model.MyRectangleContainer;
import model.Ship;
import model.ShipPosition;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {
    private final static int COLUMN_NUMBER = 10;
    private final static int ROW_NUMBER = 10;
    private MyRectangleContainer cells;
    private GridPanelMouseListener gridPanelMouseListener;
    private int width;
    private int height;
    private int rowHeight;
    private int rowWidth;
    private int xOffset;
    private int yOffset;

    private static ShipPosition shipPosition = ShipPosition.VERTICAL;
    private int lastMouseRowNumber, lastMouseColumnNumber;

    public GridPanel() {
        super();
        cells = new MyRectangleContainer(ROW_NUMBER, COLUMN_NUMBER);

        gridPanelMouseListener = new GridPanelMouseListener();

        addMouseListener(gridPanelMouseListener);
        addMouseMotionListener(gridPanelMouseListener);

        //mAllPoints = new  MyPointContainer();
        //setBackground(Color.BLACK);
    }

    public void resetPoints() {
        //mAllPoints.clear();
        repaint();
    }

    @Override
    public void invalidate() {
        //cells.clear();
        initCells();
        //selectedCell = null;
        super.invalidate();
    }

    private void initCells() {
        width = getWidth() - 5; //mWidth;
        height = getHeight() - 5; //mHeight;

        rowHeight = height / ROW_NUMBER;
        rowWidth = width / COLUMN_NUMBER;

        xOffset = (width - (COLUMN_NUMBER * rowWidth)) / 2;
        yOffset = (height - (ROW_NUMBER * rowHeight)) / 2;
        int counter = 0;

        boolean init = cells.isInited();

        for (int j = 0; j < ROW_NUMBER; j++) {

            for (int i = 0; i < COLUMN_NUMBER; i++) {
                MyRectangle cell = new MyRectangle(
                        xOffset + (i * rowWidth),
                        yOffset + (j * rowHeight),
                        rowWidth,
                        rowHeight,
                        j,
                        i
                );

                if (!init) {
                    cells.addRectangle(j, i, cell);
                } else {
                    cells.getRectangle(j, i).setBounds(cell);
                    counter++;
                }
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        for (int i = 0; i < ROW_NUMBER; i++) {
            for (int j = 0; j < COLUMN_NUMBER; j++) {

                MyRectangle cell = cells.getRectangle(i, j);
                g2d.setColor(cell.getColor());
                g2d.fillRect((int) cell.getX(), (int) cell.getY(), (int) cell.getWidth(), (int) cell.getHeight());

                g2d.setColor(Color.BLACK);
                g2d.draw(cell);

            }
        }

        g2d.dispose();

    }

    public static int getColumnNumber() {
        return COLUMN_NUMBER;
    }

    public static int getRowNumber() {
        return ROW_NUMBER;
    }

    public void highlightRectangle(int x, int y) {
        int rowNumber = getRowFromY(y);
        int columnNumber = getColumnFromX(x);

        if (rowNumber != lastMouseRowNumber || columnNumber != lastMouseColumnNumber) {
            lastMouseRowNumber = rowNumber;
            lastMouseColumnNumber = columnNumber;
            cells.highlightMyRectangle(rowNumber, columnNumber);

            this.repaint();
        }
    }

    public void highlightShip(int x, int y, int size) {
        int rowNumber = getRowFromY(y);
        int columnNumber = getColumnFromX(x);

        if (rowNumber != lastMouseRowNumber || columnNumber != lastMouseColumnNumber) {
            lastMouseRowNumber = rowNumber;
            lastMouseColumnNumber = columnNumber;
            cells.highlightShip(rowNumber, columnNumber, size, shipPosition, true);

            this.repaint();
        }

    }

    public boolean chooseLastShip() {
        return cells.chooseLastShip();
    }


    public static void setShipPosition(ShipPosition shipPosition) {
        GridPanel.shipPosition = shipPosition;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public int getRowWidth() {
        return rowWidth;
    }

    public int getRowFromY(int y) {
        int rowNumber = y / rowHeight;

        if (rowNumber >= this.ROW_NUMBER)
            rowNumber = this.ROW_NUMBER - 1;

        return rowNumber;
    }

    public int getColumnFromX(int x) {
        int columnNumber = x / rowWidth;

        if (columnNumber >= this.COLUMN_NUMBER)
            columnNumber = this.COLUMN_NUMBER - 1;

        return columnNumber;
    }

    public void setCells(MyRectangleContainer cells) {
        this.cells = cells;
    }

    public MyRectangleContainer getCells() {
        return cells;
    }

    public void hideShips() {
        cells.hideShips();
    }

    public void shoot(int rowNumber, int columnNumber){
        cells.shoot(rowNumber, columnNumber);
        repaint();
    }

    public Ship getLastHighlightedShip() {
        return cells.getLastHighlightedShip();
    }

    public boolean isRectangleForShoot(int rowNumber, int columnNumber){
        return cells.isRectangleForShoot(rowNumber, columnNumber);
    }
}
