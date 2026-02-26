package mailservice.mailclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mailservice.mailclient.controller.InboxController;
import mailservice.mailclient.controller.LoginController;

import java.io.IOException;

// main che verra passato dinamicamente al controller una volta loadata la scena e ottenuto i controller
public class MailApp extends Application {
    // main stage non statico che usa diverse scene
    private Stage mainStage;

    // start con la pagina di login, scena = login
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MailApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();
        controller.setMain(this);
        stage.setTitle("Mail Login");
        stage.setScene(scene);
        stage.show();
    }

    // scena = inbox
    public void inbox() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MailApp.class.getResource("inbox-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        InboxController controller = fxmlLoader.getController();
        mainStage.setTitle("Your mail inbox");
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}