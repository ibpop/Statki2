package model;

import view.GridPanel;

import java.util.ArrayList;
import view.MainFrame;

/**
 * Klasa reprezentuje gracza.
 * @author blazej
 */
public class Player {

    private String name;
    protected MyRectangleContainer myRectangles;
    protected int rowNumber;
    protected int columnNumber;
    //indeks tablicy + 1 oznacza ilosc masztow a wartosc ilosc statkow do rozstawienia
    protected int [] shipToSet = new int[] {1, 1, 1, 1, 1};
    //protected int[] shipToSet = new int[]{0, 2, 0, 0, 1}; //do testow
    private int mastLeftNumber;
    private ArrayList<Ship> myShips;
    private boolean lastShootDestructive = false;

    public Player(String name) {
        this.name = name;
        rowNumber = GridPanel.getRowNumber();
        columnNumber = GridPanel.getRowNumber();
        myRectangles = new MyRectangleContainer(columnNumber, rowNumber);
        myShips = new ArrayList<Ship>();

        for (int i = 0; i < shipToSet.length; i++) {
            mastLeftNumber += (i + 1) * shipToSet[i];
        }
    }

    /**
     * Liczy ile statków posiada gracz.
     * @return liczba statków
     */
    public int getNumberOfShips() {
        int number = 0;
        for (Ship s : myShips) {
            if (!s.isSunk()) {
                number++;
            }
        }
        return number;
    }

    /**
     * Zwraca rozmiar statku, który ma być ustawiony.
     * @return rozmiar statku
     */
    public int getShipSizeToSet() {
        int result = 0;
        for (int i = shipToSet.length - 1; i >= 0; i--) {
            if (shipToSet[i] > 0) {
                result = i + 1;
                break;
            }
        }
        return result;
    }

    /**
     * Metoda dodaje dany statek do kolekcji statków gracza.
     * @param size
     * @param ship
     */
    public void setShip(int size, Ship ship) {
        if (size > 0) {
            shipToSet[size - 1] = shipToSet[size - 1] - 1;
            myShips.add(ship);
        }

    }

    public MyRectangleContainer getMyRectangles() {
        return myRectangles;
    }

    public void setMyRectangles(MyRectangleContainer myRectangles) {
        this.myRectangles = myRectangles;
    }

    /**
     * Metoda odpowiadająca za wykonanie strzału gracza.
     * @param rowNumber
     * @param columnNumber
     */
    public void shoot(int rowNumber, int columnNumber) {
        if (getMyRectangles().shoot(rowNumber, columnNumber)) {
            lastShootDestructive = false;
            for (Ship ship : myShips) {
                if (ship.contains(rowNumber, columnNumber)) {
                    System.out.println("Ship is sunk " + ship.isSunk());
                }
                if (ship.contains(rowNumber, columnNumber) && ship.isSunk()) {
                    ship.sunk();
                    lastShootDestructive = true;
                    MainFrame mainFrame = MainFrame.getInstance();
                    int shipsLeft = getNumberOfShips();
                    mainFrame.getSpaceShipsPanel().getGamePanel().setLabel(shipsLeft);
                }
            }
            mastLeftNumber -= 1;
        }
    }

    public ArrayList<Ship> getMyShips() {
        return myShips;
    }

    public boolean isLastShootDestructive() {
        return lastShootDestructive;
    }

    public boolean isBeaten() {
        if (mastLeftNumber == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfShipsToSet() {
        int number = 0;

        for (int i = 0; i < shipToSet.length; i++) {
            number += shipToSet[i];
        }
        return number;
    }
}
