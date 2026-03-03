package mailservice.mailclient.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;

import java.io.IOException;
import java.time.LocalDateTime;

public class InboxController {
    private MailApp main;
    private MailModel model;

    @FXML
    private Label currentEmail;

    @FXML
    private Label connection;

    @FXML
    private ListView<Mail> inboxList;

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

    // Getters & 0Setters
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
        inboxList.setItems(model.getInbox()); // Binding
    }

    // Gestori eventi
    @FXML
    protected void onLogoutButtonClick() {
        Mail mail = new Mail();
        mail.setId(1);
        mail.setFrom("prof@uni.it");
        mail.setTo("studente@uni.it");
        mail.setSubject("Esame");
        mail.setBody("Domani alle 9");
        mail.setDate(LocalDateTime.now());
        model.getInbox().add(mail);

        Mail mail2 = new Mail();
        mail2.setId(1);
        mail2.setFrom("prof2@uni.it");
        mail2.setTo("studente@uni.it");
        mail2.setSubject("Esame");
        mail2.setBody("Domani alle 11");
        mail2.setDate(LocalDateTime.now());
        model.getInbox().add(mail2);
    }

    @FXML
    protected void onDeleteButtonClick() {}

    @FXML
    protected void onReplyButtonClick() {
        if(mailText.getText() == null || mailText.getText().isBlank()) return;
        Mail mail = model.getReplyMail(senderEmail.getText());
        try{
            main.sender(mail);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void onReplyAllButtonClick() {
        if(mailText.getText() == null || mailText.getText().isBlank()) return;
        Mail mail = model.getReplyAllMail(senderEmail.getText()+","+receiverEmail.getText());
        try{
            main.sender(mail);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void onForwardButtonClick() {
        if(mailText.getText() == null || mailText.getText().isBlank()) return;
        Mail mail = model.getForwardMail(mailSubject.getText(), mailText.getText());
        try{
            main.sender(mail);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void onNewMailButtonClick() {
        if(model.getEmail() == null) return;
        try{
            main.sender(model.getSendMail());
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void bindProperties(){
        if(model == null) return;
        currentEmail.textProperty().bind(model.emailProperty());
        inboxList.getSelectionModel().selectedItemProperty().addListener((obs, oldMail, newMail) -> {
            if(oldMail != null){
                mailDate.textProperty().unbind();
                mailSubject.textProperty().unbind();
                senderEmail.textProperty().unbind();
                receiverEmail.textProperty().unbind();
                mailText.textProperty().unbind();
            }
            if(newMail != null){
                mailDate.textProperty().bind(newMail.dateProperty().asString());
                mailSubject.textProperty().bind(newMail.subjectProperty());
                senderEmail.textProperty().bind(newMail.fromProperty());
                receiverEmail.textProperty().bind(Bindings.createStringBinding(
                        () -> String.join(", ", newMail.getTo()),
                        newMail.toProperty()
                ));
                mailText.textProperty().bind(newMail.bodyProperty());
            }
        });
    }
}