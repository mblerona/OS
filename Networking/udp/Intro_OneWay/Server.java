package Networking.youtube_exercises.example2_OneWayCom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(4999);
        Socket socket= serverSocket.accept();
        System.out.println("Client Connected..");

        InputStreamReader in=new InputStreamReader(socket.getInputStream());
        BufferedReader br= new BufferedReader(in);
        String str= br.readLine();

        System.out.println("Client message: "+ str);


    }
}
