package model;

import java.awt.*;

/**
 * Definiuje pojedyncze pole mapy gracza.
 * @author blazej
 */
public class MyRectangle extends Rectangle {

    public static enum Status {NORMAL, SHIP, HIT, MISSED, NEAR, NEARSHOOTED, SUNK};
    private Status status = Status.NORMAL;
    private int rowNumber, columnNumber;
    private static final Color NORMAL = Color.WHITE;
    private static final Color SHIP = Color.BLUE;
    private static final Color ERROR = Color.RED;
    private static final Color HIGHLIGHT = Color.DARK_GRAY;
    private static final Color MISSED = Color.CYAN;
    private static final Color HIT = Color.black;
    private static final Color SUNK = Color.ORANGE;

    private Color color = NORMAL;
    private int probability;

    public MyRectangle(int height, int width, int x, int y, int rowNumber, int columnNumber){
        super(height, width, x, y);
        
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public MyRectangle(MyRectangle myRectangle){
        this.status = myRectangle.status;
        this.color = myRectangle.color;
        this.probability = myRectangle.probability;
        this.rowNumber = myRectangle.rowNumber;
        this.columnNumber = myRectangle.columnNumber;
    }

    /**
     * Metoda zmienia kolor pola jeżeli możliwe jest
     * umiejscowienie na nim statku.
     * @return boolean
     */
    public boolean highlightForShip(){
        if(color == NORMAL && status != Status.NEAR && status !=Status.SUNK){
            color = HIGHLIGHT;
            return true;
        } else if(status == Status.NEAR){
            setError();
        }

        if(status == Status.HIT || status == Status.MISSED)
            return false;

        return false;
    }

    /**
     * Metoda zmienia kolor pola jeżeli możliwe
     * jest wykonanie strzału na tym polu.
     * @return boolean
     */
    public boolean highlightForShoot(){
        if(color == NORMAL){
            color = HIGHLIGHT;
            return true;
        }
        return false;
    }

    /**
     * Metoda zmienia kolor pola jeżeli niemożliwe jest
     * umiejscowienie na nim statku.
     * @return boolean
     */
    public boolean setError(){
        if(color!= SHIP && color!= MISSED && color!= HIT){
            color = ERROR;
            return true;
        }
        return false;
    }

    /**
     * Metoda zmienia status i kolor pola na odpowiadający
     * znajdowaniu się na tym polu części statku.
     * @return boolean
     */
    public boolean setShip(){
        if(color != ERROR && color != SHIP && status != Status.MISSED) {
            color = SHIP;
            status = Status.SHIP;
            return true;
        }
        return false;
    }

    /**
     * Metoda zmienia kolor pola na normalny (biały)
     * jeżeli nie znajduje się już na nim statek do ustawienia.
     * @return boolean
     */
    public boolean setNormalAfterHighlight(){
        if(color == ERROR || color == HIGHLIGHT){
            color = NORMAL;
            return true;
        }
        return false;
    }

    /**
     * Metoda ustawia kolor i status statku na normalny (biały).
     */
    public void hide(){
        color = NORMAL;
        if(status == Status.NEAR){
            status = Status.NORMAL;
        }
//        //odkrywa statki komputera - do testow
//        } else if(status == Status.SHIP){
//            color = Color.DARK_GRAY;
//        }
    }

    /**
     * Metoda ustawia kolor i status pola na SUNK - czarny.
     */
    public void sunk(){
        color = SUNK;
        status = Status.SUNK;
    }

    public Color getColor() {
        return color;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Ustawia status pola na NEAR.
     * Wykorzystywana do określania czy możliwe jest ustawienie statku
     * na danym polu. Wywoływana, jeżeli w polu obok znajduje się inny statek.
     */
    public void setIsNearToShip(){
        if(status != Status.HIT && status != Status.MISSED)
            status = Status.NEAR;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * Metoda wywoływana podczas strzału. Ustawia odpowiedni status
     * pola oraz jego kolor w zależności od statusu przed strzałem.
     * @return boolean
     */
    public boolean shoot(){
        switch (status) {
            case NORMAL:
                status = Status.MISSED;
                color = MISSED;
                break;
            case SHIP:
                status = Status.HIT;
                color = HIT;
                return true;
            case HIT:
                status = Status.HIT;
                color = HIT;
                break;
            case MISSED:
                status = Status.MISSED;
                color = MISSED;
                break;
            case NEAR:
                status = Status.MISSED;
                color = MISSED;
                break;
            case NEARSHOOTED:
                status = Status.MISSED;
                color = MISSED;
                break;
        }
        return false;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public void increaseProbability(int probability){
        this.probability += probability;
    }

    public void decreaseProbability(int probability){
        this.probability -= probability;
    }

    public int getProbability() {
        return probability;
    }

    public void clear(){
        status = Status.NORMAL;
        color = NORMAL;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setStatusOnline(Status status, Color color) {
        this.status = status;
        this.color = color;
    }

    /**
     * Metoda określa czy możliwe jest wykonanie strzału do
     * tego pola.
     * @return boolean
     */
    public boolean isForShoot(){
        if(status != Status.HIT && status != Status.MISSED && status != Status.SUNK)
            return true;
        return false;
    }

}
