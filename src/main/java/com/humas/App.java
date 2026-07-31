package com.humas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Sistem Humas - NetBeans JavaFX Ready!");
        Scene scene = new Scene(new StackPane(label), 400, 200);
        
        stage.setTitle("Aplikasi SIM Humas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}