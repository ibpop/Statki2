package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import model.DataPacket;
import model.MyRectangleContainer;
/**
 * Sender do komunikacji sieciowej. Wysyła informacje o strzałach.
 * @author blazej
 */
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
/**
 * Metoda wywoływana w odpowiedzi na informacje uzyskane od przeciwnika.
 * Informuje przeciwnika o rezultacie, jaki dał jego strzał.
 * @param callback
 * @param row
 * @param col
 * @param numberOfShips
 * @throws IOException 
 */
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
