package model;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Mateo on 2016-08-15.
 */
public class PlayerContainer {

    public static enum PlayerType {ME, ENEMY};
    public static enum GameState {SPACE_SHIP, GAME};
    public static enum GameMode {VS_COMPUTER, VS_HUMAN};

    private static PlayerContainer playerContainer = new PlayerContainer();
    private PlayerType currentPlayerType;
    private PlayerType enemy;

    public Player getEnemyPlayer(){
        return playersMap.get(enemy);
    }
    private Map<PlayerType, Player> playersMap;
    private static GameState gameState;
    private GameMode gameMode;


    private PlayerContainer(){
        playersMap = new EnumMap<PlayerType, Player>(PlayerType.class);
        currentPlayerType = PlayerType.ME;
        enemy = PlayerType.ENEMY;
    }

    public static PlayerContainer getInstance(){
        return playerContainer;
    }

    public void addPlayer(Player player){
        if(!playersMap.containsKey(PlayerType.ME)){
            playersMap.put(PlayerType.ME, player);
        }else if(!playersMap.containsKey(PlayerType.ENEMY)){
            playersMap.put(PlayerType.ENEMY, player);
        } else{
            System.out.println("Obaj zawodnicy zostali dodani");
        }
    }

    public Player getCurrentPlayer(){
        return playersMap.get(currentPlayerType);
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public void initEnemyShip(){
        ComputerPlayer player = (ComputerPlayer) playersMap.get(PlayerType.ENEMY);
        player.setMyShips();
    }

    public MyRectangleContainer getEnemyShip(){
        return  playersMap.get(PlayerType.ENEMY).getMyRectangles();
    }

    public void shootEnemy(int rowNumber, int columnNumber){
        playersMap.get(PlayerType.ENEMY).shoot(rowNumber, columnNumber);
    }

    public void setPlayersShip(PlayerType playerType, MyRectangleContainer myRectangleContainer){
        playersMap.get(playerType).setMyRectangles(myRectangleContainer);
    }

    public Point getEnemyShoot(){
        ComputerPlayer player = (ComputerPlayer) playersMap.get(PlayerType.ENEMY);
        return player.getShoot();
    }

    public void shootMe(int rowNumber, int columnNumber){
        playersMap.get(PlayerType.ME).shoot(rowNumber, columnNumber);
        ComputerPlayer computerPlayer = (ComputerPlayer) playersMap.get(PlayerType.ENEMY);
        computerPlayer.setLastShootStatus(playersMap.get(PlayerType.ME).getMyRectangles().getRectangle(rowNumber, columnNumber).getStatus(), playersMap.get(PlayerType.ME).isLastShootDestructive());
        
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isEnemyBeaten(){
        return playersMap.get(PlayerType.ENEMY).isBeaten();
    }

    public boolean isMeBeaten(){
        return playersMap.get(PlayerType.ME).isBeaten();
    }

    public void setMyShips(MyRectangleContainer myRectangleContainer){
        playersMap.get(PlayerType.ME).setMyRectangles(myRectangleContainer);
    }


}
