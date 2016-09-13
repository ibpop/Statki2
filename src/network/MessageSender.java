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

public class MessageSender {

    private static byte[] sentData;
    private static DatagramSocket sentSocket;
    private static InetAddress ipAddress;
    private static int port;
    private static MessageSender instance;

    private MessageSender() {
    }

    public static MessageSender getInstance() {
        return instance;
    }

    public static void init(InetAddress ipAdd, int p) {
        instance = new MessageSender();
        MessageSender.ipAddress = ipAdd;
        MessageSender.port = p;
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

//    public void sendMyShips(MyRectangleContainer myShips) throws IOException {
//        sentSocket = new DatagramSocket();
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream out = null;
//        try {
//            out = new ObjectOutputStream(bos);
//            out.writeObject(myShips);
//            byte[] myShipsBytes = bos.toByteArray();
//            sentData = myShipsBytes;
//            DatagramPacket sentPacket = new DatagramPacket(sentData,
//                    sentData.length, ipAddress, port);
//            sentSocket.send(sentPacket);
//            System.out.println("Sent myShips");
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//            try {
//                bos.close();
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }
//    }

    public void send(int callback, int row, int col, int numberOfShips)
            throws IOException {
        sentSocket = new DatagramSocket();
        sentData = ("1" + " " + callback + " " + row + " " + col + " " + numberOfShips + " ").getBytes();
        DatagramPacket sentPacket = new DatagramPacket(sentData,
                sentData.length, ipAddress, port);
        sentSocket.send(sentPacket);
        System.out.println("Sent: " + new String(sentData));

    }
}
