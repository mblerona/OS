package Networking.TCP.Example3_MilenaAud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    public Server(int port){
        this.port=port;
    }

    @Override
    public void run() {
        System.out.println("Starting the server..");
        ServerSocket serverSocket=null;
        try {
            serverSocket= new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started !!");

        while(true){

            try {
                Socket clientSocket= serverSocket.accept();
                System.out.println("new client - creating worker thread");
                Worker worker = new Worker(clientSocket);
                worker.start(); // Start the worker thread

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
