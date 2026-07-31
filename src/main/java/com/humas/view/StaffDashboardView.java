package com.humas.view;

import atlantafx.base.theme.Styles;
import com.humas.model.User;
import com.humas.util.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffDashboardView {

    public void start(Stage stage) {
        User user = UserSession.getInstance().getUser();

        Label lblTitle = new Label("DASHBOARD STAFF HUMAS");
        lblTitle.getStyleClass().add(Styles.TITLE_2);

        Label lblWelcome = new Label("Selamat Datang, " + (user != null ? user.getNama() : "Staff"));
        lblWelcome.getStyleClass().add(Styles.TEXT_BOLD);

        Label lblDesc = new Label("Akses review narasi berita, pembuatan draft, dan pengelolaan tugas harian.");
        lblDesc.getStyleClass().add(Styles.TEXT_MUTED);

        Button btnLogout = new Button("Logout");
        btnLogout.getStyleClass().addAll(Styles.DANGER);

        btnLogout.setOnAction(e -> {
            UserSession.clearSession();
            new LoginView().start(stage);
        });

        VBox card = new VBox(15, lblTitle, lblWelcome, lblDesc, btnLogout);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.getStyleClass().add(Styles.ELEVATED_1);
        card.setMaxWidth(500);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("PRISMA - Dashboard Staff");
        stage.setScene(scene);
        stage.show();
    }
}