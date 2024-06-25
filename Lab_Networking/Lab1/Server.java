package Exam_Networking.Lab1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Thread {
    private DatagramSocket socket;
    private byte[] buffer =new byte[256];

    public Server(int port){
        try {
            this.socket= new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Server is running..");
        System.out.println("Waiting for connections..");

        while(true){
            try{
            DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
            socket.receive(packet);

            String received = new String(packet.getData(),0,packet.getLength());
                System.out.println("Client said: "+received);


                String response;

                if(received.equalsIgnoreCase("login")){
                    response="logged in";
                    System.out.println("Client connected");
                }
                else if(received.equalsIgnoreCase("logout")){
                    response="logged out";
                    System.out.println("Client left connection");
                    System.out.println("===============");
                    System.out.println("Waiting for connections");
                }
                else{
                    response= "Echo "+ received;
                }

                byte[] responseBytes= response.getBytes();
                DatagramPacket responsePacket= new DatagramPacket(responseBytes,responseBytes.length,packet.getAddress(), packet.getPort());
                socket.send(responsePacket);

        }
            catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Server server = new Server(5555);
        server.start();

    }
}
