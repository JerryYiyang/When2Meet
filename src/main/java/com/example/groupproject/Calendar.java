package com.example.groupproject;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Calendar {
    private YearMonth selectedMonth;

    private ArrayList<LocalDate> selectedDates;
    private ArrayList<LocalDate> highlightedDates;

    @FXML
    private GridPane calendarGrid;
    @FXML
    private Label monthYearLabel;

    public Calendar(GridPane cal, Label moye) // for make page
    {
        this(moye, cal);
        updateCalendar();
    }

    public Calendar(GridPane cal, Label moye, ArrayList<LocalDate> highlighted) // for join page
    {
        this(moye, cal);
        highlightedDates = highlighted;
        updateCalendar();
    }

    private Calendar(Label moye, GridPane cal)
    {
        calendarGrid = cal;
        monthYearLabel = moye;
        selectedMonth = YearMonth.now();
        selectedDates = new ArrayList<>();
    }

    public void prevMonth()
    {
        selectedMonth = selectedMonth.minusMonths(1);
        updateCalendar();
    }

    public void nextMonth()
    {
        selectedMonth = selectedMonth.plusMonths(1);
        updateCalendar();
    }

    private void updateCalendar()
    {
        calendarGrid.getChildren().clear();

        int dayOfWeek = selectedMonth.atDay(1).getDayOfWeek().getValue();
        if(dayOfWeek == 7) dayOfWeek = 0; // Sunday: 0, ..., Saturday: 6

        int weekOfMonth = 0;
        int daysInMonth = selectedMonth.lengthOfMonth();
        int currDay = 1;
        while(currDay <= daysInMonth)
        {
            String dayString = (currDay < 10) ? ("  " + currDay) : ("" + currDay);
            Label dayLabel = new Label(dayString);
            dayLabel.setOnMouseClicked(this::dayLabelClicked);
            LocalDate thisDay = LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonthValue(), currDay);
            if(selectedDates.contains(thisDay))
                greenDay(dayLabel);
            else if(highlightedDates != null && highlightedDates.contains(thisDay))
                yellowDay(dayLabel);
            else
                redDay(dayLabel);

            calendarGrid.add(dayLabel, dayOfWeek, weekOfMonth);

            if(dayOfWeek == 6)
            {
                weekOfMonth++;
                dayOfWeek = 0;
            }
            else {
                dayOfWeek++;
            }
            currDay++;
        }

        Label emptyLabel;
        while(weekOfMonth <= 7)
        {
            emptyLabel = new Label("");
            calendarGrid.add(emptyLabel, 0, weekOfMonth);
            weekOfMonth++;
        }

        monthYearLabel.setText(selectedMonth.format(DateTimeFormatter.ofPattern("MMM yyyy")));
    }

    private void dayLabelClicked(Event event) {
        int day = Integer.parseInt(((Label) event.getSource()).getText().trim());
        LocalDate date = LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonthValue(), day);
        if(!selectedDates.contains(date)) {
            if(highlightedDates == null || highlightedDates.contains(date))
            {
                selectedDates.add(date);
                greenDay((Label) event.getSource());
            }
        }
        else {
            selectedDates.remove(date);
            if(highlightedDates != null && highlightedDates.contains(date))
                yellowDay((Label) event.getSource());
            else
                redDay((Label) event.getSource());
        }
    }

    private void redDay(Label dayLabel)
    {
        dayLabel.setStyle("-fx-background-color: pink; -fx-padding: 10px;");
    }

    private void greenDay(Label dayLabel)
    {
        dayLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px;");
    }

    private void yellowDay(Label dayLabel)
    {
        dayLabel.setStyle("-fx-background-color: lightyellow; -fx-padding: 10px;");
    }
}
