package view;

import model.NoNameException;
import controller.MenuPanelListener;
import model.PlayerContainer;

import javax.swing.*;
import java.awt.*;
/**
 * Klasa odpowiadająca za drugi widok gry. Wybór rodzaju gry oraz wprowadzanie
 * nazwy gracza.
 * @author blazej
 */
public class PlayerPanel extends JPanel {

    private JLabel playerNameLabel;
    private JTextField playerName;
    private JButton spaceShipsButton;
    private MenuPanelListener menuPanelListener;
    private JRadioButton vsComputerButton, vsHumanButton;
    private ButtonGroup buttonGroup;

    public PlayerPanel() {
        super();
        playerName = new JTextField("Nazwa gracza", 15);
        playerNameLabel = new JLabel("Nazwa gracza  ");
        spaceShipsButton = new JButton("Rozstaw statki");

        vsComputerButton = new JRadioButton("vs komputer");
        vsComputerButton.setSelected(true);

        vsHumanButton = new JRadioButton("Online");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(vsComputerButton);
        buttonGroup.add(vsHumanButton);
        spaceShipsButton.setName("SPACE_SHIPS");

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;
        add(vsComputerButton, c);
        c.gridx = 1;
        add(vsHumanButton, c);

        c.gridy = 2;
        c.gridx = 0;

        add(playerNameLabel, c);

        c.gridx = 1;
        add(playerName, c);

        c.gridy = 3;
        add(spaceShipsButton, c);

        menuPanelListener = MenuPanelListener.getInstance();
        spaceShipsButton.addActionListener(menuPanelListener);
    }

    public String getPlayerName() throws NoNameException {
        if (playerName.getText().length() == 0) {
            throw new NoNameException();
        }
        return playerName.getText();
    }

    public PlayerContainer.GameMode getGameMode() {
        if (vsComputerButton.isSelected()) {
            return PlayerContainer.GameMode.VS_COMPUTER;
        } else {
            return PlayerContainer.GameMode.VS_HUMAN;
        }
    }
}
