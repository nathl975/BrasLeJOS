package com.RobotArm.exception;

import java.io.IOException;

public class UnableToReadGammesException extends IOException {
    @Override
    public String getMessage() {
        return "Il y a eu une erreur lors de la récupération des gammes.";
    }
}
