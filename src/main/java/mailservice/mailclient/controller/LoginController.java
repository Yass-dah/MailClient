package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.MailModel;
import mailservice.mailclient.network.Client;

import java.io.IOException;

public class LoginController {
    private static final String emailFormat = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private MailApp main;
    private MailModel model;
    private Client client;

    @FXML
    private TextField email;

    @FXML
    private Label formatWarning;

    @FXML
    private Label serverWarning;

    // Setters
    public void setMain(MailApp main) {
        this.main = main;
    }

    public void setModel(MailModel model) {
        this.model = model;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Gestori eventi
    @FXML
    protected void onLoginButtonClick() {
        String email = this.email.getText();
        if(email == null || !email.matches(emailFormat)) {
            formatWarning.setText("please enter a correct format (example@mail.com)");
            return;
        }
        formatWarning.setText("");
        boolean mailExists = client.getConnected() && client.checkEmail(email);
        if(!client.getConnected()) {
            Thread conn = new Thread(() -> client.connect());
            conn.start();
            try {
                conn.join();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            if (!client.getConnected()){
                serverWarning.setText("can't reach server");
                return;
            }
        }
        if(!mailExists)
            serverWarning.setText("Inexistent email");
        else {
            initUser(email);
            try {
                main.inbox();
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void initUser(String email){
        if(model != null) model.setEmail(email);
    }
}