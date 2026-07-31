package com.humas.util;

import com.humas.model.User;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void setSession(User user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public static void clearSession() {
        instance = null;
    }
}