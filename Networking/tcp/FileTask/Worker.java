package Networking.TCP.Example2_Jovana_fileTask;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker extends Thread{
          private Socket socket;
          private BufferedWriter bw;


          public Worker(Socket socket, BufferedWriter bw){
              this.socket=socket;
              this.bw=bw;

          }

    @Override
    public void run() {
        try {
            receiveData(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void receiveData(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int numFiles= dis.readInt();
        bw.write(String.format("%s %d %d",
                socket.getInetAddress().getHostAddress(),
                socket.getPort(),
                numFiles));
        bw.newLine();
        bw.flush();
    }
}
