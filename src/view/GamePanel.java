package view;



import model.MyRectangleContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {
    private GridBagConstraints c;
    private GridPanel gridPanel;
    private char [] alphabet;
    private int alphabetIndex = 0;
    private JLabel label;
    private String panelName;

    public String getPanelName() {
        return panelName;
    }

    public void setLabel(int numberOfShips){
        
        label.setText(this.panelName + ": " + Integer.toString(numberOfShips));
    }
    
    public GamePanel(String panelName){
        super();
        
        this.panelName = panelName;
        label = new JLabel(panelName);

        alphabet = new char[10];

        for(char alph = 'A'; alph <= 'J'; alph++){
            pushAlbhabet(alph);
        }

        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.weightx = 1;
        c.weighty = 1;

        c.gridy = 0;
        add(label,c);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 1;


        for(Integer i = 1; i < 11; i++){
            c.gridx = i;
            add(new JLabel(i.toString()), c);
        }

        c.gridx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        for(Integer i = 1; i < 11; i++){
            c.gridy = i+1;
            add(new JLabel(Character.toString(alphabet[i-1])), c);
        }

        c.fill = GridBagConstraints.BOTH;
        c.gridy = 2;
        c.gridx = 1;
        c.gridwidth = 10;
        c.gridheight = 10;
        gridPanel = new GridPanel();
        add(gridPanel, c);

    }

    private void pushAlbhabet(char element){
        alphabet[alphabetIndex] = element;
        ++alphabetIndex;
    }

    private char popAlbhabet(){
        --alphabetIndex;
        return alphabet[alphabetIndex];
    }

    public void resetPoints(){
        repaint();
    }

    public void removeAllListeners(){
        for(MouseListener ml : gridPanel.getMouseListeners()){
            gridPanel.removeMouseListener(ml);
        }

        for(MouseMotionListener mml : gridPanel.getMouseMotionListeners()){
            gridPanel.removeMouseMotionListener(mml);
        }
    }

    public MyRectangleContainer getCells(){
        return gridPanel.getCells();
    }

    public void setCells(MyRectangleContainer cells){
        gridPanel.setCells(cells);
    }

    public void hideShips(){
        gridPanel.hideShips();
    }

    public void shoot(int rowNumber, int columnNumber){
        gridPanel.shoot(rowNumber, columnNumber);
    }

}
