package com.example.groupproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import src.main.java.com.example.groupproject.DatabaseConnection;
import src.main.java.com.example.groupproject.Main;

import java.io.IOException;

public class StartPageController {
    @FXML
    public TextField eventID;
    private static DatabaseConnection connect = DatabaseConnection.getInstance();

    @FXML
    private void onJoinButtonClick() throws IOException {
        /* TODO: Validate event ID using database */
        if(connect.checkID(eventID.getText()))
        {
            Main.loadJoinPage();
        }
        else {
            Main.makeErrorPopup("Invalid event ID");
        }
    }

    @FXML
    private void onMakeButtonClick() throws IOException {
        Main.loadMakePage();
    }
}