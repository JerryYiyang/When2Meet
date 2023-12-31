package com.example.groupproject;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JoinPageController {

    public TextField userNameTextField;
    public Label monthYearLabel;
    public GridPane calendarGrid;
    public PieChart clockPieChart;
    public ToggleButton amToggleButton;
    public ToggleButton pmToggleButton;
    private Calendar cal;

    private static DatabaseConnection connect = DatabaseConnection.getInstance();

    public void initialize()
    {
        String eid = Main.getCurrentEventID();
        /* TODO: (done) Get day data for the event */
        ArrayList<String> dbDates = connect.getDates(eid);
        ArrayList<String> dbTimes = connect.getPossibleTimes(eid);

        //if(dbDates.size() != dbTimes.size())
        System.out.println("dates size: " + dbDates.size() + " times size: " + dbTimes.size());

        ArrayList<LocalDate> possibleDates = new ArrayList<>();
        ArrayList<ArrayList<Integer>> possibleTimes = new ArrayList<>();

        // convert date strings to LocalDate
        for (String s : dbDates) {
            possibleDates.add(LocalDate.parse(s));
        }

        // convert time strings to ArrayList<Integer>
        for(int i = 0; i < dbTimes.size(); i++)
        {
            ArrayList<Integer> ti = new ArrayList<>();
            String[] t = dbTimes.get(i).split(" ");
            for(int j = 0; j < t.length; j++)
            {
                ti.add(Integer.valueOf(t[j]));
            }
            possibleTimes.add(ti);
        }

        cal = new Calendar(calendarGrid, clockPieChart, monthYearLabel, possibleDates, possibleTimes);
        onPMButtonClick();
        greenButton(pmToggleButton);
        grayButton(amToggleButton);
    }

    public void onLeftButtonClick() {
        cal.prevMonth();
    }

    public void onRightButtonClick() {
        cal.nextMonth();
    }

    public void onJoinButtonClick() throws IOException {
        Pair<ArrayList<LocalDate>, ArrayList<ArrayList<Integer>>> data = cal.getCalendarData();

        // data
        ArrayList<LocalDate> selectedDates = data.getKey();
        ArrayList<ArrayList<Integer>> selectedTimes = data.getValue();
        String userName = userNameTextField.getText();
        String eid = Main.getCurrentEventID();

        for(ArrayList<Integer> a : selectedTimes)
        {
            if(a.isEmpty()) {
                Main.makeErrorPopup("Missing time data");
                return;
            }
        }
        if(selectedDates.isEmpty() || selectedTimes.isEmpty())
        {
            Main.makeErrorPopup("No dates/times selected");
            return;
        }
        if(userName.isEmpty()) {
            Main.makeErrorPopup("No name entered");
            return;
        }

        /* TODO: (done) create new user in database */
        if(connect.checkPerson(userName)){
            /*TODO: konrad look over the logic of this, i took the main code from makepagecontroller*/
            for(int i = 0; i < selectedDates.size(); i++){
                StringBuilder times = new StringBuilder();
                for(Integer time : selectedTimes.get(i)){
                    times.append(Integer.toString(time));
                    times.append(" ");
                }
                connect.updateAvailability(times.toString(), userName, eid, String.valueOf(selectedDates.get(i)));
            }
        }else{
            connect.addPerson(userName, eid);
            for(int i = 0; i < selectedDates.size(); i++){
                StringBuilder times = new StringBuilder();
                for(Integer time : selectedTimes.get(i)){
                    times.append(Integer.toString(time));
                    times.append(" ");
                }
                connect.enterAvailability(times.toString(), userName, eid, String.valueOf(selectedDates.get(i)));
            }
        }
        Main.loadEndJoinPage();
    }

    public void onPMButtonClick() {
        if(cal.getAM())
        {
            amToggleButton.setSelected(false);
            greenButton(pmToggleButton);
            grayButton(amToggleButton);
            cal.switchToPM();
        }
        else {
            pmToggleButton.setSelected(false);
        }
    }

    public void onAMButtonClick() {
        if(!cal.getAM())
        {
            pmToggleButton.setSelected(false);
            greenButton(amToggleButton);
            grayButton(pmToggleButton);
            cal.switchToAM();
        }
        else {
            amToggleButton.setSelected(false);
        }
    }

    private void greenButton(ToggleButton button)
    {
        button.setStyle("-fx-background-color: lightgreen;");
    }

    private void grayButton(ToggleButton button)
    {
        button.setStyle("-fx-background-color: lightgrey;");
    }
}
