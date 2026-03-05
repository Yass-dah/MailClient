package mailservice.mailclient.network;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import mailservice.mailclient.model.Mail;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String serverHost = "localhost";
    private static final int serverPort = 5000;
    private ConnectionLooper conn = new ConnectionLooper();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected = false;

    // Getters
    public boolean getConnected() {
        return connected;
    }

    public ConnectionLooper getConnectionLooper(){
        return conn;
    }

    // Connessione al server
    public boolean connect() {
        try {
            socket = new Socket(serverHost, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return connected;
    }

    // classe aggiuntiva per verifica connessione periodica eseguita da un thread
    public class ConnectionLooper implements Runnable {
        private volatile boolean running = true;
        private volatile SimpleBooleanProperty reachable = new SimpleBooleanProperty(false);

        public void stop(){
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        public boolean isReachable() {
            return reachable.get();
        }

        public SimpleBooleanProperty reachableProperty() {
            return reachable;
        }

        public void run() {
            while (isRunning()) {
                boolean flag = false;
                try (Socket testSocket = new Socket()) {
                    testSocket.connect(new InetSocketAddress("localhost", 5000), 500);
                    flag = true;
                } catch (IOException ignored) {
                    flag = false;
                }
                final boolean x = flag;
                Platform.runLater(() -> reachable.set(x));
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    System.err.println(e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Chiude connessione
    public void disconnect() {
        try {
            if(socket != null)
                socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        connected = false;
    }

    // operation methods
    public boolean checkEmail(String email){
        out.println("1|" + email);
        String response = "";
        try {
            response = in.readLine();
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return "OK".equals(response);
    }

    public boolean sendMail(String from, String to, String subject, String body){
        out.println("2|" + from + "|" + to + "|" + subject + "|" + body);
        String response = "";
        try {
            response = in.readLine();
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return "MAIL_SENT".equals(response);
    }

    public List<Mail> getInbox(String email) throws IOException {
        List<Mail> mails = new ArrayList<>();
        out.println("3|" + email);

        String line;

        while (!(line = in.readLine()).equals("END"))
            mails.add(parseMail(line));

        return mails;
    }

    public boolean deleteMail(String email, long id){
        out.println("4|" + email + "|" + id);
        String response = "";
        try {
            response = in.readLine();
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return "MAIL_DELETED".equals(response);
    }

    private Mail parseMail(String line) {
        String[] parts = line.split("\\|");

        Mail mail = new Mail();
        mail.setId(Long.parseLong(parts[0]));
        mail.setFrom(parts[1]);
        mail.setTo(parts[2]);
        mail.setSubject(parts[3]);
        mail.setBody(parts[4]);
        mail.setDate(LocalDateTime.parse(parts[5]));
        return mail;
    }
}