package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.DataPacket;
import model.MyRectangleContainer;
import model.PlayerContainer;
import view.MainFrame;
/**
 * Listener do komunikacji sieciowej. Nasłuchuje na oddzielnym wątku.
 * @author blazej
 */
public class MessageListener {

    private static DatagramSocket receiveSocket;
    private static byte[] receiveData;
    private static MessageListener instance;
    private static int port;
    private static PlayerContainer.PlayerType player;
    private static MainFrame mainFrame;
    private static long started;
    private static boolean initialized;

    private MessageListener() {
    }

    public static MessageListener getInstance() {
        return instance;
    }

    public static void init(int p) {
        instance = new MessageListener();
        initialized = false;
        started = System.currentTimeMillis();
        MessageListener.port = p;
        try {
            if (MessageListener.receiveSocket == null) {
                MessageListener.receiveSocket = new DatagramSocket(MessageListener.port);
            }
        } catch (SocketException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainFrame = MainFrame.getInstance();
    }

    public void listen() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        receiveData = new byte[32];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        receiveSocket.receive(receivePacket);

                        if (System.currentTimeMillis() - started < 100) {
                            mainFrame.setBothPlayersPresent();
                            System.out.println("setBothPlayersPresent()");
                            mainFrame.setTurn(PlayerContainer.PlayerType.ME);
                        } else {
                            mainFrame.setTurn(PlayerContainer.PlayerType.ENEMY);
                        }
                        String dataReceived = new String(receivePacket.getData());
                        System.out.println("received: " + dataReceived);
                        if (mainFrame.getTurn() == PlayerContainer.PlayerType.ENEMY) {
                            if (dataReceived.startsWith("0")) {
                                if (dataReceived.contains("-")) {
                                    mainFrame.setBothPlayersPresent();
                                    System.out.println("setBothPlayersPresent()");
                                    if (!initialized) {
                                        int myShipsLeft = PlayerContainer.getInstance().getCurrentPlayer().getNumberOfShips();
                                        
                                        mainFrame.getMessageSender().send(new DataPacket(PlayerContainer.PlayerType.ME, -1, -1, myShipsLeft));
                                        initialized = true;
                                    }
                                } else {
                                    mainFrame.handleShoot(DataPacket.stringToDataPacket(dataReceived));
                                }
                            } else if (dataReceived.startsWith("1")) {
                                mainFrame.handleCallback(dataReceived);
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
