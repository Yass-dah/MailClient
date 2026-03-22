package mailservice.mailclient.controller;

import javafx.collections.FXCollections;
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

    // Gestore login
    @FXML
    protected void onLoginButtonClick() {
        String email = this.email.getText();
        if(email == null || !email.matches(emailFormat)) {
            formatWarning.setText("please enter a correct format (example@mail.com)");
            return;
        }
        formatWarning.setText("");
        if(!client.getConnectionLooper().isReachable()) {
            serverWarning.setText("can't reach server");
            return;
        }
        boolean emailExists = client.checkEmail(email);
        if(!emailExists) {
            serverWarning.setText("Inexistent email");
        } else {
            initUser(email);
            model.setInbox(FXCollections.observableArrayList(client.getInbox(email)));
            try {
                main.inbox();
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // inizializzatore email per login
    public void initUser(String email){
        if(model != null) model.setEmail(email);
    }
}