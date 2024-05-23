package udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {


    private  DatagramSocket socket;
    private byte[] buffer=new byte[256];

    public UDPServer(int port){
        try {
            this.socket=new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void run() {
        System.out.println("Server is running..");
        System.out.println("Waiting for connections...");

        while(true){
            try{
                DatagramPacket datagramPacket=new DatagramPacket(buffer,buffer.length);
                socket.receive(datagramPacket);

                String received=new String(datagramPacket.getData(),0, datagramPacket.getLength());

                System.out.println("Client said: "+received);

                String response;

                if(received.equalsIgnoreCase("login")){
                    response="logged in";
                    System.out.println("Client Connected");
                }
                else if(received.equalsIgnoreCase("logout")){
                    response="logged out";
                    System.out.println("Client disconnected");

                    System.out.println("================================");
                    System.out.println("Waiting for a connection...");

                }
                else{
                    response="Client said: " +received;
                    ;                }

                byte[] responseBytes = response.getBytes();

                DatagramPacket responsePacket =new DatagramPacket(responseBytes, responseBytes.length,datagramPacket.getAddress(),datagramPacket.getPort());
                socket.send(responsePacket);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }





    public static void main(String[] args) {
        UDPServer server= new UDPServer(5555);
        server.start();
    }
}
