package Networking.youtube_exercises.example2_OneWayCom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("localhost", 4999);
        PrintWriter pr= new PrintWriter(socket.getOutputStream());
        pr.println("Hello i am client");
        pr.flush();

    }
}
