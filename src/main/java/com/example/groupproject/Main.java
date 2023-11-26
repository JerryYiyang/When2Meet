package com.example.groupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    static private Stage stage;

    static final private int width = 660;
    static final private int height = 460;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        stage.setTitle("When2Meet");
        loadStartPage();
    }

    public static void makeErrorPopup(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("When2Meet");
        alert.setHeaderText("Error: " + s);
        alert.show();
    }

    public static void loadStartPage() throws IOException {
        loadPage("startpage-view.fxml");
    }

    public static void loadJoinPage() throws IOException {
        loadPage("joinpage-view.fxml");
    }

    public static void loadMakePage() throws IOException {
        loadPage("makepage-view.fxml");
    }

    public static void loadEndJoinPage() throws IOException {
        loadPage("endjoinpage-view.fxml");
    }

    public static void loadEndMakePage() throws IOException {
        loadPage("endmakepage-view.fxml");
    }

    private static void loadPage(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);
        stage.show();
    }
}