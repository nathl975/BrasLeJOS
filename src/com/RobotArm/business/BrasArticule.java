package com.RobotArm.business;

public class BrasArticule {
    // The field must be declared volatile so that double check lock would work correctly.
    private static volatile BrasArticule instance;

    public Moteur moteurGrab;
    public Moteur moteurLift;
    public Moteur moteurTurn;

    private BrasArticule(char portMoteurGrab, char portMoteurLift, char portMoteurTurn, char portCapteurContact, char portCapteurCouleur, int vitesse, int acceleration) {
        CapteurContact.initCapteur(portCapteurContact);
        CapteurCouleur.initCapteur(portCapteurCouleur);
        CapteurPince.initCapteur();

        Moteur.initMoteur(portMoteurGrab, 2, CapteurPince.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Pince
        Moteur.initMoteur(portMoteurLift, 3, CapteurCouleur.getInstance(), vitesse, acceleration, Moteur.SENS_NEGATIF); // Levée
        Moteur.initMoteur(portMoteurTurn, 5, CapteurContact.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Rotation

        this.moteurGrab = Moteur.getInstance(portMoteurGrab);
        this.moteurLift = Moteur.getInstance(portMoteurLift);
        this.moteurTurn = Moteur.getInstance(portMoteurTurn);
    }

    public static BrasArticule getInstance(char portMoteurGrab, char portMoteurLift, char portMoteurTurn, char portCapteurContact, char portCapteurCouleur, int vitesse, int acceleration) {
        // The approach taken here is called double-checked locking (DCL). It
        // exists to prevent race condition between multiple threads that may
        // attempt to get singleton instance at the same time, creating separate
        // instances as a result.
        //
        // It may seem that having the `result` variable here is completely
        // pointless. There is, however, a very important caveat when
        // implementing double-checked locking in Java, which is solved by
        // introducing this local variable.
        //
        // You can read more info DCL issues in Java here:
        // https://refactoring.guru/java-dcl-issue
        BrasArticule result = instance;
        if (result != null) {
            return result;
        }
        synchronized(BrasArticule.class) {
            if (instance == null) {
                instance = new BrasArticule(portMoteurGrab, portMoteurLift, portMoteurTurn, portCapteurContact, portCapteurCouleur, vitesse, acceleration);
            }
            return instance;
        }
    }

    /**
     * Positionne tous les moteurs en butée.
     */
    public void resetPositionMoteurs() {
        try {
            this.moteurGrab.tourner(360);
            this.moteurGrab.stop();

            this.moteurLift.tourner(360);
            this.moteurLift.stop();

            this.moteurTurn.tourner(360);
            this.moteurTurn.stop();

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Arrête immédiatement tous les moteurs
     */
    public void stopAllMoteurs() {
        this.moteurLift.stop();
        this.moteurTurn.stop();
        this.moteurGrab.stop();
    }
}
