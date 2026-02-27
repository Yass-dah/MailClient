package mailservice.mailclient.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Mail {
    private SimpleLongProperty id;
    private SimpleStringProperty from;
    private ArrayList<String> to;
    private SimpleStringProperty subject;
    private SimpleStringProperty body;
    private LocalDateTime date;

    // costruttore
    public Mail() {
        this.id = new SimpleLongProperty();
        this.from = new SimpleStringProperty();
        this.to = new ArrayList<>();
        this.subject = new SimpleStringProperty();
        this.body = new SimpleStringProperty();
        this.date = LocalDateTime.now();
    }

    // Getters & Setters
    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getFrom() {
        return from.get();
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public ArrayList<String> getTo() {
        return to;
    }

    public void setTo(ArrayList<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getBody() {
        return body.get();
    }

    public SimpleStringProperty bodyProperty() {
        return body;
    }

    public void setBody(String body) {
        this.body.set(body);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // mail operation methods
    public void add(String to) {
        this.to.add(to);
    }

    public void remove(String to) {
        this.to.remove(to);
    }

    // stampa
    @Override
    public String toString() {
        return subject.get() + " FROM " + from.get();
    }
}