package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.MailModel;

import java.io.IOException;

public class LoginController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private MailApp main;
    private MailModel model;

    @FXML
    private TextField email;

    @FXML
    private Label formatWarning;

    // Setters
    public void setMain(MailApp main) {
        this.main = main;
    }

    public void setModel(MailModel model) {
        this.model = model;
    }

    // Gestori eventi
    @FXML
    protected void onLoginButtonClick() {
        String email = this.email.getText();
        if(email != null && email.matches(EMAIL_REGEX)) {
            initUser(email);
            try {
                main.inbox();
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        } else formatWarning.setText("please enter a correct format (example@mail.com)");
    }

    public void initUser(String email){
        if(model != null) model.setEmail(email);
    }
}