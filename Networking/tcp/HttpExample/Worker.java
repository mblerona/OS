package Networking.TCP.Example1_Intro_Kostadin;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker extends Thread {

    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            WebRequest request = WebRequest.of(reader);
            System.out.println(request.command + " " + request.url);

            // Comment out the logging part for now
            // shareLog(socket.getInetAddress().getHostAddress(), request.command, request.url);

            writer.write("HTTP/1.1 200 OK\n");
            writer.write("Content-Type: text/html\n\n");

            writer.write("Hello " + request.header.get("User-Agent") + "! <br/>");
            writer.write("You requested: " + request.command + " " + request.url + " by using HTTP version " + request.version + "\n");
            writer.write("\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Comment out the entire shareLog method
    /*
    public void shareLog(String clientIPAddress, String command, String url) throws IOException {
        String serverName = System.getenv("LOGGER_SERVERNAME");
        String serverPort = System.getenv("LOGGER_SERVERPORT");
        if (serverPort == null) {
            throw new RuntimeException("Logger Server port is not specified {LOGGER_SERVERPORT}!");
        }
        Socket socket = new Socket(InetAddress.getByName(serverName), Integer.parseInt(serverPort));
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(String.format("[%s] %s: %s %s", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), clientIPAddress, command, url));
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
    */

    public static class WebRequest {

        private String command;
        private String url;
        private String version;

        private Map<String, String> header;

        private WebRequest(String command, String url, String version, Map<String, String> header) {
            this.command = command;
            this.url = url;
            this.version = version;
            this.header = header;
        }

        public static WebRequest of(BufferedReader reader) throws IOException {
            List<String> input = new ArrayList<>();
            String line;
            while (!(line = reader.readLine()).equals("")) {
                input.add(line);
            }
            String[] args = input.get(0).split(" ");
            String command = args[0];
            String url = args[1];
            String version = args[2];

            HashMap<String, String> headers = new HashMap<>();

            for (int i = 1; i < input.size(); i++) {
                String[] pair = input.get(i).split(":");
                headers.put(pair[0], pair[1]);
            }

            WebRequest request = new WebRequest(command, url, version, headers);
            return request;
        }
    }
}
