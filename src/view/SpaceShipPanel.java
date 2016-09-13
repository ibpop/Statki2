package view;

import controller.MenuPanelListener;
import controller.PositionButtonListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
/**
 * Klasa reprezentująca widok gry, który odpowiada za rozstawienie statków.
 * @author blazej
 */
public class SpaceShipPanel extends JPanel {

    private GamePanel gamePanel;
    private GridBagConstraints c;
    private JButton playButton;
    private MenuPanelListener menuPanelListener;
    private JPanel arrowPanel;
    private JPanel shipsLeftToSetPanel;
    private JLabel shipsLeftToSetLabel;
    private JPanel networkPanel;
    private JLabel networkPanelLabel;
    private MainFrame mainFrame;

    private JTextField portSend;
    private JTextField portReceive;
    private JTextField ip;

    public String getIp() {
        return ip.getText();
    }

    public int getPortSend() throws NumberFormatException {
        return Integer.parseInt(portSend.getText());
    }

    public int getPortReceive() throws NumberFormatException {
        return Integer.parseInt(portReceive.getText());
    }

    public SpaceShipPanel() {
        super();
        gamePanel = new GamePanel("Rozstaw swoje statki");
        arrowPanel = new JPanel();
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.weightx = 3;
        c.weighty = 3;
        c.gridy = 0;
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;

        add(gamePanel, c);

        shipsLeftToSetPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cShipsLeft = new GridBagConstraints();
        shipsLeftToSetLabel = new JLabel();
        shipsLeftToSetPanel.add(shipsLeftToSetLabel);
        cShipsLeft.gridx = 0;
        cShipsLeft.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 0;
        c.gridx = 1;
        add(shipsLeftToSetPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;
        playButton = new JButton("Graj");
        playButton.setName("PLAY");
        playButton.setEnabled(false);
        menuPanelListener = MenuPanelListener.getInstance();
        playButton.addActionListener(menuPanelListener);
        add(playButton, c);

        arrowPanel.setLayout(new GridBagLayout());
        JLabel arrowPanelLabel = new JLabel("Zmien pozycje statku: ");
        arrowPanel.add(arrowPanelLabel);
        GridBagConstraints cArrow = new GridBagConstraints();

        PositionButtonListener buttonListener = new PositionButtonListener();

        BasicArrowButton horizontal = new BasicArrowButton(BasicArrowButton.WEST);
        horizontal.setName("HORIZONTAL");
        horizontal.addActionListener(buttonListener);

        BasicArrowButton vertical = new BasicArrowButton(BasicArrowButton.NORTH);
        vertical.setName("VERTICAL");
        vertical.addActionListener(buttonListener);

        cArrow.gridx = 1;
        cArrow.gridy = 0;
        cArrow.anchor = GridBagConstraints.LINE_END;

        arrowPanel.add(horizontal, cArrow);
        cArrow.gridx = 2;
        cArrow.gridy = 0;
        cArrow.anchor = GridBagConstraints.LINE_START;
        arrowPanel.add(vertical, cArrow);

        c.gridy = 1;
        c.gridx = 2;
        add(arrowPanel, c);
    }
/**
 * Metoda wstawiająca panel sieciowy do SpaceShipPanel jeżeli wybrano
 * rozgrywkę online.
 */
    public void setNetworkPanel() {
        networkPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cNetwork = new GridBagConstraints();
        networkPanelLabel = new JLabel("Informacje sieciowe");
        networkPanel.add(networkPanelLabel);

        portSend = new JTextField("", 10);
        JLabel portLabelSend = new JLabel("Port OUT");

        portReceive = new JTextField("", 10);
        JLabel portLabelReceive = new JLabel("Port IN");

        ip = new JTextField("localhost", 10);
        JLabel ipLabel = new JLabel("IP");

        cNetwork.gridx = 0;
        cNetwork.gridy = 1;

        networkPanel.add(ipLabel, cNetwork);

        cNetwork.gridx = 1;
        cNetwork.gridy = 1;

        networkPanel.add(ip, cNetwork);

        cNetwork.gridx = 0;
        cNetwork.gridy = 2;

        networkPanel.add(portLabelSend, cNetwork);

        cNetwork.gridx = 1;
        cNetwork.gridy = 2;

        networkPanel.add(portSend, cNetwork);

        cNetwork.gridx = 0;
        cNetwork.gridy = 3;

        networkPanel.add(portLabelReceive, cNetwork);

        cNetwork.gridx = 1;
        cNetwork.gridy = 3;

        networkPanel.add(portReceive, cNetwork);

        cNetwork.gridx = 0;
        cNetwork.gridy = 4;

        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 0;
        c.gridx = 2;
        add(networkPanel, c);
    }

    public void setPlayButtonEnabled(boolean isEneble) {
        playButton.setEnabled(isEneble);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setShipsLeftLabel(int numberOfShips) {
        shipsLeftToSetLabel.setText("Statki do rozstawienia: " + numberOfShips);
    }
}
