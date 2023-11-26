package com.example.groupproject;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JoinPageController {

    public TextField userNameTextField;
    public Label monthYearLabel;
    public GridPane calendarGrid;
    private Calendar cal;

    public void initialize()
    {
        /* TODO: Get day data for the event */
        ArrayList<LocalDate> possibleDates = new ArrayList<>();
        possibleDates.add(LocalDate.now());
        cal = new Calendar(calendarGrid, monthYearLabel, possibleDates);
    }

    public void onLeftButtonClick() {
        cal.prevMonth();
    }

    public void onRightButtonClick() {
        cal.nextMonth();
    }

    public void onJoinButtonClick() throws IOException {
        Main.loadEndJoinPage();
    }
}
