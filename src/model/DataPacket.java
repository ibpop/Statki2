package model;

import model.PlayerContainer.PlayerType;

public class DataPacket {

    private PlayerType player;
    private int column;
    private int row;
    private int shipsLeft;

    public DataPacket(PlayerType player, int row, int column, int ships) {
        this.player = player;
        this.column = column;
        this.row = row;
        this.shipsLeft = ships;
    }

    public static DataPacket stringToDataPacket(String dataReceived) {
        String[] st = dataReceived.split(" ");
        PlayerType player;
        if (st[0].equalsIgnoreCase("0")) {
            player = PlayerType.ME;
        } else {
            player = PlayerType.ENEMY;
        }
        int row = Integer.parseInt(st[1]);
        int column = Integer.parseInt(st[2]);
        int shipsLeft = Integer.parseInt(st[3]);
        return new DataPacket(player, row, column, shipsLeft);
    }

    public static String packetToString(DataPacket dataPacket) {
        StringBuilder sb = new StringBuilder();
        String player = null;
        if (dataPacket.getPlayer() == PlayerType.ME) {
            player = "0";
        } else {
            player = "1";
        }
        sb.append("0").append(" ")
                .append(dataPacket.getRow()).append(" ").append(dataPacket.getColumn())
                .append(" ")
                .append(dataPacket.getShipsLeft()).append(" ");
        return sb.toString();
    }

    public PlayerType getPlayer() {
        return player;
    }

    public void setPlayer(PlayerType player) {
        this.player = player;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getShipsLeft() {
        return shipsLeft;
    }

    public void setShipsLeft(int shipsLeft) {
        this.shipsLeft = shipsLeft;
    }

}
