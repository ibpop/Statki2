package controller;

import model.ShipPosition;
import view.GridPanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mateo on 2016-08-17.
 */
public class PositionButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source instanceof BasicArrowButton){
            String buttonLabel = ((BasicArrowButton) source).getName();
            switch (buttonLabel){
                case "HORIZONTAL":
                    GridPanel.setShipPosition(ShipPosition.HORIZONTAL);
                    break;
                case "VERTICAL":
                    GridPanel.setShipPosition(ShipPosition.VERTICAL);
                    break;
                default:
                    break;
            }

        }

    }
}
