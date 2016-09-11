package model;

import java.awt.*;

/**
 * Created by Mateo on 2016-05-26.
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

    public boolean highlightForShoot(){
        if(color == NORMAL){
            color = HIGHLIGHT;
            return true;
        }
        return false;
    }

    public boolean setError(){
        if(color!= SHIP && color!= MISSED && color!= HIT){
            color = ERROR;
            return true;
        }
        return false;
    }

    public boolean setShip(){
        if(color != ERROR && color != SHIP && status != Status.MISSED) {
            color = SHIP;
            status = Status.SHIP;
            return true;
        }
        return false;
    }

    public boolean setNormalAfterHighlight(){
        if(color == ERROR || color == HIGHLIGHT){
            color = NORMAL;
            return true;
        }
        return false;
    }

    public void hide(){
        color = NORMAL;
        if(status == Status.NEAR){
            status = Status.NORMAL;
        }
//        } else if(status == Status.SHIP){
//            color = Color.DARK_GRAY;
//        }
    }

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

    public void decrease(int probability){
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

    public boolean isForShoot(){
        if(status != Status.HIT && status != Status.MISSED && status != Status.SUNK)
            return true;
        return false;
    }

}
