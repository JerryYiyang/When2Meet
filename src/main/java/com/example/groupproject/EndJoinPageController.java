package com.example.groupproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;

public class EndJoinPageController {
    @FXML
    private Label confirmationLabel;
    @FXML
    private Button returnButton;

    public void initialize() {
        // Display the confirmation message
        confirmationLabel.setText("You have successfully joined the event!");
    }

    @FXML
    private void onReturnButtonClick() throws IOException {
        // Code to return to the main page or relevant section
        Main.loadStartPage();
    }
}
