package com.example.groupproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class StartPageController {
    @FXML
    public TextField eventID;
    private static DatabaseConnection connect = DatabaseConnection.getInstance();

    @FXML
    private void onJoinButtonClick() throws IOException {
        /* TODO: (done!) Validate event ID using database */
        String eid;
        boolean admin = eventID.getText().startsWith("*");
        if(admin)
            eid = eventID.getText().substring(1);
        else
            eid = eventID.getText();

        if(connect.checkID(eid))
        {
            if(admin) {
                calculateBestTime(eid);
            } else {
                Main.setCurrentEventID(eid);
                Main.loadJoinPage();
            }
        }
        else {
            Main.makeErrorPopup("Invalid event ID");
        }
    }

    @FXML
    private void onMakeButtonClick() throws IOException {
        Main.loadMakePage();
    }

    private void calculateBestTime(String eid) {
        // TO-DO: get data for all users, all available dates and all available times
        ArrayList<ArrayList<String>> dbAllDates;
        ArrayList<ArrayList<String>> dbAllTimes;


    }
}