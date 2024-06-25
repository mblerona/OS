package Exam_Networking.Lab2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

class CounterClass{
    public static final AtomicInteger messageCounter=new AtomicInteger(0);

}

public class Server  extends Thread{
    private final int port;
    public Server(int port){
        this.port=port;
    }

    @Override
    public void run() {
        System.out.println("Waiting for connections...");
        try(ServerSocket serverSocket =new ServerSocket(port)){
            System.out.println("Server Started.. ");
            System.out.println("Listening on port: "+ port);
            while(true){
                Socket clientSocket=serverSocket.accept();
                System.out.println("new client connected");
                new Worker(clientSocket).start();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server=new Server(7000);
        server.start();
    }

}
