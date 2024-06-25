package Exam_Networking.Lab1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

//Client sends login message to server
//Server sends confirmation login
//Client sends any message, server replies echo with the same
//if client says log out, server replies logged out. client terminates

public class Client extends Thread{
    private String serverName;
    private int serverPort;
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;

    public Client(String serverName, int serverPort) throws SocketException, UnknownHostException {
        this.serverName=serverName;
        this.serverPort=serverPort;

        this.datagramSocket=new DatagramSocket();
        this.inetAddress= InetAddress.getByName(serverName);

    }

    @Override
    public void run() {
        try(BufferedReader br =new BufferedReader(new InputStreamReader(System.in))){

            while(true){
                System.out.println("Enter message...");
                String message=br.readLine();

                sendMessage(message);
                if(message.equals("logout")){
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            datagramSocket.close();
        }
    }

    private void sendMessage(String message) throws IOException {
        byte[]buffer=message.getBytes();
        DatagramPacket packet=new DatagramPacket(buffer,buffer.length,inetAddress,serverPort);
        datagramSocket.send(packet);

        packet=new DatagramPacket(new byte[256],256);
        datagramSocket.receive(packet);

        String response =new String(packet.getData(),0,packet.getLength());
        System.out.println("Server response= " +response);
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        Client client= new Client("localhost", 5555);
        client.start();

    }
}
