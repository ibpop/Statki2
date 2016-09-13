package controller;

import model.PlayerContainer;
import view.GridPanel;
import view.MainFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.DataPacket;
import model.MyRectangle;
import model.MyRectangleContainer;
import model.TimeoutException;
import network.MessageListener;

/**
 * Odpowiada za nasłuchiwanie działań wykonywanych
 * na panelu GridPanel.
 * @author blazej
 */

public class GridPanelMouseListener implements MouseListener, MouseMotionListener {

    private PlayerContainer playerContainer;
    private int currentShipSize;
    private MainFrame mainFrame;
    private boolean receiveEnemyMap = false;
    private boolean sendMyMap = false;

    public GridPanelMouseListener() {
        playerContainer = PlayerContainer.getInstance();
        mainFrame = MainFrame.getInstance();
    }

    /**
     * Służy do obsługiwania kliknięcia myszką.
     * Sprawdza jaki panel obecnie jest aktywny i wykonuje odpowiednie działania.
     * Tutaj wysyłane są wiadomości do przeciwnika. Wykonywane strzały, sprawdzane
     * warunki wygranej/przegranej.
     * @param e Event
     */
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
                int rowNumber = 0;
                int columnNumber = 0;
                if (mainFrame == null) {
                    mainFrame = MainFrame.getInstance();
                }
                switch (mainFrame.getGameMode()) {
                    case VS_COMPUTER:
                        rowNumber = component.getRowFromY(e.getY());
                        columnNumber = component.getColumnFromX(e.getX());
                        if (component.isRectangleForShoot(rowNumber, columnNumber)) {
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
                    case VS_HUMAN:
                         if (!mainFrame.isBothPlayersPresent() && System.currentTimeMillis() - mainFrame.getStarted() > 10000) {
                            try {
                                throw new TimeoutException();
                            } catch (TimeoutException ex) {
                                Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null,"Brak drugiego gracza. Aplikacja zostanie wyłączona.");
                                System.exit(0);
                            }
                        }
                        if (mainFrame.getTurn() == PlayerContainer.PlayerType.ENEMY) {
                            //serializacja
//                            if (!receiveEnemyMap) {
//                                MyRectangleContainer myShips = mainFrame.getMySpaceShipRectangles();
//                                
//                                try {
//                                    mainFrame.getMessageSender().sendMyShips(myShips);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(GridPanelMouseListener.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                                mainFrame.getMessageListener().listenForShips();
//                                receiveEnemyMap = true;
//                            }
                            //nie pozwol na wykonanie strzalu jak nie twoja kolej
                            //czyli nic nie rob
                        } else {
                            int myShipsLeft = playerContainer.getCurrentPlayer().getNumberOfShips();
                            mainFrame.getMyShipPanel().setLabel(myShipsLeft);
                            if (playerContainer.getCurrentPlayer().getNumberOfShips() == 0) {
                                mainFrame.setFinishedPanel("Przegrałeś");
                            }
                            rowNumber = component.getRowFromY(e.getY());
                            columnNumber = component.getColumnFromX(e.getX());
                            if (component.isRectangleForShoot(rowNumber, columnNumber)) {
                                //przy serializacji
//                                playerContainer.shootEnemy(rowNumber, columnNumber);
//                                if (playerContainer.isEnemyBeaten()) {
//                                    mainFrame.setFinishedPanel("Wygrałeś");
//                                    break;
//                                }
                                try {
                                    mainFrame.getMessageSender().send(new DataPacket(PlayerContainer.PlayerType.ME, rowNumber, columnNumber, playerContainer.getCurrentPlayer().getNumberOfShips()));
                                } catch (IOException ex) {
                                    Logger.getLogger(GridPanelMouseListener.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                component.shoot(rowNumber, columnNumber);
                                mainFrame.setTurn(PlayerContainer.PlayerType.ENEMY);
                            } 
                            
                        }
                        break;

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

    /**
     * Obsługuje ruchy myszy. W przypadku aktywnego panelu
     * ustawiania swoich statkow odpowiada za wyswietlanie odpowiedniego statku
     * do rozstawienia. Jeżeli rozstawiono wszystkie statki aktywuje przycisk
     * "Graj", dzięki czemu można rozpocząć rozgrywkę.
     * @param e Event
     */
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
