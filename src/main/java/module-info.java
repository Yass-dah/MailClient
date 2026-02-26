module mailservice.mailclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens mailservice.mailclient to javafx.fxml;
    exports mailservice.mailclient;
    exports mailservice.mailclient.controller;
    opens mailservice.mailclient.controller to javafx.fxml;
}