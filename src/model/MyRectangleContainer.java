package model;

import java.io.Serializable;

/**
 * Klasa-kontener. Przetrzymuje wszystkie pola
 * z mapy danego gracza.
 * @author blazej
 */
public class MyRectangleContainer implements Serializable {

    private int columnNumber, rowNumber;
    private MyRectangle[][] myRectangles;
    private boolean isInited = false;
    private Ship lastHighlightedShip;
    private MyRectangle lastHighlightedRectangle;

    public MyRectangleContainer(int columnNumber, int rowNumber) {
        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        myRectangles = new MyRectangle[rowNumber][columnNumber];
    }

    public MyRectangleContainer(MyRectangle[][] myRectangles) {
        this.rowNumber = myRectangles.length;
        this.columnNumber = myRectangles[0].length;
        this.myRectangles = new MyRectangle[rowNumber][columnNumber];
        
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                this.myRectangles[i][j] = new MyRectangle(myRectangles[i][j]);
            }
        }
    }

    public void addRectangle(int rowNumber, int columnNumber, MyRectangle myRectangle) {
        myRectangles[rowNumber][columnNumber] = myRectangle;
    }

    public MyRectangle getRectangle(int rowNumber, int columnNumber) {
        return myRectangles[rowNumber][columnNumber];
    }

    public boolean isInited() {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (myRectangles[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public MyRectangle[][] getMyRectangles() {
        return myRectangles;
    }

    /**
     * Metoda odpowiada za zmianę koloru wyświetlanych pól
     * podczas rozstawiania statków.
     * @param rowNumber
     * @param columnNumber
     * @param size rozmiar statku (liczba masztów)
     * @param shipPosition pionowa lub pozioma
     * @param startFromCenter czy środek statku znajduje się w miejscu kursora
     */
    public void highlightShip(int rowNumber, int columnNumber, int size, ShipPosition shipPosition, boolean startFromCenter) {
        int begin, current;
        MyRectangle tmp;
        boolean isShipExited = false;
        boolean isError = false;

        if (lastHighlightedShip != null) {
            for (MyRectangle rect : lastHighlightedShip.getMyRectangles()) {
                rect.setNormalAfterHighlight();
            }
        }

        lastHighlightedShip = new Ship();
        lastHighlightedShip.setShipPosition(shipPosition);

        switch (shipPosition) {
            case VERTICAL:
                if (startFromCenter) {
                    begin = rowNumber - size / 2;

                    if (begin < 0) {
                        size += begin;
                        begin = 0;
                        isShipExited = true;
                    }
                } else {
                    begin = rowNumber;
                }

                if (this.rowNumber < (begin + size)) {
                    size = this.rowNumber - (begin);
                    isShipExited = true;
                }

                current = begin;

                for (int i = 0; i < size; i++) {
                    tmp = getRectangle(current++, columnNumber);
                    //ustawienie statusu bliskich
                    if (isShipExited) {
                        tmp.setError();
                    } else if (!tmp.highlightForShip()) {
                        //jezeli natrafimy na klocek ze statusem near to przerywamy
                        //i kolorujemy caly statek na czerwono w petli ponizej
                        isError = true;
                        break;
                    }
                    lastHighlightedShip.addRectangles(tmp);
                }

                if (isError) {
                    lastHighlightedShip.clear();
                    current = begin;
                    for (int i = 0; i < size; i++) {
                        tmp = getRectangle(current++, columnNumber);
                        tmp.setError();
                        lastHighlightedShip.addRectangles(tmp);
                    }
                }

                break;
            case HORIZONTAL:
                if (startFromCenter) {
                    begin = columnNumber - size / 2;
                    if (begin < 0) {
                        size += begin;
                        begin = 0;
                        isShipExited = true;
                    }
                } else {
                    begin = columnNumber;
                }

                if (this.columnNumber < (begin + size)) {
                    size = this.columnNumber - (begin);
                    isShipExited = true;
                }
                current = begin;
                for (int i = 0; i < size; i++) {
                    tmp = getRectangle(rowNumber, current++);
                    if (isShipExited) {
                        tmp.setError();
                    } else if (!tmp.highlightForShip()) {
                        isError = true;
                        break;
                    }
                    lastHighlightedShip.addRectangles(tmp);
                }

                if (isError) {
                    lastHighlightedShip.clear();
                    current = begin;
                    for (int i = 0; i < size; i++) {
                        tmp = getRectangle(rowNumber, current++);
                        tmp.setError();
                        lastHighlightedShip.addRectangles(tmp);

                    }
                }
                break;
        }
    }

    /**
     * Metoda ustawia pola dookoła statku na status NEAR.
     * Dzięku temu nie można postawić obok niego innego statku.
     * @return boolean
     */
    public boolean chooseLastShip() {
        if (lastHighlightedShip != null) {
            int rowNumber, columnNumber;
            
            for (int i = 0; i < lastHighlightedShip.getMyRectangles().size(); i++) {
                MyRectangle rect = lastHighlightedShip.getMyRectangles().get(i);
                if (!rect.setShip()) {
                    return false;
                } else {
                    rowNumber = rect.getRowNumber();
                    columnNumber = rect.getColumnNumber();
                    switch (lastHighlightedShip.getShipPosition()) {
                        case VERTICAL:
                            //ustawienie bocznych column statku na status NEAR
                            if (columnNumber > 0) {
                                getRectangle(rowNumber, columnNumber - 1).setIsNearToShip();
                            }

                            if (columnNumber < this.columnNumber - 1) {
                                getRectangle(rowNumber, columnNumber + 1).setIsNearToShip();
                            }

                            //ustawienie gornych wierszy na status NEAR jezeli istnieja i nie sa juz zaznaczone
                            if (rowNumber + 1 < this.rowNumber && getRectangle(rowNumber + 1, columnNumber).getStatus() != MyRectangle.Status.SHIP) {
                                getRectangle(rowNumber + 1, columnNumber).setIsNearToShip();
                            }

                            if (rowNumber > 0 && getRectangle(rowNumber - 1, columnNumber).getStatus() != MyRectangle.Status.SHIP) {
                                getRectangle(rowNumber - 1, columnNumber).setIsNearToShip();
                            }

                            break;
                        case HORIZONTAL:
                            if (rowNumber > 0) {
                                getRectangle(rowNumber - 1, columnNumber).setIsNearToShip();

                            }

                            if (rowNumber < this.rowNumber - 1) {
                                getRectangle(rowNumber + 1, columnNumber).setIsNearToShip();

                            }

                            if (columnNumber + 1 < this.columnNumber && getRectangle(rowNumber, columnNumber + 1).getStatus() != MyRectangle.Status.SHIP) {
                                getRectangle(rowNumber, columnNumber + 1).setIsNearToShip();
                            }

                            if (columnNumber > 0 && getRectangle(rowNumber, columnNumber - 1).getStatus() != MyRectangle.Status.SHIP) {
                                getRectangle(rowNumber, columnNumber - 1).setIsNearToShip();
                            }

                            break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Metoda ustawia pola dookoła statku
     * na status MISSED. Używana po zatopieniu statku. Dzięki niej komputer nie
     * strzela dookoła zatopionego statku unikając marnowania strzałów.
     * @param ship
     */
    public void setMissedArroundMyRectangles(Ship ship) {
        int rowNumber, columnNumber;
        
        for (int i = 0; i < ship.getMyRectangles().size(); i++) {
            MyRectangle rect = ship.getMyRectangles().get(i);
            rowNumber = rect.getRowNumber();
            columnNumber = rect.getColumnNumber();
            System.out.println(" RowNmuber " + rowNumber + " columnNumber " + columnNumber);
            switch (ship.getShipPosition()) {
                case VERTICAL:
                    //ustawienie bocznych column statku na status MISSED
                    if (columnNumber > 0) {
                        getRectangle(rowNumber, columnNumber - 1).shoot();
                    }

                    if (columnNumber < this.columnNumber - 1) {
                        getRectangle(rowNumber, columnNumber + 1).shoot();
                    }

                    //ustawienie gornych wierszy na status MISSED jezeli istnieja i nie sa juz zaznaczone
                    if (rowNumber + 1 < this.rowNumber && getRectangle(rowNumber + 1, columnNumber).getStatus() != MyRectangle.Status.SHIP) {
                        getRectangle(rowNumber + 1, columnNumber).shoot();
                    }

                    if (rowNumber > 0 && getRectangle(rowNumber - 1, columnNumber).getStatus() != MyRectangle.Status.SHIP) {
                        getRectangle(rowNumber - 1, columnNumber).shoot();
                    }

                    break;
                case HORIZONTAL:
                    if (rowNumber > 0) {
                        getRectangle(rowNumber - 1, columnNumber).shoot();

                    }

                    if (rowNumber < this.rowNumber - 1) {
                        getRectangle(rowNumber + 1, columnNumber).shoot();

                    }

                    if (columnNumber + 1 < this.columnNumber && getRectangle(rowNumber, columnNumber + 1).getStatus() != MyRectangle.Status.SHIP) {
                        getRectangle(rowNumber, columnNumber + 1).shoot();
                    }

                    if (columnNumber > 0 && getRectangle(rowNumber, columnNumber - 1).getStatus() != MyRectangle.Status.SHIP) {
                        getRectangle(rowNumber, columnNumber - 1).shoot();
                    }

                    break;
            }
        }

    }

    public void removeLastShip() {
        for (MyRectangle rect : lastHighlightedShip.getMyRectangles()) {
            rect.clear();
        }
    }

    public Ship getLastHighlightedShip() {
        return lastHighlightedShip;
    }

    public boolean shoot(int rowNumber, int columnNumber) {
        return getRectangle(rowNumber, columnNumber).shoot();
    }

    public void hideShips() {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                myRectangles[i][j].hide();
            }
        }
    }

    /**
     * Metoda zwraca pole
     * z najwyższym prawdopodobieństwem znajdowania się na nim statku.
     * @return pole z najwyższym prawdopodobieństwem
     */
    public MyRectangle getHighestProbablityMyRectangle() {
        MyRectangle rect = myRectangles[0][0];
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (rect.getProbability() < myRectangles[i][j].getProbability() && myRectangles[i][j].isForShoot()) {
                    rect = myRectangles[i][j];
                }
            }
        }
        return rect;
    }

    public void clear() {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                myRectangles[i][j].clear();
            }
        }
    }

    public void printProbability() {
        System.out.println("--------------");
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                System.out.print(String.format("%1$3s", myRectangles[i][j].getProbability()));
            }
            System.out.println();
        }
    }

    public void clearProbability() {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                myRectangles[i][j].setProbability(0);
            }
        }
    }

    public boolean highlightMyRectangle(int rowNumber, int columnNumber) {
        if (lastHighlightedRectangle != null) {
            lastHighlightedRectangle.setNormalAfterHighlight();
        }
        lastHighlightedRectangle = getRectangle(rowNumber, columnNumber);

        return lastHighlightedRectangle.highlightForShoot();
    }

    public boolean isRectangleForShoot(int rowNumber, int columnNumber) {
        return getRectangle(rowNumber, columnNumber).isForShoot();
    }
}
