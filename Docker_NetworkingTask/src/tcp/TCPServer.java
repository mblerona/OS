package tcp;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class CounterClass {
    public static final AtomicInteger messageCounter = new AtomicInteger(0);
}

public class TCPServer extends Thread {

    private final int port;

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Waiting for clients...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected...");
                new WorkerThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPServer server = new TCPServer(7000);
        server.start();
    }
}
