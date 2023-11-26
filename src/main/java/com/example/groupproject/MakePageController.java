package com.example.groupproject;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MakePageController {
    public TextField eventNameTextField;
    public TextField eventIDTextField;

    public GridPane calendarGrid;
    public Label monthYearLabel;
    private Calendar cal;

    public void initialize()
    {
        cal = new Calendar(calendarGrid, monthYearLabel);
    }

    public void onLeftButtonClick() {
        cal.prevMonth();
    }

    public void onRightButtonClick() {
        cal.nextMonth();
    }

    public void onCreateButtonClick() throws IOException {
        Main.loadEndMakePage();
    }

    public void onNameTextFieldType() {
        /* TODO: ensure the ID is not already used; if it is, then change ID to ID+i, for example birthday2, or birthday3 */
        eventIDTextField.setText(eventNameTextField.getText());
    }
}
