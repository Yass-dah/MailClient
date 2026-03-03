package mailservice.mailclient.network;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String serverHost = "smtp.mailserver.com";
    private static final int serverPort = 5000;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected = false;

    // Connessione al server
    public boolean connect() {
        try {
            socket = new Socket(serverHost, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connected;
    }

    // Chiude connessione
    public void disconnect() {
        try {
            socket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}