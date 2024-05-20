package Networking.TCP.Example2_Jovana_fileTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
    String serverAddress;
    int port;
    String folderToSearch;

    public Client(String serverAddress, int port, String folderToSearch) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.folderToSearch = folderToSearch;
    }

    static int getFiles(String folderToSearch){
        File file =new File(folderToSearch);
        FilenameFilter txtCSV = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercase= name.toLowerCase();
                return lowercase.endsWith(".txt") || lowercase.endsWith(".csv");
            }
        };

        File[]files = file.listFiles(txtCSV);
        int num=0;
        if(files != null) { // Check if files array is not null
            for(File f: files){
                if(f.isFile() && f.length()>10*1024 && f.length()<100*1024){
                    num++;
                }
            }
        }
        return num;
    }

    void sendData(String serverAddress, int port, int num) throws IOException {
        Socket socket=null;
        try {
            socket=new Socket(serverAddress,port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(num);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            socket.close();
        }

    }
    @Override
    public void run(){
int num =getFiles(this.folderToSearch);
        try {
            sendData(serverAddress,port,num);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
