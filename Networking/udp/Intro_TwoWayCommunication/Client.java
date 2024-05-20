package Networking.youtube_exercises.Example3_TwoWayCom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("localhost", 4999);
        PrintWriter pr= new PrintWriter(socket.getOutputStream());
        pr.println("Hello i am client");
        pr.println("Is it working?");
        pr.flush();


        InputStreamReader in=new InputStreamReader(socket.getInputStream());
        BufferedReader br= new BufferedReader(in);
        String str= br.readLine();

        System.out.println("Server: "+ str);
    }
}
