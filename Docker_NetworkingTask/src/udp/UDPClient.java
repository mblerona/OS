package udp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPClient extends Thread {

    private int serverPort;
    private String serverName;

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;

    public UDPClient(String serverName, int serverPort) throws UnknownHostException {

        this.serverName = serverName;
        this.serverPort = serverPort;

        try {
            this.datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        this.inetAddress = InetAddress.getByName(serverName);
    }

    public void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, serverPort);
        datagramSocket.send(packet);
        packet = new DatagramPacket(new byte[256], 256);
        datagramSocket.receive(packet);
        String response = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Server response: " + response);
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.println("Enter message ('login' to connect / 'logout' to disconnect)\n");
                String message = br.readLine();
                sendMessage(message);
                if (message.equalsIgnoreCase("logout")) {
                    System.out.println("Disconnecting from server.. ");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            datagramSocket.close();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        UDPClient client = new UDPClient("localhost", 5555);
        client.start();
    }
}
