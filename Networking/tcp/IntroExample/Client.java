package Networking.TCP.Example3_MilenaAud;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client extends Thread{


    private InetAddress inetAddress;
    private int port;

    Client(InetAddress inetAddress, int port){
        this.inetAddress=inetAddress;
        this.port=port;
    }
    @Override
    public void run() {
        Socket socket=null;
        Random random =new Random();
        try {
            socket =new Socket(inetAddress,port);

            BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String method = random.nextInt(10)%2==0? "GET" : "POST";
            bw.write(method+ " /movies/"+ random+" HTTP/1.1\n");
            bw.write("User: FINKI\n");
            bw.flush();

            String line= br.readLine();
            while(line!=null){
                System.out.println("Servet sent : " + line);
                line=br.readLine();
            }

            System.out.println("DONE");
            socket.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        Client client= null;
        try {
            client = new Client(InetAddress.getLocalHost(),5000);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        client.start();

    }
}
