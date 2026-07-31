package com.humas.controller;

import com.humas.util.UserSession;
import com.humas.view.LoginView;
import javafx.stage.Stage;

public class KabagDashboardController {

    public void handleLogout(Stage stage) {
        UserSession.clearSession();
        new LoginView().start(stage);
    }
}