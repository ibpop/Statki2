package view;

import controller.MenuPanelListener;

import javax.swing.*;
import java.awt.*;
/**
 * Klasa reprezentująca początkowy panel gry.
 * @author blazej
 */
public class MenuPanel extends JPanel {
    private JButton newGameButton;
    private JButton exitButton;
    private MenuPanelListener menuPanelListener;


    public MenuPanel(){
        super();
        setPreferredSize(new Dimension(100, 100));

        newGameButton = new JButton("Nowa gra");
        newGameButton.setName("NEW_GAME");
        exitButton = new JButton("Wyjście");
        exitButton.setName("EXIT");

        menuPanelListener = MenuPanelListener.getInstance();

        newGameButton.addActionListener(menuPanelListener);
        exitButton.addActionListener(menuPanelListener);

        add(newGameButton);
        add(exitButton);
    }
}
