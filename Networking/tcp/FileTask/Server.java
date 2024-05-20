package Networking.TCP.Example2_Jovana_fileTask;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    int port;

ServerSocket serverSocket;
BufferedWriter bw;
String path;

public Server(String path, int port){
    this.path=path;
    this.port=port;

}

    @Override
    public void run() {
        try {
            this.bw=new BufferedWriter(new FileWriter(path,true));
             this.serverSocket = new ServerSocket(port);

            while(true){
                Socket socket = this.serverSocket.accept();
                Worker worker= new Worker(socket,bw);
                worker.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
