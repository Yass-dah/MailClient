module mailservice.mailclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens mailservice.mailclient to javafx.fxml;
    exports mailservice.mailclient;
}