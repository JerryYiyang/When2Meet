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
        /* TODO: (done) Get day data for the event */
        ArrayList<ArrayList<Integer>> possibleTimes = new ArrayList<>();
        ArrayList<String> possibleDatesString = connect.getDates();
        ArrayList<String> allTimes = connect.getPossibleTimes();
        for(int i = 0; i < possibleDatesString.size(); i++){
            ArrayList<Integer> ti = new ArrayList<>();
            String[] t = allTimes.get(i).split(" ");
            for(int j = 0; j < allTimes.size(); j++){
                ti.add(Integer.valueOf(t[j]));
            }
            possibleTimes.add(ti);
        }

        ArrayList<LocalDate> possibleDates = new ArrayList<>();
        for (String s : possibleDatesString) {
            possibleDates.add(LocalDate.parse(s));
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
        ArrayList<LocalDate> selectedDates = data.getKey();
        ArrayList<ArrayList<Integer>> selectedTimes = data.getValue();
        /* TODO: create new user in database */

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
