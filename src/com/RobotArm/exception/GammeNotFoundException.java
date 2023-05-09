package com.RobotArm.exception;

public class GammeNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Il n'y a pas de gamme pour cet identifiant.";
    }
}
