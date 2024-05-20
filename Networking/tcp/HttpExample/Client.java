package Networking.TCP.Example1_Intro_Kostadin;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
    private int serverPort;
    private String serverName;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            socket = new Socket(InetAddress.getByName(this.serverName), this.serverPort);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("GET / HTTP/1.1\n");
            writer.write("Host: developer.mozilla.org\n");
            writer.write("User-Agent: OSClient\n");
            writer.write("\n");
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Client received: " + line);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        // Using hardcoded values for server name and port
        String serverName = "localhost";
        int serverPort = 7000;

        Client client = new Client(serverName, serverPort);
        client.start();
    }
}
