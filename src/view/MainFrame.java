package view;


import controller.MenuPanelListener;
import model.MyRectangleContainer;
import model.PlayerContainer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private MenuPanel menuPanel;
    private GamePanel myShipPanel, enemyShipPanel;

    public GamePanel getEnemyShipPanel() {
        return enemyShipPanel;
    }

    public GamePanel getMyShipPanel() {
        return myShipPanel;
    }
    private SpaceShipPanel spaceShipsPanel;
    private JPanel allPanel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem closeMenuItem;
    //private MyComponentListener mMyComponentListener;
    private static MainFrame mainFrame = new MainFrame();
    private GridBagConstraints gridBagConstraints;
    private PlayerPanel playerPanel;
    private PlayerContainer playerContainer;
    private MenuPanelListener menuPanelListener;

    private MainFrame(){
        super();
        playerContainer = PlayerContainer.getInstance();

        playerPanel = new PlayerPanel();

        myShipPanel = new GamePanel("Moje statki");
        myShipPanel.setLabel(5);
        enemyShipPanel = new GamePanel("Statki wroga");
        enemyShipPanel.setLabel(5);
        spaceShipsPanel = new SpaceShipPanel();

        allPanel = new JPanel();

        menuPanel = new MenuPanel();
        //mMyComponentListener = new MyComponentListener();
        gridBagConstraints = new GridBagConstraints();
        allPanel.setLayout(new GridBagLayout());
        allPanel.add(menuPanel, gridBagConstraints);

        add(allPanel);
        menuPanelListener = MenuPanelListener.getInstance();
        closeMenuItem = new JMenuItem("Zamknij");
        closeMenuItem.setName("MENU_CLOSE");
        closeMenuItem.addActionListener(menuPanelListener);
        
        menu = new JMenu("Opcje");
        
        menu.add(closeMenuItem);
        menuBar = new JMenuBar();
        menuBar.add(menu);

        setJMenuBar(menuBar);

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        setPreferredSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void setPlayerPanel(){
        allPanel.remove(menuPanel);
        allPanel.add(playerPanel);
        validate();
        repaint();
    }

    public void setGamePanel(){
        allPanel.remove(spaceShipsPanel);

        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.gridy = 0;

        gridBagConstraints.gridx = 0;
        allPanel.add(enemyShipPanel, gridBagConstraints);

        //MyRectangleContainer tmp = new MyRectangleContainer(spaceShipsPanel.getGamePanel().getCells().getMyRectangles());
        enemyShipPanel.setCells(playerContainer.getEnemyShip());
        playerContainer.getEnemyShip().getMyRectangles();


        gridBagConstraints.gridx = 1;
        myShipPanel.removeAllListeners();
        //myShipPanel.setCells(new MyRectangleContainer(spaceShipsPanel.getGamePanel().getCells().getMyRectangles()));
        myShipPanel.setCells(spaceShipsPanel.getGamePanel().getCells());

        allPanel.add(myShipPanel, gridBagConstraints);

        //mGamePanel.addComponentListener(mMyComponentListener);
        validate();
        repaint();
    }

    public void setFinishedPanel(String message){
        JPanel finishedPanel = new JPanel();
        finishedPanel.add(new JLabel(message));
        allPanel.remove(myShipPanel);
        allPanel.remove(enemyShipPanel);
        allPanel.add(finishedPanel);
        repaint();
        validate();
    }

    public static MainFrame getInstance(){
        return mainFrame;
    }


    public void setSpaceShipsPanel() {
        allPanel.remove(playerPanel);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        allPanel.add(spaceShipsPanel, gridBagConstraints);
        validate();
        repaint();
    }

    public String getPlayerName(){
        return playerPanel.getPlayerName();
    }

    public SpaceShipPanel getSpaceShipsPanel() {
        return spaceShipsPanel;
    }

    public void hideEnemyShips(){
        enemyShipPanel.hideShips();
    }

    public void shootMe(int rowNumber, int columnNumber){
        myShipPanel.shoot(rowNumber, columnNumber);
    }

    public PlayerContainer.GameMode getGameMode(){
        return playerPanel.getGameMode();
    }

    public MyRectangleContainer getMySpeceShipRectangles(){
        return spaceShipsPanel.getGamePanel().getCells();
    }
}
