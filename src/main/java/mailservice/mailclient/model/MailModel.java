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
        this.inbox = (inbox != null) ? FXCollections.observableArrayList(inbox) : null;
    }

    // inbox operation methods
    public void addMail(Mail mail) {
        inbox.add(mail);
    }

    public void removeMail(long id) {
        inbox.removeIf(mail -> mail.getId() == id);
    }

    // inizializzatore per forward
    public Mail getForwardMail(String subject, String body) {
        Mail mail = new Mail();
        mail.setFrom(email.get());
        mail.setSubject(subject);
        mail.setBody(body);
        return mail;
    }

    // inizializzatore per reply
    public Mail getReplyMail(String to, String subject) {
        Mail mail = new Mail();
        mail.setFrom(email.get());
        mail.setTo(to);
        mail.setSubject("Risposta: " + subject);
        return mail;
    }

    // inizializzatore per reply all
    public Mail getReplyAllMail(String to, String subject) {
        Mail mail = new Mail();
        mail.setFrom(email.get());
        mail.setTo(to);
        mail.setSubject("Risposta: " + subject);
        return mail;
    }

    // inizializzatore per new mail
    public Mail getSendMail() {
        Mail mail = new Mail();
        mail.setFrom(email.get());
        return mail;
    }

    // max id delle inbox (ovvero ultimo)
    public long getLastId(){
        long max = -1;
        if(inbox != null)
            for (Mail mail : inbox)
                if (mail.getId() > max) max = mail.getId();
        return max;
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
