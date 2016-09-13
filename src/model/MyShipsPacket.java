package model;

public class MyShipsPacket {

    private MyRectangleContainer myMap;

    public MyShipsPacket(MyRectangleContainer myMap) {
        this.myMap = myMap;
    }

    public MyRectangleContainer getMyMap() {
        return myMap;
    }

    public void setMyMap(MyRectangleContainer myMap) {
        this.myMap = myMap;
    }

}
