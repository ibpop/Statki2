package network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.MyRectangleContainer;
import model.PlayerContainer;
import view.MainFrame;
/**
 * Listener do wysyłania informacji o swojej mapie. Używany przy ewentualnej
 * serializacji.
 * @author blazej
 */
public class EnemyShipsListener {

    private static DatagramSocket receiveSocket;
    private static byte[] receiveData;
    private static EnemyShipsListener instance;
    private static int port;
    private static PlayerContainer.PlayerType player;
    private static MainFrame mainFrame;
    private static long started;
    private static boolean initialized;
    private boolean callbackSent;

    private EnemyShipsListener() {
        this.callbackSent = false;
    }

    public static EnemyShipsListener getInstance() {
        return instance;
    }

    public static void init(int p) {
        instance = new EnemyShipsListener();
        initialized = false;
        started = System.currentTimeMillis();
        EnemyShipsListener.port = p;
        try {
            if (EnemyShipsListener.receiveSocket == null) {
                EnemyShipsListener.receiveSocket = new DatagramSocket(EnemyShipsListener.port);
            }
        } catch (SocketException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainFrame = MainFrame.getInstance();
    }

    public void listenForShips() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                while (true) {
                    try {
                        receiveData = new byte[6000];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        receiveSocket.receive(receivePacket);
                        byte[] enemyShipsBytes = receivePacket.getData();
                        if(!callbackSent){
                            MyShipsSender myShipsSender = MyShipsSender.getInstance();
                            myShipsSender.sendCallback(mainFrame.getMySpaceShipRectangles());
                            callbackSent = true;
                            receiveSocket.close();
                        }
                       
                        ByteArrayInputStream bis = new ByteArrayInputStream(enemyShipsBytes);
                        ObjectInputStream in = null;
                        try {
                            in = new ObjectInputStream(bis);
                            try {
                                Object o = in.readObject();
                                PlayerContainer playerContainer = PlayerContainer.getInstance();
                                playerContainer.getEnemyPlayer().setMyRectangles((MyRectangleContainer)o);
                                System.out.println("Received enemy ships");
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } finally {
                            try {
                                bis.close();
                            } catch (IOException ex) {
                                // ignore close exception
                            }
                            try {
                                if (in != null) {
                                    in.close();
                                }
                            } catch (IOException ex) {
                                // ignore close exception
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

   
}
