package view;

import controller.MenuPanelListener;
import controller.PositionButtonListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import sun.text.normalizer.CharTrie;

/**
 * Created by Mateo on 2016-06-02.
 */
public class SpaceShipPanel extends JPanel {
    private GamePanel gamePanel;
    private GridBagConstraints c;
    private JButton playButton;
    private MenuPanelListener menuPanelListener;
    private JPanel arrowPanel;
    private JPanel shipsLeftToSetPanel;
    private JLabel shipsLeftToSetLabel;

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
        shipsLeftToSetLabel = new JLabel("Statki do rozstawienia: " + 5);
        shipsLeftToSetPanel.add(shipsLeftToSetLabel);
        cShipsLeft.gridx = 0;
        cShipsLeft.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 0;
        c.gridx = 1;
        add(shipsLeftToSetPanel,c);
        
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

        c.gridy =1;
        c.gridx=2;
        add(arrowPanel,c);

    }

    public void setPlayButtonEnabled(boolean isEneble){
        playButton.setEnabled(isEneble);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    public void setShipsLeftLabel(int numberOfShips){
        shipsLeftToSetLabel.setText("Statki do rozstawienia: " + numberOfShips);
    }
}

