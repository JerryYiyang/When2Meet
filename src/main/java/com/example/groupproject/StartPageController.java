package com.example.groupproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
                int eLength = Integer.parseInt(Main.makeInputPopup("How long is the event? (in hours)"));
                calculateBestTime(eid, eLength);
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

    private void calculateBestTime(String eid, int eventLength) {
        ArrayList<String> dates = connect.getDates(eid); // all possible dates for the event
        ArrayList<ArrayList<String>> times = new ArrayList<>();
        for(String d : dates)
        {
            times.add(connect.getAvailability(eid, d)); // all times signed up for
        }

        ArrayList<int[]> weightedTimes = new ArrayList<>();
        for(int i = 0; i < dates.size(); i++)
        {
            weightedTimes.add(new int[24]);
            Arrays.fill(weightedTimes.get(i), 0);
        }
        for(int i = 0; i < dates.size(); i++)
        {
            for(int j = 0; j < times.get(i).size(); j++)
            {
                String timeString = times.get(i).get(j);
                System.out.println("timeString: " + timeString);
                String[] individTimes = timeString.split(" ");
                for(int k = 0; k < individTimes.length; k++)
                {
                    weightedTimes.get(i)[Integer.parseInt(individTimes[k])] += 1;
                }
            }
        }

        for(int i = 0; i < weightedTimes.size(); i++)
        {
            System.out.println("day: " + dates.get(i));
            for(int j = 0; j < weightedTimes.get(i).length; j++)
            {
                System.out.print(weightedTimes.get(i)[j] + " ");
            }
        }

        calculateBestTimeBlock(weightedTimes, eventLength, dates);
    }

    private void calculateBestTimeBlock(ArrayList<int[]> allTimes, int eventLength, ArrayList<String> dates) {
        int maxPeople = 0;
        int listIndex = -1;
        int startTime = -1;
        for(int t = 0; t < allTimes.size(); t++)
        {
            int[] times = allTimes.get(t);
            int currPeople = 0;
            for(int i = 0; i < eventLength; i++)
            {
                currPeople += times[i];
            }
            if(currPeople > maxPeople) {
                maxPeople = currPeople;
                listIndex = t;
                startTime = 0;
            }

            for(int i = eventLength; i < 24; i++)
            {
                currPeople -= times[i-eventLength]; // remove first hour
                currPeople += times[i]; // add last hour
                if(currPeople > maxPeople) {
                    maxPeople = currPeople;
                    listIndex = t;
                    startTime = i-eventLength+1;
                }
            }
        }

        if(listIndex != -1 && startTime != -1)
            Main.makeInfoPopup("Ideal day & time: " + dates.get(listIndex) + " from " + startTime + ":00 - " + (startTime+eventLength) + ":00");
        else
            Main.makeInfoPopup("No ideal date found");
    }
}