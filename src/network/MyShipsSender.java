package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import model.DataPacket;
import model.MyRectangleContainer;
import model.MyShipsPacket;

public class MyShipsSender {

    private static byte[] sentData;
    private static DatagramSocket sentSocket;
    private static InetAddress ipAddress;
    private static int port;
    private static MyShipsSender instance;

    private MyShipsSender() {
    }

    public static MyShipsSender getInstance() {
        return instance;
    }

    public static void init(InetAddress ipAdd, int p) {
        instance = new MyShipsSender();
        MyShipsSender.ipAddress = ipAdd;
        MyShipsSender.port = p;
    }

    public void send(DataPacket dataPacket)
            throws IOException {
        sentSocket = new DatagramSocket();
        sentData = DataPacket.packetToString(dataPacket).getBytes();
        DatagramPacket sentPacket = new DatagramPacket(sentData,
                sentData.length, ipAddress, port);
        sentSocket.send(sentPacket);
        System.out.println("Sent: " + new String(sentData));

    }
    public void sendCallback(MyRectangleContainer myShips) throws IOException{
        sendMyShips(myShips);
    }

    public void sendMyShips(MyRectangleContainer myShips) throws IOException {
        sentSocket = new DatagramSocket();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(myShips);
            byte[] myShipsBytes = bos.toByteArray();
            sentData = myShipsBytes;
            DatagramPacket sentPacket = new DatagramPacket(sentData,
                    sentData.length, ipAddress, port);
            sentSocket.send(sentPacket);
            System.out.println("Sent myShips");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

}
