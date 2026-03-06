package mailservice.mailclient.network;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import mailservice.mailclient.model.Mail;

import java.io.*;
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
                boolean status = checkConn();
                if(!status)
                    connect();
                Platform.runLater(() -> reachable.set(status));

                System.out.println("checking server activity..." + reachable.get());
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
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

    public synchronized boolean checkConn() {
        if(out == null || in == null)
            return false;
        out.println("0|PING");
        try {
            String response = in.readLine();
            connected = "OK".equals(response);
        } catch (IOException e) {
            connected = false;
        }
        return getConnected();
    }

    // operation methods
    public synchronized boolean checkEmail(String email){
        if(!getConnected() || out == null || in == null)
            return false;
        String response = "";
        out.println("1|" + email);
        try {
            response = in.readLine();
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return "OK".equals(response);
    }

    public synchronized boolean sendMail(String from, String to, String subject, String body){
        if(!getConnected() || out == null || in == null)
            return false;
        String response = "";
        out.println("2|" + from + "|" + to + "|" + subject + "|" + body);
        try {
            response = in.readLine();
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return "MAIL_SENT".equals(response);
    }

    public synchronized List<Mail> getInbox(String email){
        List<Mail> mails = new ArrayList<>();
        if(!getConnected() || out == null || in == null)
            return mails;
        out.println("3|" + email);

        String line;
        try {
            while ((line = in.readLine()) != null && !line.equals("END"))
                mails.add(parseMail(line));
        } catch(IOException e){
            connected = false;
            System.err.println(e);
        }
        return mails;
    }

    public synchronized boolean deleteMail(String email, long id){
        if(!getConnected() || out == null || in == null)
            return false;
        String response = "";
        try {
            out.println("4|" + email + "|" + id);
            response = in.readLine();
        } catch (IOException e) {
            connected = false;
            System.err.println(e);
        }
        return "MAIL_DELETED".equals(response);
    }

    private Mail parseMail(String line) {
        String[] parts = line.split("\\|");

        if(parts.length < 6) return null;
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