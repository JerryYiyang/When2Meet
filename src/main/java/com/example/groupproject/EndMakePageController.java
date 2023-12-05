package com.example.groupproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;

public class EndMakePageController {
    @FXML
    private Label confirmationLabel;
    @FXML
    private Button returnButton;

    public void initialize() {
        // Display the confirmation message
        confirmationLabel.setText("Your event has been successfully created!");
    }

    @FXML
    private void onReturnButtonClick() throws IOException {
        // Code to return to the main page or relevant section
        Main.loadStartPage();
    }
}
