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

public class MakePageController {
    public TextField eventNameTextField;
    public TextField eventIDTextField;

    public GridPane calendarGrid;
    public Label monthYearLabel;
    public PieChart clockPieChart;
    public ToggleButton amToggleButton;
    public ToggleButton pmToggleButton;
    private Calendar cal;

    private static DatabaseConnection connect = DatabaseConnection.getInstance();

    public void initialize()
    {
        cal = new Calendar(calendarGrid, clockPieChart, monthYearLabel);
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

    public void onCreateButtonClick() throws IOException {
        Pair<ArrayList<LocalDate>, ArrayList<ArrayList<Integer>>> data = cal.getCalendarData();
        ArrayList<LocalDate> selectedDatesD = data.getKey();
        ArrayList<ArrayList<Integer>> selectedTimes = data.getValue();
        /* TODO: (done?) make new event */
        ArrayList<String> selectedDates = new ArrayList<>();
        for (LocalDate localDate : selectedDatesD) {
            selectedDates.add(localDate.toString());
        }
        String ID = eventIDTextField.getText();
        connect.addEvent(ID, eventNameTextField.getText());
        connect.enterDates(selectedDates);
        for(int i = 0; i < selectedDates.size(); i++){
            String times = "";
            for(Integer time : selectedTimes.get(i)){
                times += Integer.toString(time);
                times += " ";
            }
            connect.setPossibleTimes(selectedDates.get(i), ID, times);
        }
        Main.loadEndMakePage();
    }

    public void onNameTextFieldType() {
        /* TODO: (done!) ensure the ID is not already used; if it is, then change ID to ID+i, for example birthday2, or birthday3 */
        String s = eventNameTextField.getText();
        int extra = 1;
        if(!connect.checkID(s)) {
            eventIDTextField.setText(s);
            return;
        }

        while(connect.checkID(s + extra))
        {
            extra++;
        }
        if(s.equals(eventIDTextField.getText())) // make sure it hasn't changed
            eventIDTextField.setText(s + extra);
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
