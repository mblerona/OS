package Networking.UDP.Example3;

import java.io.IOException;
import java.net.*;

public class Client extends Thread{
    private String serverName;
    private int serverPort;

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private String message;
    private byte[] buffer;

    public Client(String serverName, int serverPort, String message) throws UnknownHostException {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message=message;

        try {
            this.datagramSocket=new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        this.inetAddress=InetAddress.getByName(serverName);

    }

    @Override
    public void run() {
        buffer=message.getBytes();

        DatagramPacket packet=new DatagramPacket(buffer,buffer.length, inetAddress,serverPort);

        try {
            datagramSocket.send(packet);
            packet=new DatagramPacket(buffer, buffer.length, inetAddress, serverPort);
            datagramSocket.receive(packet);
            System.out.println(new String(
                    packet.getData(),0, packet.getLength()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) throws UnknownHostException {
        Client client= new Client("localhost", 4445,"HEllo there :) ");
        client.start();
    }
}
