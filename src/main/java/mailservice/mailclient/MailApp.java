package mailservice.mailclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mailservice.mailclient.controller.InboxController;
import mailservice.mailclient.controller.LoginController;
import mailservice.mailclient.controller.SenderController;
import mailservice.mailclient.model.Mail;
import mailservice.mailclient.model.MailModel;
import mailservice.mailclient.network.Client;
import java.io.IOException;

// main che verra passato dinamicamente al controller una volta loadata la scena e ottenuto i controller
public class MailApp extends Application {
    // main stage non statico che usa diverse scene
    private Stage mainStage;
    private MailModel model;
    private Client client;
    private Thread connectionChecker;

    // start con la pagina di login, scena = login
    @Override
    public void start(Stage stage) throws IOException {
        model = new MailModel();
        mainStage = stage;
        client = new Client();
        new Thread(() -> client.connect()).start();
        connectionChecker = new Thread(client.getConnectionLooper());
        connectionChecker.start();
        login();
    }

    public void login() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MailApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();
        controller.setMain(this);
        controller.setModel(model);
        controller.setClient(client);
        mainStage.setTitle("Mail Login");
        mainStage.setScene(scene);
        mainStage.show();
    }

    // scena = inbox
    public void inbox() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MailApp.class.getResource("inbox-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        InboxController controller = fxmlLoader.getController();
        controller.setMain(this);
        controller.setModel(model);
        controller.setClient(client);
        controller.bindProperties();
        mainStage.setTitle("Your mail inbox");
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.centerOnScreen();
    }

    // scena = sender
    public void sender(Mail mail) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MailApp.class.getResource("send-view.fxml"));
        String title = "Send a mail";
        Scene scene = new Scene(fxmlLoader.load());
        SenderController controller = fxmlLoader.getController();
        controller.setMain(this);
        controller.setModel(model);
        controller.setClient(client);
        controller.initMail(mail);
        mainStage.setTitle(title);
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}