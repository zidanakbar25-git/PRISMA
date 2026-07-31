package com.humas;

import atlantafx.base.theme.PrimerLight;
import com.humas.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Terapkan Tema AtlantaFX secara Global
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // 2. Tampilkan Halaman Login
        new LoginView().start(stage);
        
        String hashBaru = BCrypt.hashpw("123", BCrypt.gensalt());
System.out.println("=== HASH BCRYPT JAVA ===");
System.out.println(hashBaru);
System.out.println("=======================");
    }

    public static void main(String[] args) {
        launch(args);
    }
}