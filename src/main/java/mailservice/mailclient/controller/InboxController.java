package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class InboxController {
    private MailApp main;
    private MailModel model;

    @FXML
    private Label currentEmail;

    @FXML
    private Label connection;

    @FXML
    private ListView inboxList;

    @FXML
    private Label mailDate;

    @FXML
    private Label mailSubject;

    @FXML
    private Label senderEmail;

    @FXML
    private Label receiverEmail;

    @FXML
    private Label mailText;

    // Setters
    public void setMain(MailApp main) {
        this.main = main;
    }

    public void setModel(MailModel model) {
        this.model = model;
        inboxList.setItems(model.getInbox()); // Binding
    }

    // Gestori eventi
    @FXML
    protected void onLogoutButtonClick() {
        Mail mail = new Mail();
        mail.setId(1);
        mail.setFrom("prof@uni.it");
        mail.setTo(new ArrayList<String>(Collections.singleton("studente@uni.it")));
        mail.setSubject("Esame");
        mail.setBody("Domani alle 9");
        mail.setDate(LocalDateTime.now());
        model.getInbox().add(mail);

        Mail mail2 = new Mail();
        mail2.setId(1);
        mail2.setFrom("prof2@uni.it");
        mail2.setTo(new ArrayList<String>(Collections.singleton("studente@uni.it")));
        mail2.setSubject("Esame");
        mail2.setBody("Domani alle 11");
        mail2.setDate(LocalDateTime.now());
        model.getInbox().add(mail2);
    }

    @FXML
    protected void onDeleteButtonClick() {}

    @FXML
    protected void onReplyButtonClick() {}

    @FXML
    protected void onReplyAllButtonClick() {}

    @FXML
    protected void onForwardButtonClick() {}

    @FXML
    protected void onNewMailButtonClick() {
        try{
            main.sender();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}