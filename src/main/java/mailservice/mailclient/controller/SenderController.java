package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;
import mailservice.mailclient.network.Client;

import java.io.IOException;

public class SenderController {
    private static final String receiversFormat =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(,[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})*$";
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCloseButtonClick() {
        Stage stage = (Stage) submit.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            try {
                main.inbox();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    // Gestori eventi
    @FXML
    protected void onSendButtonClick() {
        boolean validSubject = mailSubject != null && !mailSubject.getText().isEmpty();
        boolean validBody = mailText != null && !mailText.getText().isEmpty();
        boolean validReceivers = receiverEmail != null && receiverEmail.getText().matches(receiversFormat);
        if(!validReceivers) formatWarning.setText("Please enter valid addresses divided by a ','");
        else if (!validSubject || !validBody) emptyWarning.setText("Subject and Body cannot be empty");
        else{
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMail(model.getEmail(), receiverEmail.getText(), mailSubject.getText(), mailText.getText());
                    }
                }).start();
                main.inbox();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void initMail(Mail mail){
        senderEmail.setText(model.getEmail());
        mailSubject.setFocusTraversable(mail.getSubject() == null);
        submit.setText(mail.getSubject() == null ? "SEND" : "FORWARD");
        receiverEmail.setText(mail.getTo() == null ? "" : mail.getTo());
        mailSubject.setText(mail.getSubject() == null ? "" : mail.getSubject());
        mailText.setText(mail.getBody() == null ? "" : mail.getBody());
    }
}