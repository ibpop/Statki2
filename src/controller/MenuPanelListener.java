package controller;

import model.ComputerPlayer;
import model.Player;
import model.PlayerContainer;
import view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import model.DataPacket;
import model.MyRectangleContainer;
import network.MessageListener;
import network.MessageSender;
import network.EnemyShipsListener;
import network.MyShipsSender;
import static javax.swing.JOptionPane.showMessageDialog;

public class MenuPanelListener implements ActionListener {

    private static MainFrame mainFrame;
    private static MenuPanelListener menuListener = new MenuPanelListener();
    private static PlayerContainer playerContainer;

    private MenuPanelListener() {

    }

    public static MenuPanelListener getInstance() {
        return menuListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (mainFrame == null) {
            mainFrame = MainFrame.getInstance();
        }

        if (playerContainer == null) {
            playerContainer = PlayerContainer.getInstance();
        }

        if (source instanceof JMenuItem) {
            String buttonLabel = ((JMenuItem) source).getName();

            if (buttonLabel.equals("MENU_CLOSE")) {
                System.exit(0);
            }
        }

        if (source instanceof JButton) {
            String buttonLabel = ((JButton) source).getName();
            //zmienic na enum !!! i wrzucic do model

            switch (buttonLabel) {
                case "NEW_GAME":
                    mainFrame.setPlayerPanel();
                    break;
                case "SPACE_SHIPS":
                    mainFrame.setSpaceShipsPanel();
                    try {
                        String playerName = mainFrame.getPlayerName();
                        playerContainer.addPlayer(new Player(playerName));
                    } catch (NoNameException ex) {
                        showMessageDialog(null, "Brak nazwy użytkownika. Aplikacja zostanie zamknięta");
                        System.exit(0);
                    }

                    PlayerContainer.GameMode gameMode = mainFrame.getGameMode();
                    int shipsLeft = playerContainer.getCurrentPlayer().getNumberOfShipsToSet();
                    mainFrame.getSpaceShipsPanel().setShipsLeftLabel(shipsLeft);

                    switch (gameMode) {
                        case VS_COMPUTER:
                            playerContainer.addPlayer(new ComputerPlayer("komputer"));
                            break;
                        case VS_HUMAN:
                            playerContainer.addPlayer(new Player(""));
                            mainFrame.getSpaceShipsPanel().setNetworkPanel();
                            break;
                    }

                    playerContainer.setGameState(PlayerContainer.GameState.SPACE_SHIP);
                    break;
                case "PLAY":
                    mainFrame.setGamePanel();
                    playerContainer.setMyShips(mainFrame.getMySpaceShipRectangles());
                    playerContainer.setGameState(PlayerContainer.GameState.GAME);

                    int myShipsLeft = playerContainer.getCurrentPlayer().getNumberOfShips();
                    mainFrame.getMyShipPanel().setLabel(myShipsLeft);

                    switch (mainFrame.getGameMode()) {
                        case VS_COMPUTER:
                            playerContainer.initEnemyShip();
                            mainFrame.hideEnemyShips();
                            int enemyShipsLeft = playerContainer.getEnemyPlayer().getNumberOfShips();
                            mainFrame.getEnemyShipPanel().setLabel(enemyShipsLeft);
                            break;
                        case VS_HUMAN:
                            try {
                                MessageSender.init(InetAddress.getByName((mainFrame.getSpaceShipsPanel().getIp())), mainFrame.getSpaceShipsPanel().getPortSend());
                                MessageListener.init(mainFrame.getSpaceShipsPanel().getPortReceive());
                                mainFrame.setMessageSender(MessageSender.getInstance());
                                mainFrame.setMessageListener(MessageListener.getInstance());

                                mainFrame.getMessageSender().send(new DataPacket(PlayerContainer.PlayerType.ME, -1, -1, -1));
                                mainFrame.setTurn(PlayerContainer.PlayerType.ENEMY);
                                mainFrame.getMessageListener().listen();

                                //TODO: wyslanie info o swoich statkach i pobranie od przeciwnika
//                                MyShipsSender.init(InetAddress.getByName((mainFrame.getSpaceShipsPanel().getIp())), mainFrame.getSpaceShipsPanel().getPortSend()+2);
//                                EnemyShipsListener.init(mainFrame.getSpaceShipsPanel().getPortReceive()+2);
//                                
//                                mainFrame.setMyShipsSender(MyShipsSender.getInstance());
//                                mainFrame.setEnemyShipsListener(EnemyShipsListener.getInstance());
//                                
//                                MyRectangleContainer myShips = mainFrame.getMySpaceShipRectangles();
//                                mainFrame.getMyShipsSender().sendMyShips(myShips);
                                // mainFrame.getEnemyShipsListener().listenForShips();
                                mainFrame.getEnemyShipPanel().setLabel(5);
                            } catch (NumberFormatException ex) {
                                Logger.getLogger(MenuPanelListener.class.getName()).log(Level.SEVERE, null, ex);
                                showMessageDialog(null, "Wpisałeś zły port! Aplikacja zostanie zamknięta.");
                                System.exit(0);
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(MenuPanelListener.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(MenuPanelListener.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                    }
                    break;
                case "EXIT":
                    System.exit(0);
                    break;
                default:
                    break;
            }

        }
    }
}
