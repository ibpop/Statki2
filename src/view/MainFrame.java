package view;

import controller.GridPanelMouseListener;
import controller.MenuPanelListener;
import model.NoNameException;
import model.MyRectangleContainer;
import model.PlayerContainer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import model.DataPacket;
import model.MyRectangle;
import network.EnemyShipsListener;
import network.MessageListener;
import network.MessageSender;
import network.MyShipsSender;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
/**
 * Klasa definiująca główną ramkę gry - jej okno. W niej znajdują się wszystkie
 * panele.
 * @author blazej
 */
public class MainFrame extends JFrame {

    private MenuPanel menuPanel;
    private GamePanel myShipPanel, enemyShipPanel;
    private PlayerContainer.PlayerType turn;
    private long started;

    private MessageSender messageSender;
    private MessageListener messageListener;

    private MyShipsSender myShipsSender;
    private EnemyShipsListener enemyShipsListener;

    private SpaceShipPanel spaceShipsPanel;
    private JPanel allPanel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem closeMenuItem;
    private static MainFrame mainFrame = new MainFrame();
    private GridBagConstraints gridBagConstraints;
    private PlayerPanel playerPanel;
    private PlayerContainer playerContainer;
    private MenuPanelListener menuPanelListener;
    private boolean bothPlayersPresent;

    private MainFrame() {
        super();
        this.bothPlayersPresent = false;
        playerContainer = PlayerContainer.getInstance();

        playerPanel = new PlayerPanel();

        myShipPanel = new GamePanel("Moje statki");
        enemyShipPanel = new GamePanel("Statki wroga");
        spaceShipsPanel = new SpaceShipPanel();

        allPanel = new JPanel();

        menuPanel = new MenuPanel();
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
/**
 * Metoda ustawiająca panel gracza.
 */
    public void setPlayerPanel() {
        allPanel.remove(menuPanel);
        allPanel.add(playerPanel);
        validate();
        repaint();
    }

    public void setGamePanel() {
        allPanel.remove(spaceShipsPanel);

        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.gridy = 0;

        gridBagConstraints.gridx = 0;
        allPanel.add(enemyShipPanel, gridBagConstraints);

        enemyShipPanel.setCells(playerContainer.getEnemyShip());
        playerContainer.getEnemyShip().getMyRectangles();

        gridBagConstraints.gridx = 1;
        myShipPanel.removeAllListeners();
        myShipPanel.setCells(spaceShipsPanel.getGamePanel().getCells());

        allPanel.add(myShipPanel, gridBagConstraints);

        validate();
        repaint();
    }
/**
 * Metoda ustawiająca okno gry podczas jej zakończenia.
 * @param message 
 */
    public void setFinishedPanel(String message) {
        JPanel finishedPanel = new JPanel();
        finishedPanel.add(new JLabel(message));
        allPanel.remove(myShipPanel);
        allPanel.remove(enemyShipPanel);
        allPanel.add(finishedPanel);
        repaint();
        validate();
    }

    public static MainFrame getInstance() {
        return mainFrame;
    }
/**
 * Metoda ustawiająca panel rozmieszczania statków na mapie.
 */
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

    public String getPlayerName() throws NoNameException {
        return playerPanel.getPlayerName();
    }

    public SpaceShipPanel getSpaceShipsPanel() {
        return spaceShipsPanel;
    }

    public void hideEnemyShips() {
        enemyShipPanel.hideShips();
    }
/**
 * Metoda wykonująca strzał w zadane pole gracza.
 * @param rowNumber
 * @param columnNumber 
 */
    public void shootMe(int rowNumber, int columnNumber) {
        myShipPanel.shoot(rowNumber, columnNumber);
    }

    public PlayerContainer.GameMode getGameMode() {
        return playerPanel.getGameMode();
    }

    public MyRectangleContainer getMySpaceShipRectangles() {
        return spaceShipsPanel.getGamePanel().getCells();
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public GamePanel getEnemyShipPanel() {
        return enemyShipPanel;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public PlayerContainer.PlayerType getTurn() {
        return turn;
    }

    public void setTurn(PlayerContainer.PlayerType t) {
        turn = t;
    }

    public MyShipsSender getMyShipsSender() {
        return myShipsSender;
    }

    public void setMyShipsSender(MyShipsSender myShipsSender) {
        this.myShipsSender = myShipsSender;
    }

    public EnemyShipsListener getEnemyShipsListener() {
        return enemyShipsListener;
    }

    public void setEnemyShipsListener(EnemyShipsListener enemyShipsListener) {
        this.enemyShipsListener = enemyShipsListener;
    }
/**
 * Metoda odpowiadająca za obsługę informacji uzyskanych od przeciwnika
 * poprzez sieć.
 * @param dataReceived 
 */
    public void handleShoot(DataPacket dataReceived) {
        int column = dataReceived.getColumn();
        int row = dataReceived.getRow();
        int shipsLeft = dataReceived.getShipsLeft();
        if (shipsLeft == 0) {
            setFinishedPanel("Wygrałeś");
        } else {
            playerContainer.shootMeOnline(row, column);
            shootMe(row, column);
            int myShipsLeft = playerContainer.getCurrentPlayer().getNumberOfShips();
            getEnemyShipPanel().setLabel(shipsLeft);
            try {
                switch (playerContainer.getCurrentPlayer().getMyRectangles().getRectangle(row, column).getStatus()) {
                    case MISSED:
                        mainFrame.getMessageSender().send(0, row, column, myShipsLeft);
                        break;
                    case HIT:
                        mainFrame.getMessageSender().send(1, row, column, myShipsLeft);
                        break;
                    case SUNK:
                        mainFrame.getMessageSender().send(2, row, column, myShipsLeft);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        mainFrame.setTurn(PlayerContainer.PlayerType.ME);
    }
/**
 * Metoda odpowiadająca za obsługę informacji zwrotnych uzyskanych od przeciwnika po
 * wykonaniu strzału przez gracza.
 * @param dataReceived 
 */
    public void handleCallback(String dataReceived) {
        String[] data = dataReceived.split(" ");
        int rowNumber = Integer.parseInt(data[2]);
        int columnNumber = Integer.parseInt(data[3]);
        //playerContainer.getEnemyPlayer().getMyRectangles().getRectangle(rowNumber, columnNumber).shoot();
        switch (Integer.parseInt(data[1])) {
            case 0:
                playerContainer.getEnemyPlayer().getMyRectangles().getRectangle(rowNumber, columnNumber).setStatus(MyRectangle.Status.MISSED);
                break;
            case 1:
                playerContainer.getEnemyPlayer().getMyRectangles().getRectangle(rowNumber, columnNumber).setStatusOnline(MyRectangle.Status.HIT, Color.black);
                //TODO: ustaw color
                break;
            case 2:
                playerContainer.getEnemyPlayer().getMyRectangles().getRectangle(rowNumber, columnNumber).sunk();
                showMessageDialog(null, "Zatopiłeś statek wroga!");
                mainFrame.getEnemyShipPanel().setLabel(Integer.parseInt(data[4]));
                break;

        }
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public GamePanel getMyShipPanel() {
        return myShipPanel;
    }

    public void setBothPlayersPresent() {
        this.bothPlayersPresent = true;
    }
    
    public boolean isBothPlayersPresent() {
        return bothPlayersPresent;
    }
}
