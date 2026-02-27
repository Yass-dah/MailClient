package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.MailModel;
import java.io.IOException;

public class SenderController {
    private MailApp main;
    private MailModel model;

    @FXML
    private TextField mailSubject;

    @FXML
    private TextField senderEmail;

    @FXML
    private TextField receiverEmail;

    @FXML
    private TextField mailText;

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
    protected void onSendButtonClick() {
        try{
            main.inbox();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}