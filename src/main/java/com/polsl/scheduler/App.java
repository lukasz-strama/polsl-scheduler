package com.polsl.scheduler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.polsl.scheduler.db.DatabaseConnector;


/** 
 * Main application class for the POLSL Scheduler JavaFX application.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseConnector.initDatabase();
        
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.setTitle("POLSL Scheduler");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}