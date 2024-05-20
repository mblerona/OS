package Networking.UDP.example1;

public class App {
    public static void main(String[] args) {
        Server server =new Server(8080);
        server.start();

        Client client= new Client("localhost",8080,"Hello World");
        client.start();
    }
}
