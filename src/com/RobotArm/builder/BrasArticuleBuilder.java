package com.RobotArm.builder;

import com.RobotArm.business.BrasArticule;

public class BrasArticuleBuilder {
    private char portMoteurGrab;
    private char portMoteurLift;
    private char portMoteurTurn;
    private char portCapteurContact;
    private char portCapteurCouleur;
    private int vitesse;
    private int acceleration;

    public BrasArticuleBuilder() {
        // Valeurs par défaut
        this.vitesse = 120;
        this.acceleration = 360;
    }

    public BrasArticuleBuilder withPortMoteurGrab(char portMoteurGrab) {
        this.portMoteurGrab = portMoteurGrab;
        return this;
    }

    public BrasArticuleBuilder withPortMoteurLift(char portMoteurLift) {
        this.portMoteurLift = portMoteurLift;
        return this;
    }

    public BrasArticuleBuilder withPortMoteurTurn(char portMoteurTurn) {
        this.portMoteurTurn = portMoteurTurn;
        return this;
    }

    public BrasArticuleBuilder withPortCapteurContact(char portCapteurContact) {
        this.portCapteurContact = portCapteurContact;
        return this;
    }

    public BrasArticuleBuilder withPortCapteurCouleur(char portCapteurCouleur) {
        this.portCapteurCouleur = portCapteurCouleur;
        return this;
    }
    public BrasArticuleBuilder withVitesse(int vitesse) {
        this.vitesse = vitesse;
        return this;
    }

    public BrasArticuleBuilder withAcceleration(int acceleration) {
        this.acceleration = acceleration;
        return this;
    }

    public BrasArticule build() {
        return BrasArticule.getInstance(portMoteurGrab, portMoteurLift, portMoteurTurn, portCapteurContact, portCapteurCouleur,  vitesse, acceleration);
    }
}
