package mailservice.mailclient.network;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Client {
    private static final String serverHost = "localhost";
    private static final int serverPort = 50000;
    private ConnectionLooper conn = new ConnectionLooper();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // Getters
    public ConnectionLooper getConnectionLooper(){
        return conn;
    }

    // Connessione al server
    public void connect() throws IOException {
        socket = new Socket(serverHost, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    // classe aggiuntiva per verifica connessione periodica eseguita da un thread
    public class ConnectionLooper implements Runnable {
        private MailModel model;
        private volatile boolean running = true;
        private volatile SimpleBooleanProperty reachable = new SimpleBooleanProperty(false);

        // model setter
        public void setModel(MailModel model) {
            this.model = model;
        }

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
                Platform.runLater(() -> reachable.set(status));
                System.out.println("checking server activity..." + reachable.get());
                if(status && model != null)
                    Platform.runLater(()-> model.getInbox().addAll(updateInbox(model.getEmail(), model.getLastId())));
                try {
                    Thread.sleep(3000);
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
            System.err.println(e.getMessage());
        }
    }

    public synchronized boolean checkConn() {
        try {
            connect();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    // operation methods
    public synchronized boolean checkEmail(String email){
        try {
            connect();
            String response = "";
            out.println("1|" + email);
            response = in.readLine();
            return "OK".equals(response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    public synchronized boolean sendMail(String from, String to, String subject, String body){
        try {
            connect();
            String response = "";
            out.println("2|" + from + "|" + to + "|" + subject + "|" + body);
            response = in.readLine();
            return "MAIL_SENT".equals(response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    public synchronized ArrayList<Mail> getInbox(String email){
        ArrayList<Mail> mails = new ArrayList<>();
        try {
            connect();
            out.println("3|" + email + "|" + -1);
            String line;
            while ((line = in.readLine()) != null && !line.equals("END"))
                mails.add(parseMail(line));
        } catch(IOException e){
            System.err.println(e.getMessage());
        } finally {
            disconnect();
        }
        return mails;
    }

    public synchronized ArrayList<Mail> updateInbox(String email, long lastId){
        ArrayList<Mail> mails = new ArrayList<>();
        try {
            connect();
            out.println("3|" + email + "|" + lastId);
            String line;
            while ((line = in.readLine()) != null && !line.equals("END"))
                mails.add(parseMail(line));
        } catch(IOException e){
            System.err.println(e.getMessage());
        } finally {
            disconnect();
        }
        return mails;
    }

    public synchronized boolean deleteMail(String email, long id){
        try {
            connect();
            String response = "";
            out.println("4|" + email + "|" + id);
            response = in.readLine();
            return "MAIL_DELETED".equals(response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
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