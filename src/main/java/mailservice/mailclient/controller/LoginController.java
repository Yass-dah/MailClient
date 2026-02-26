package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import mailservice.mailclient.MailApp;

public class LoginController {
    private MailApp main;

    @FXML
    private TextField email;

    @FXML
    private Label formatWarning;

    public void setMain(MailApp main) {
        this.main = main;
    }

    @FXML
    protected void onLoginButtonClick() {
        String email = this.email.getText();

        if(email != null) {
            try {
                main.inbox();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        //formatWarning.setText("please enter a correct format (example@mail.com)");
    }
}