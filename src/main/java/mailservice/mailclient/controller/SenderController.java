package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;
import java.io.IOException;

public class SenderController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
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
    private Button submit;

    @FXML
    private Label formatWarning;

    // Getters & Setters
    public MailApp getMain() {
        return main;
    }

    public void setMain(MailApp main) {
        this.main = main;
    }

    public MailModel getModel() {
        return model;
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

    public void forwardMail(Mail mail){
        submit.setText("FORWARD");
        senderEmail.setText(mail.getFrom());
        mailSubject.setText(mail.getSubject());
        mailText.setText(mail.getBody());
    }
}