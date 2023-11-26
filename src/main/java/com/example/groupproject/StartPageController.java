package com.example.groupproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import java.io.IOException;
import java.util.Objects;

public class StartPageController {
    @FXML
    public TextField eventID;

    @FXML
    private void onJoinButtonClick() throws IOException {
        /* TODO: Validate event ID using database */
        if(Objects.equals(eventID.getText(), ""))
        {
            Main.makeErrorPopup("Invalid event ID");
        }
        else {
            Main.loadJoinPage();
        }
    }

    @FXML
    private void onMakeButtonClick() throws IOException {
        Main.loadMakePage();
    }
}