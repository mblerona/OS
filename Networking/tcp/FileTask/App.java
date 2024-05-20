package Networking.TCP.Example2_Jovana_fileTask;

public class App {
    public static void main(String[] args) {
        Server server=new Server("client_data",3398);
        server.start();

        Client client1= new Client("localhost", 3398,"\"C:\\Users\\blero\\OneDrive\\Desktop\\finki\\4 Semester\\OS\\AudJava\\OS\\src\"");
        Client client2= new Client("localhost", 3398,"\"C:\\Users\\blero\\OneDrive\\Desktop\\finki\\4 Semester\\OS\\AudJava\\OS\\src\\Networking\\TCP\\Example2\\contentFolder\"");

         client1.start();
         client2.start();
    }
}
