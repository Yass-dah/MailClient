package mailservice.mailclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InboxController {
    @FXML
    private Label formatWarning;

    @FXML
    protected void onLoginButtonClick() {
        formatWarning.setText("please enter a correct format (example@mail.com)");
    }
}