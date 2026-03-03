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
    private static final String receiversFormat =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(,[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})*$";
    private MailApp main;
    private MailModel model;

    @FXML
    private TextField mailSubject;

    @FXML
    private Label senderEmail;

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
        if(receiverEmail != null && receiverEmail.getText().matches(receiversFormat)) {
            try {
                main.inbox();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else formatWarning.setText("Please enter valid addresses divided by a ','");
    }

    public void initMail(Mail mail){
        senderEmail.setText(model.getEmail());
        mailSubject.setFocusTraversable(mail.getSubject() == null ? true : false);
        submit.setText(mail.getSubject() == null ? "SEND" : "FORWARD");
        receiverEmail.setText(mail.getTo() == null ? "" : mail.getTo());
        mailSubject.setText(mail.getSubject() == null ? "" : mail.getSubject());
        mailText.setText(mail.getBody() == null ? "" : mail.getBody());
    }
}