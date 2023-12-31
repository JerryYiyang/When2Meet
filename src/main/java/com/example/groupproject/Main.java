package com.example.groupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {

    static private Stage stage;

    static final private int width = 860;
    static final private int height = 460;

    private static DatabaseConnection connect = DatabaseConnection.getInstance();
    private static String currentEventID;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        stage.setTitle("When2Meet");
        stage.setOnCloseRequest(event -> {
            connect.clearDatabase();
            connect.closeConnection();
        });
        loadStartPage();
    }

    public static void makeErrorPopup(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("When2Meet");
        alert.setHeaderText("Error: " + s);
        alert.show();
    }

    public static void makeInfoPopup(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("When2Meet");
        alert.setHeaderText(s);
        alert.show();
    }

    public static String makeInputPopup(String s) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("When2Meet");
        dialog.setHeaderText(s);

        Optional<String> result = dialog.showAndWait();

        return result.orElse("");
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

    public static String getCurrentEventID()
    {
        return currentEventID;
    }

    public static void setCurrentEventID(String ID)
    {
        currentEventID = ID;
    }

}
