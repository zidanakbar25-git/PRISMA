package com.humas.view;

import atlantafx.base.theme.Styles;
import com.humas.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private LoginController controller = new LoginController();

    public void start(Stage stage) {
        // Header
        Label lblTitle = new Label("PRISMA HUMAS");
        lblTitle.getStyleClass().add(Styles.TITLE_2);

        Label lblSub = new Label("Silakan masuk menggunakan akun Anda");
        lblSub.getStyleClass().add(Styles.TEXT_MUTED);

        // Input Form
        Label lblUser = new Label("Username");
        lblUser.getStyleClass().add(Styles.TEXT_BOLD);
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Masukkan username");

        Label lblPass = new Label("Password");
        lblPass.getStyleClass().add(Styles.TEXT_BOLD);
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan password");

        Label lblMessage = new Label();
        lblMessage.getStyleClass().add(Styles.DANGER);

        // Tombol Login
        Button btnLogin = new Button("Masuk");
        btnLogin.getStyleClass().addAll(Styles.ACCENT, Styles.LARGE);
        btnLogin.setMaxWidth(Double.MAX_VALUE);

        VBox userBox = new VBox(5, lblUser, txtUsername);
        VBox passBox = new VBox(5, lblPass, txtPassword);

        // Container Card
        VBox card = new VBox(15, lblTitle, lblSub, userBox, passBox, btnLogin, lblMessage);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(30));
        card.setMaxWidth(380);
        card.getStyleClass().add(Styles.ELEVATED_1);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Event Tombol -> Serahkan Logika ke Controller
        btnLogin.setOnAction(e -> {
            String errorMsg = controller.handleLogin(
                txtUsername.getText().trim(), 
                txtPassword.getText().trim(), 
                stage
            );

            if (errorMsg != null) {
                lblMessage.setText(errorMsg);
            }
        });

        Scene scene = new Scene(root, 450, 500);
        stage.setTitle("PRISMA - Login");
        stage.setScene(scene);
        stage.show();
    }
}