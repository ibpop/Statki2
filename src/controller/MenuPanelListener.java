package controller;

import model.ComputerPlayer;
import model.Player;
import model.PlayerContainer;
import view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanelListener implements ActionListener{
    private static MainFrame mainFrame;
    private static MenuPanelListener menuListener =  new MenuPanelListener();
    private static PlayerContainer playerContainer;

    private MenuPanelListener(){

    }

    public static MenuPanelListener getInstance(){
        return menuListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(mainFrame == null) {
            mainFrame = MainFrame.getInstance();
        }

        if(playerContainer == null){
            playerContainer = PlayerContainer.getInstance();
        }
        
        if(source instanceof JMenuItem){
            String buttonLabel = ((JMenuItem) source).getName();
            
            if(buttonLabel.equals("MENU_CLOSE"))
                System.exit(0);
        }

        if(source instanceof JButton){
            String buttonLabel = ((JButton) source).getName();
            //zmienic na enum !!! i wrzucic do model

            switch (buttonLabel){
                case "NEW_GAME":
                    mainFrame.setPlayerPanel();
                    break;
                case "SPACE_SHIPS":
                    mainFrame.setSpaceShipsPanel();
                    String playerName = mainFrame.getPlayerName();
                    PlayerContainer.GameMode gameMode = mainFrame.getGameMode();
                    playerContainer.addPlayer(new Player(playerName));

                    switch (gameMode){
                        case VS_COMPUTER:
                            playerContainer.addPlayer(new ComputerPlayer("komputer"));
                            break;
                        case VS_HUMAN:
                            playerContainer.addPlayer(new Player(""));
                            break;
                    }

                    playerContainer.setGameState(PlayerContainer.GameState.SPACE_SHIP);
                    break;
                case "PLAY":
                    mainFrame.setGamePanel();
                    playerContainer.initEnemyShip();
                    playerContainer.setMyShips(mainFrame.getMySpeceShipRectangles());
                    playerContainer.setGameState(PlayerContainer.GameState.GAME);
                    mainFrame.hideEnemyShips();
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
