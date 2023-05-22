package com.RobotArm.exception;

public class WrongTypeActionException extends Exception {
    @Override
    public String getMessage() {
        return "L'action demandée est incorrecte.";
    }
}
