package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa dziedzicząca po klasie Player. Ta klasa odpowiada za
 * implementację logiki komputera podczas rozgrywki.
 * @author blazej
 */
public class ComputerPlayer extends Player {

    public static enum ShootingMode {
        HUNT, TARGET
    };
    public ShootingMode shootingMode = ShootingMode.HUNT;
    private int[] shipToHit;
    private MyRectangleContainer myShoots;
    private MyRectangle lastShoot;
    private Ship hittedShip;

    public ComputerPlayer(String name) {
        super(name);
        shipToHit = new int[shipToSet.length];

        for (int i = 0; i < shipToSet.length; i++) {
            shipToHit[i] = shipToSet[i];
        }
        hittedShip = new Ship();

    }

    /**
     * Ustawia statki komputera na planszy. Wybór dokonywany
     * jest losowo do momentu aż wszystkie statki zostaną prawidłowo umiejscowione.
     */
    public void setMyShips() {
        int shipSize = getShipSizeToSet();
        int beginRow, beginColumn;
        ShipPosition shipPosition;
        boolean flag;
        Random rand = new Random();

        while (true) {
            shipSize = getShipSizeToSet();
            if (shipSize == 0) {
                break;
            }
            while (true) {
                beginRow = rand.nextInt(rowNumber);
                beginColumn = rand.nextInt(columnNumber);
                shipPosition = ShipPosition.randomPosition();
                //shipPosition = ShipPosition.HORIZONTAL;
                myRectangles.highlightShip(beginRow, beginColumn, shipSize, shipPosition, false);
                flag = myRectangles.chooseLastShip();
                if (flag) {
                    break;
                }
            }
            setShip(shipSize, myRectangles.getLastHighlightedShip());
        }
    }

    /**
     * Logika odpowiedzialna za wykonanie strzału.
     * Zwraca współrzędne pola, w które strzela komputer.
     * @return Point - Klasa
     */
    public Point getShoot() {
        Random rand = new Random();
        int shootColumn = rand.nextInt(columnNumber);
        int shootRow = rand.nextInt(rowNumber);

        if (myShoots == null) {
            myShoots = new MyRectangleContainer(myRectangles.getMyRectangles());
            myShoots.clear();
        }

        System.out.println(shootingMode);
        setProbabilityForShoot();
        myShoots.printProbability();
        lastShoot = myShoots.getHighestProbablityMyRectangle();
        myShoots.clearProbability();
        shootColumn = lastShoot.getColumnNumber();
        shootRow = lastShoot.getRowNumber();
        System.out.println(shootRow + " " + shootColumn);
        return new Point(shootColumn, shootRow);
    }
/**
 * Ustawia prawdopodobieństwo znalezienia się
 * statku w danym polu na mapie.
 */
    private void setProbabilityForShoot() {
        for (int i = 0; i < shipToHit.length; i++) {
            if (shipToHit[i] > 0) {
                for (int j = 0; j < shipToHit[i]; j++) {
                    setProbabilityForShip(i + 1, ShipPosition.HORIZONTAL);
                    setProbabilityForShip(i + 1, ShipPosition.VERTICAL);
                }
            }
        }
    }
/**
 * Ustawia prawdopodobieństwo znalezienia się
 * w danym polu dla wybranego rodzaju statku.
 * @param shipSize rozmiar statku
 * @param position pozycja: Pionowa, Pozioma
 */
    private void setProbabilityForShip(int shipSize, ShipPosition position) {
        MyRectangleContainer tmp = new MyRectangleContainer(myShoots.getMyRectangles());
        tmp.hideShips();
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                tmp.highlightShip(i, j, shipSize, position, false);
                if (tmp.chooseLastShip()) {
                    int probability = 1;

                    if (shootingMode == ShootingMode.TARGET && isShipContainsHitted(tmp.getLastHighlightedShip())) {
                        probability = 30;
                    }

                    for (MyRectangle rect : tmp.getLastHighlightedShip().getMyRectangles()) {
                        myShoots.getRectangle(rect.getRowNumber(), rect.getColumnNumber()).increaseProbability(probability);
                    }
                    tmp.removeLastShip();
                    //to do refactor zeby pousuwac status isNear ukrywam wszystkie
                    //tmp.hideShips();
                }
                tmp.hideShips();
            }
        }
    }
/**
 * Sprawdza czy dany statek został trafiony.
 * @param ship Statek, który ma zostać sprawdzony.
 * @return prawda lub fałsz
 */
    private boolean isShipContainsHitted(Ship ship) {
        if (hittedShip != null) {
            for (MyRectangle rect : hittedShip.getMyRectangles()) {
                if (ship.contains(rect.getRowNumber(), rect.getColumnNumber())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ustawia stan komputera w jaki ma przejść po
     * oddanym strzale.
     * @param status jaki status ustawić 
     * @param lastShootDestructive czy ostatni strzał zniszczył statek
     */
    public void setLastShootStatus(MyRectangle.Status status, boolean lastShootDestructive) {
        lastShoot.setStatus(status);
        switch (status) {
            case NORMAL:
                break;
            case SHIP:
                break;
            case HIT:
                System.out.println("lastShootDestructive " + lastShootDestructive);
                if (shootingMode == ShootingMode.HUNT && !lastShootDestructive) {
                    shootingMode = ShootingMode.TARGET;
                }
                hittedShip.addRectangles(lastShoot);
                break;
            case MISSED:
                break;
            case NEAR:
                break;
            case NEARSHOOTED:
                break;
            case SUNK:
                hittedShip.addRectangles(lastShoot);
                shootingMode = ShootingMode.HUNT;
                shipToHit[hittedShip.getSize() - 1] = shipToHit[hittedShip.getSize() - 1] - 1;
                myShoots.setMissedArroundMyRectangles(hittedShip);
                hittedShip.clear();
                break;
        }
    }
}
