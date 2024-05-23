package tcp;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient extends Thread {
    private final String serverName;
    private final int serverPort;

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(InetAddress.getByName(serverName), serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            String line;
            while (true) {
                if (consoleReader.ready()) {
                    line = consoleReader.readLine();
                    writer.write(line + "\n");
                    writer.flush();
                    if ("logout".equalsIgnoreCase(line)) {
                        break;
                    }
                }

                if (reader.ready()) {
                    line = reader.readLine();
                    if (line != null && !line.isEmpty()) {
                        System.out.println("Server: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient("localhost", 7000);
        client.start();
    }
}
