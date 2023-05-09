package com.RobotArm.exception;

public class UserNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Impossible de trouver l'utilisateur.";
    }
}
