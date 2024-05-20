package Networking.UDP.example1;

import java.io.IOException;
import java.net.*;

public class Client extends Thread{
    private String ServerName;
    private int ServerPort;

    private DatagramSocket socket;
    private InetAddress inetAddress;
    private String message;
    private byte[] buffer;

    public Client(String serverName, int serverPort, String message) {
        this.ServerName = serverName;
        this.ServerPort = serverPort;
        this.message=message;
        try {
            this.socket=new DatagramSocket();
            this.inetAddress= inetAddress.getByName(serverName);
        } catch (SocketException e) {
           e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer=message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,this.inetAddress, this.ServerPort);
        try {
            socket.send(packet);
            packet=new DatagramPacket(buffer, buffer.length,this.inetAddress,this.ServerPort);
            socket.receive(packet);
            System.out.println(new String(packet.getData(),0, packet.getLength()));

        } catch (IOException e) {
           e.printStackTrace();
        }

    }
}
