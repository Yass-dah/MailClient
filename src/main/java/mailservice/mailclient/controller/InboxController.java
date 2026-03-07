package mailservice.mailclient.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Paint;
import mailservice.mailclient.MailApp;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;
import mailservice.mailclient.network.Client;

import java.io.IOException;

public class InboxController {
    private MailApp main;
    private MailModel model;
    private Client client;

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
        inboxList.setItems(model.getInbox()); // Binding
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Gestori eventi
    @FXML
    protected void onLogoutButtonClick() {
        model.setEmail(null);
        model.setInbox(null);
        try{
            main.login();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
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
        System.out.println(model.getInbox());
        if (client != null && client.getConnectionLooper() != null) {
            connection.setTextFill(Paint.valueOf(client.getConnectionLooper().isReachable() ? "#06A106" : "#d70000"));
            connection.setText(client.getConnectionLooper().isReachable() ? "Online" : "Offline");
            client.getConnectionLooper().reachableProperty().addListener((obs, oldValue, newValue) -> {
                connection.setTextFill(Paint.valueOf(newValue ? "#06A106" : "#d70000"));
                connection.setText(newValue ? "Online" : "Offline");
            });// stato connessione : online - offline
        }
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