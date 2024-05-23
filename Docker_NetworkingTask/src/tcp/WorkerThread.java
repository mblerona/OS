package tcp;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class WorkerThread extends Thread {

    private final Socket socket;

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String firstMessage = reader.readLine();
            if (!Objects.equals(firstMessage, "login")) {
                System.out.println("First message has to be login. Client Connection failed");
                // writer.write("First message has to be login. Connection failed\n");
                writer.flush();
                return;
            }

            CounterClass.messageCounter.incrementAndGet();
            System.out.println("Client logged in");
            writer.write("Client logged in\n");
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                CounterClass.messageCounter.incrementAndGet();
                System.out.println("Replica: " + line);
                System.out.println("Messages: " + CounterClass.messageCounter);
                if (Objects.equals(line, "logout")) {
                    System.out.println("Client left connection...");
                    System.out.println("Logged out\n");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
