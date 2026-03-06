package mailservice.mailclient.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Mail {
    private SimpleLongProperty id;
    private SimpleStringProperty from;
    private SimpleStringProperty to;
    private SimpleStringProperty subject;
    private SimpleStringProperty body;
    private ObjectProperty<LocalDateTime> date;

    // costruttori
    public Mail() {
        this.id = new SimpleLongProperty();
        this.from = new SimpleStringProperty();
        this.to = new SimpleStringProperty();
        this.subject = new SimpleStringProperty();
        this.body = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>(LocalDateTime.now());
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

    public String getTo() {
        return to.get();
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
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
        return date.get();
    }

    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    // inizializzatori
    public void initDateNow(){
        setDate(LocalDateTime.now());
    }

    // stampa
    @Override
    public String toString() {
        return subject.get() + " FROM " + from.get();
    }
}