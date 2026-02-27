package mailservice.mailclient.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MailModel {
    private SimpleStringProperty email;
    private ObservableList<Mail> inbox;

    // costruttore
    public MailModel() {
        this.email = new SimpleStringProperty();
        this.inbox = FXCollections.observableArrayList();
    }

    // Getters & Setters
    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public ObservableList<Mail> getInbox() {
        return inbox;
    }

    public void setInbox(ObservableList<Mail> inbox) {
        this.inbox = FXCollections.observableArrayList(inbox);
    }

    // inbox operation methods
    public void addMail(Mail mail) {
        inbox.add(mail);
    }

    public void removeMail(Mail mail) {
        inbox.remove(mail);
    }

    // stampa
    @Override
    public String toString() {
        return "MailModel{" +
                "email=" + email +
                ", inbox=" + inbox +
                '}';
    }
}
