package controller;

import model.PlayerContainer;
import view.GridPanel;
import view.MainFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GridPanelMouseListener implements MouseListener, MouseMotionListener {

    private PlayerContainer playerContainer;
    private int currentShipSize;
    private MainFrame mainFrame;

    public GridPanelMouseListener() {
        playerContainer = PlayerContainer.getInstance();
        mainFrame = MainFrame.getInstance();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GridPanel component = (GridPanel) e.getComponent();
        switch (playerContainer.getGameState()) {
            case SPACE_SHIP:
                if (component.chooseLastShip()) {
                    playerContainer.getCurrentPlayer().setShip(currentShipSize, component.getLastHighlightedShip());
                    MainFrame mainFrame = MainFrame.getInstance();
                    int shipsLeft = playerContainer.getCurrentPlayer().getNumberOfShipsToSet();
                    mainFrame.getSpaceShipsPanel().setShipsLeftLabel(shipsLeft);
                }
                break;
            case GAME:
                int rowNumber = component.getRowFromY(e.getY());
                int columnNumber = component.getColumnFromX(e.getX());
                if(component.isRectangleForShoot(rowNumber, columnNumber)){

                    playerContainer.shootEnemy(rowNumber, columnNumber);
                    if (playerContainer.isEnemyBeaten()) {
                        mainFrame.setFinishedPanel("Wygrałeś");
                        break;
                    }
                    component.shoot(rowNumber, columnNumber);

                    Point point = playerContainer.getEnemyShoot();
                    int enemyShootRow = (int) point.getY();
                    int enemyShootColumn = (int) point.getX();
                    playerContainer.shootMe(enemyShootRow, enemyShootColumn);

                    mainFrame = MainFrame.getInstance();
                    mainFrame.shootMe(enemyShootRow, enemyShootColumn);

                    int myShipsLeft = playerContainer.getCurrentPlayer().getNumberOfShips();
                    mainFrame.getMyShipPanel().setLabel(myShipsLeft);

                    int enemyShipsLeft = playerContainer.getEnemyPlayer().getNumberOfShips();
                    mainFrame.getEnemyShipPanel().setLabel(enemyShipsLeft);

                    if (playerContainer.isMeBeaten()) {
                        mainFrame.setFinishedPanel("Przegrałeś");
                    }
                }
                break;
        }

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GridPanel component = (GridPanel) e.getComponent();
        switch (playerContainer.getGameState()) {
            case SPACE_SHIP:
                currentShipSize = playerContainer.getCurrentPlayer().getShipSizeToSet();

                if (currentShipSize == 0) {
                    MainFrame mainFrame = MainFrame.getInstance();
                    mainFrame.getSpaceShipsPanel().setPlayButtonEnabled(true);
                }
                //component.highlightRectangle(e.getX(), e.getY());
                component.highlightShip(e.getX(), e.getY(), currentShipSize);
                break;
            case GAME:
                component.highlightRectangle(e.getX(), e.getY());
                break;
        }

    }
}
