package Exam_Networking.Lab2;

//TCP connection
//Client connects to server, server sends connection
// Server sends response "logged in"
// if first message isnt login, server closes connection
//client sends any message to which server replies with the same

//if client says logout, server says "logged out" and closes connection

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//server should have a counter for the messages for all clients.
// server should allow parralel connections from multiple clients
public class Client extends Thread{
    private final String serverName;
    private final int serverPort;

    public Client(String serverName, int serverPort){
        this.serverName=serverName;
        this.serverPort=serverPort;

    }

    @Override
    public void run() {
        try(Socket socket=new Socket(InetAddress.getByName(serverName),serverPort)) {
            BufferedReader reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


            System.out.println("Connected to Server");

            String line;
            while(true){
                if(consoleReader.ready()){
                    line=consoleReader.readLine();
                    writer.write(line+ "\n");
                    writer.flush();

                    if(line.equalsIgnoreCase("logout")){
                        break;
                    }
                }

                if(reader.ready()){
                    line= reader.readLine();
                    if(line!=null && !line.isEmpty()){
                        System.out.println("server: " + line);
                    }
                }
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client= new Client("localhost", 7000);
        client.start();
    }
}
