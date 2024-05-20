package Networking.UDP.example1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends  Thread{
    private DatagramSocket socket;
    private byte[] buffer = new byte[256];

    public Server(int port ){
        try {
            socket=new DatagramSocket(port);
        } catch (SocketException e) {
        e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
        while(true){
            try {
                socket.receive(packet);
                String received =new String(packet.getData(),0, packet.getLength());
                System.out.println("RECEIVED: "+received);
                InetAddress address= packet.getAddress();
                int port= packet.getPort();

                packet=new DatagramPacket(buffer, buffer.length,address,port);
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
