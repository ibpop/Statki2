package model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Klasa reprezentująca statek.
 * @author blazej
 */
public class Ship implements Serializable{

    private int size;
    private int beginRow;
    private int beginColumn;
    private ArrayList<MyRectangle> myRectangles;
    private ShipPosition shipPosition;

    public Ship(int size, int beginRow, int beginColumn, ArrayList<MyRectangle> myRectangles) {
        this.size = size;
        this.beginRow = beginRow;
        this.beginColumn = beginColumn;
        this.myRectangles = myRectangles;
    }

    public Ship(){
        this.myRectangles = new ArrayList<MyRectangle>();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    public void setBeginColumn(int beginColumn) {
        this.beginColumn = beginColumn;
    }

    public void setMyRectangles(ArrayList<MyRectangle> myRectangles) {
        this.myRectangles = myRectangles;
    }

    public ShipPosition getShipPosition() {
        if(shipPosition == null)
            setShipPosition();
        return shipPosition;
    }

    private void setShipPosition(){
        if(myRectangles != null){
            shipPosition = ShipPosition.HORIZONTAL;
            if(myRectangles.size() >= 2){
                if(myRectangles.get(0).getColumnNumber() == myRectangles.get(1).getColumnNumber())
                    shipPosition = ShipPosition.VERTICAL;
            }
        }
    }
    public void setShipPosition(ShipPosition shipPosition) {
        this.shipPosition = shipPosition;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<MyRectangle> getMyRectangles() {
        return myRectangles;
    }

    public void addRectangles(MyRectangle rect){
        myRectangles.add(rect);
        size = myRectangles.size();
    }

    public void clear(){
        myRectangles.clear();
    }

    public void sunk(){
        for(MyRectangle rect : myRectangles){
            rect.sunk();
        }
    }

    public boolean isSunk(){
        for(MyRectangle rect : myRectangles){
            if(rect.getStatus() != MyRectangle.Status.HIT && 
               rect.getStatus() != MyRectangle.Status.SUNK)
                return false;
        }
        return true;
    }
/**
 * Metoda sprawdza czy na zadanym polu znajduje się statek.
 * @param rowNumber
 * @param columnNumber
 * @return boolean
 */
    public boolean contains(int rowNumber, int columnNumber){
        for(MyRectangle rect : myRectangles){
            if(rect.getColumnNumber() == columnNumber && rect.getRowNumber() == rowNumber)
                return true;
        }
        return false;
    }
}
