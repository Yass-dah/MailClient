package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;
import mailservice.mailclient.network.Client;

import java.io.IOException;
import java.lang.String;

public class SenderController {
    private static final String receiversFormat =
            "^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}(,[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,})*$";
    private MailApp main;
    private MailModel model;
    private Client client;

    @FXML
    private Label emptyWarning;

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
    protected void onInboxButtonClick() {
        try{
            main.inbox();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // NOTA: non gestisce spam su 1 o più dest. uguali
    @FXML
    protected void onSendButtonClick() {
        boolean validSubject = mailSubject != null && !mailSubject.getText().isEmpty();
        boolean validBody = mailText != null && !mailText.getText().isEmpty();
        boolean validReceivers = receiverEmail != null && receiverEmail.getText().matches(receiversFormat);
        if(!validReceivers) {
            formatWarning.setText("Please enter valid addresses divided by a ','");
            return;
        }
        if (!validSubject || !validBody) {
            emptyWarning.setText("Subject and Body cannot be empty");
            return;
        }
        boolean mailExists = client.getConnectionLooper().isReachable();
        String[] emails = receiverEmail.getText().split(",");
        for(String e : emails)
            mailExists = mailExists && client.checkEmail(e);
        if(!mailExists)
            formatWarning.setText("One or more of the following emails were not found");
        else{
            try {
                new Thread(() -> client.sendMail(model.getEmail(), receiverEmail.getText(), mailSubject.getText(), mailText.getText())).start();
                main.inbox();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // inizializzatore mail
    public void initMail(Mail mail) {
        senderEmail.setText(model.getEmail());
        mailSubject.setFocusTraversable(mail.getSubject() == null);
        if(mail.getSubject() != null && mail.getBody() == null){
            submit.setText("REPLY");
            receiverEmail.setFocusTraversable(false);
        } else if(mail.getSubject() != null && mail.getBody() != null)
            submit.setText("FORWARD");
        receiverEmail.setText(mail.getTo() == null ? "" : mail.getTo());
        mailSubject.setText(mail.getSubject() == null ? "" : mail.getSubject());
        mailText.setText(mail.getBody() == null ? "" : mail.getBody());
    }
}