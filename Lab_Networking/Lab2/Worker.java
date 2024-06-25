package Exam_Networking.Lab2;

import Synchronization.ProducerController;
import org.w3c.dom.css.Counter;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread{
    private final Socket socket;
    public Worker(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try(BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String firstMessage= br.readLine();

            if(!firstMessage.equals("login")){
                System.out.println("first message has to be login..");
                System.out.println("Connection failed");
                writer.write("bla bla");
                writer.flush();
                return;
            }

            CounterClass.messageCounter.incrementAndGet();
            System.out.println("Client logged in");
            writer.write("Client logged in");
            writer.flush();


            String line;
            while((line= br.readLine())!=null){
                CounterClass.messageCounter.incrementAndGet();
                System.out.println("Replica:" +line);
                System.out.println("Counter " +CounterClass.messageCounter);

                if(line.equals("logout")){
                    System.out.println("Client left..");
                    System.out.println("Logged out");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
