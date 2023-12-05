package com.example.groupproject;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Calendar {
    private YearMonth selectedMonth;
    private LocalDate selectedDate;
    private Label selectedDateLabel;

    private ArrayList<LocalDate> selectedDatesList;
    private ArrayList<ArrayList<Integer>> selectedTimesList;
    private ArrayList<LocalDate> highlightedDates;
    private ArrayList<ArrayList<Integer>> highlightedTimes;

    @FXML
    private GridPane calendarGrid;
    @FXML
    private PieChart clock;
    @FXML
    private Label monthYearLabel;
    private boolean AM;

    public Calendar(GridPane cal, PieChart clock, Label moye) // for make page
    {
        this(moye, cal, clock);
        updateCalendar();
        updateClock();
    }

    public Calendar(GridPane cal, PieChart clock, Label moye, ArrayList<LocalDate> highDates, ArrayList<ArrayList<Integer>> highTimes) // for join page
    {
        this(moye, cal, clock);
        highlightedDates = highDates;
        highlightedTimes = highTimes;
        updateCalendar();
        updateClock();
    }

    private Calendar(Label moye, GridPane cal, PieChart clo)
    {
        calendarGrid = cal;
        clock = clo;
        monthYearLabel = moye;
        selectedMonth = YearMonth.now();
        selectedDatesList = new ArrayList<>();
        selectedTimesList = new ArrayList<>();
        selectedDate = null;
        clock.setMinSize(200, 200);
        clock.setMaxSize(200, 200);
        clock.setLabelLineLength(0);
        clock.setStartAngle(60);
        AM = false;
    }

    public void prevMonth()
    {
        selectedMonth = selectedMonth.minusMonths(1);
        updateCalendar();
        updateClock();
    }

    public void nextMonth()
    {
        selectedMonth = selectedMonth.plusMonths(1);
        updateCalendar();
        updateClock();
    }

    public void switchToAM()
    {
        AM = true;
        updateClock();
    }

    public void switchToPM()
    {
        AM = false;
        updateClock();
    }

    public boolean getAM()
    {
        return AM;
    }

    private void updateCalendar()
    {
        calendarGrid.getChildren().clear();
        selectedDate = null;

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
            if(selectedDatesList.contains(thisDay))
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
        // variables
        Label dayLabel = (Label) event.getSource();
        int day = Integer.parseInt(dayLabel.getText().trim());
        LocalDate date = LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonthValue(), day);

        if(!selectedDatesList.contains(date)) {
            if(highlightedDates == null || highlightedDates.contains(date))
            { // red day; add
                if(selectedDateLabel != null)
                    greenDay(selectedDateLabel);
                selectedDate = date;
                selectedDateLabel = dayLabel;
                selectedDatesList.add(date);
                selectedTimesList.add(new ArrayList<>());
                highlightDay(dayLabel);
            }
            else { // deselect
                if(selectedDateLabel != null)
                {
                    greenDay(selectedDateLabel);
                    selectedDate = null;
                    selectedDateLabel = null;
                }
            }
        }
        else {
            if(selectedDate == null || !selectedDate.equals(date))
            { // green day, but not our currently selected day; need to deselect previous day, and select the new day
                if(selectedDateLabel != null)
                    greenDay(selectedDateLabel);
                selectedDate = date;
                selectedDateLabel = dayLabel;
                highlightDay(dayLabel);
            }
            else
            { // green day, also the currently selected day; need to remove
                selectedDate = null;
                selectedDateLabel = null;
                selectedTimesList.remove(selectedDatesList.indexOf(date));
                selectedDatesList.remove(date);
                if(highlightedDates != null && highlightedDates.contains(date))
                    yellowDay((Label) event.getSource());
                else
                    redDay((Label) event.getSource());
            }
        }

        updateClock();
    }

    private void updateClock()
    {
        clock.getData().clear();

        int index = -1;
        if(selectedDate != null)
            index = selectedDatesList.indexOf(selectedDate);

        String amPM;
        if(AM)
            amPM = " AM";
        else
            amPM = " PM";
        for(int i = 1; i <= 12; i++)
        {
            PieChart.Data slice = new PieChart.Data(""+i, (double) 1 /12);
            clock.getData().add(slice);

            if(index == -1)
                grayDay(slice);
            else {
                slice.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clockTimeClicked(slice));
                int time = (i == 12) ? (0) : (i);
                if(!AM)
                    time += 12;
                if(selectedTimesList.get(index).contains(time))
                    greenSlice(slice);
                else if(highlightedTimes == null)
                    redSlice(slice);
                else
                {
                    int highIndex = highlightedDates.indexOf(selectedDate);
                    if(highIndex != -1 && highlightedTimes.get(highIndex).contains(time))
                        yellowSlice(slice);
                    else
                        redSlice(slice);
                }
            }
        }
    }

    private void clockTimeClicked(PieChart.Data slice)
    {
        int index;
        if(selectedDate != null)
            index = selectedDatesList.indexOf(selectedDate);
        else {
            System.out.println("???");
            return;
        }

        //System.out.println("index: " + index);
        //printDateAndTimes();

        Integer time = Integer.parseInt(slice.getName());
        if(time == 12)
            time = 0;
        if(!AM)
            time += 12;
        //System.out.println("time: " + time);
        if(selectedTimesList.get(index).contains(time))
        {
            selectedTimesList.get(index).remove(time);
            if(highlightedTimes == null)
                redSlice(slice);
            else {
                int highIndex = highlightedDates.indexOf(selectedDate);
                if(highIndex != -1 && highlightedTimes.get(highIndex).contains(time))
                    yellowSlice(slice);
                else
                    redSlice(slice);
            }
        }
        else {
            if(highlightedTimes == null)
            {
                selectedTimesList.get(index).add(time);
                greenSlice(slice);
            }
            else {
                int highIndex = highlightedDates.indexOf(selectedDate);
                if(highIndex != -1 && highlightedTimes.get(highIndex).contains(time))
                {
                    selectedTimesList.get(index).add(time);
                    greenSlice(slice);
                }
            }
        }
    }

    private void printDateAndTimes()
    {
        System.out.println("dates size: " + selectedDatesList.size());
        System.out.println("times size: " + selectedTimesList.size());
        for(int i = 0; i < selectedDatesList.size(); i++)
        {
            LocalDate date = selectedDatesList.get(i);
            System.out.print(date.toString() + " : ");
            ArrayList<Integer> times = selectedTimesList.get(i);
            for(Integer time : times)
            {
                System.out.print(time + " ");
            }
            System.out.println();
        }
    }

    private void highlightDay(Label dayLabel)
    {
        dayLabel.setStyle("-fx-background-color: darkseagreen; -fx-padding: 10px; -fx-font-style: italic");
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

    private void redSlice(PieChart.Data slice)
    {
        slice.getNode().setStyle("-fx-background-color: pink; -fx-padding: 10px;");
    }

    private void greenSlice(PieChart.Data slice)
    {
        slice.getNode().setStyle("-fx-background-color: lightgreen; -fx-padding: 10px;");
    }

    private void yellowSlice(PieChart.Data slice)
    {
        slice.getNode().setStyle("-fx-background-color: lightyellow; -fx-padding: 10px;");
    }

    private void grayDay(PieChart.Data slice)
    {
        slice.getNode().setStyle("-fx-background-color: lightgrey; -fx-padding: 10px;");
    }

    public Pair<ArrayList<LocalDate>, ArrayList<ArrayList<Integer>>> getCalendarData()
    {
        return new Pair<>(selectedDatesList, selectedTimesList);
    }
}
