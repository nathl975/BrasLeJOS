package com.RobotArm.business;
 
import com.RobotArm.interfaces.ICapteur;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

/**
 * Classe représentant un moteur du robot
 *
 * @author Alvin
 */
public class Moteur {
    private final char port;
    private final double ratio;
    private final ICapteur capteur;
    private NXTRegulatedMotor NXTMotor;
    private int sensRotation; // Sens vers lesquels tourner pour que le moteur arrive en butée de son capteur

    private static Moteur A, B, C, D;
    public static int SENS_NEGATIF = -1, SENS_POSITIF = 1;

    /**
     * Constructeur privé pour les 4 instances uniques de la classe
     *
     * @param port         Port sur lequel le moteur à instancier est branché
     * @param ratio        Ratio de force entre le moteur et la fin de la chaîne d'engrenages
     * @param capteur      Capteur lié au moteur
     * @param vitesse      Vitesse du moteur
     * @param acceleration Accélération du moteur
     * @param sensRotation Sens de rotation du moteur, permet d'inverser le sens naturel pour que le moteur tourne vers son capteur avec un angle positif, et s'éloigne du capteur avec un angle négatif
     */
    private Moteur(char port, double ratio, ICapteur capteur, float vitesse, int acceleration, int sensRotation) {
        this.port = port;
        this.ratio = ratio;
        this.capteur = capteur;

        this.setSensRotation(sensRotation);

        switch (this.port) {
            case 'A':
                NXTMotor = Motor.A;
                NXTMotor.setStallThreshold(1, 50);
                break;
            case 'B':
                NXTMotor = Motor.B;
                NXTMotor.setStallThreshold(5, 250);
                break;
            case 'C':
                NXTMotor = Motor.C;
                NXTMotor.setStallThreshold(5, 250);
                break;
            case 'D':
                NXTMotor = Motor.D;
                NXTMotor.setStallThreshold(5, 250);
                break;
        }
        NXTMotor.setSpeed(vitesse);
        NXTMotor.setAcceleration(acceleration);
        NXTMotor.setStallThreshold(5, 250);
    }


    /**
     * Permet d'initialiser une instance d'un des 4 moteurs de la classe Singleton Moteur
     *
     * @param port         Port sur lequel le moteur à instancier est branché
     * @param ratio        Ratio de force entre le moteur et la fin de la chaîne d'engrenages
     * @param capteur      Capteur lié au moteur
     * @param vitesse      Vitesse du moteur
     * @param acceleration Accélération du moteur
     * @param sensRotation Sens de rotation du moteur, permet d'inverser le sens naturel pour que le moteur tourne vers son capteur avec un angle positif, et s'éloigne du capteur avec un angle négatif
     */
    public static void initMoteur(char port, float ratio, ICapteur capteur, float vitesse, int acceleration, int sensRotation) {
        switch (port) {
            case 'a':
            case 'A':
                if (A == null)
                    A = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
                break;
            case 'b':
            case 'B':
                if (B == null)
                    B = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
                break;
            case 'c':
            case 'C':
                if (C == null)
                    C = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
                break;
            case 'd':
            case 'D':
                if (D == null)
                    D = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
                break;
        }
    }

    /**
     * @param port Port du moteur
     * @return Instance correspondant au port donné
     */
    public static Moteur getInstance(char port) {
        switch (port) {
            case 'a':
            case 'A':
                return A;
            case 'b':
            case 'B':
                return B;
            case 'c':
            case 'C':
                return C;
            case 'd':
            case 'D':
                return D;
            default:
                return null;
        }
    }

    /**
     * Permet de faire tourner un moteur sur un angle
     *
     * @param degres Angle de rotation
     */
    public void tourner(int degres) {

        if (degres == 0)
            return;

        // Vrai si le moteur a commencé à tourner
        float sens = Math.signum(degres);
        System.out.printf("Moteur %s tourne sur %d%n", port, degres);
        // On vérifie tout d'abord si le moteur peut tourner
        // Conditions pour que moteur tourne :
        // Si le moteur doit tourner vers un angle positif, alors il tourne vers le capteur.
        // Dans ce cas, on vérifie si le capteur détecte le moteur en butée.
        if (this.capteur.getMesure() == 1 && sens > 0.0f) {
            System.out.printf("Moteur %s en butee%n", port);
            return;
        }
        // S'il peut tourner, alors on le fait tourner.
        this.NXTMotor.rotate((int) Math.round(degres * this.ratio * this.sensRotation), true);
        // Tant que les conditions pour s'arrêter ne sont pas remplies, on attend
        // Conditions pour s'arrêter :
        // - Le capteur lance un signal, et le sens de rotation est vers le capteur
        // - OU le moteur s'est arrêté de tourner naturellement
        // - OU le moteur est bloqué (stalled) et force pour tourner
        Delay.msDelay(25);
        while ((this.capteur.getMesure() == 0 || sens < 0.0f) && (NXTMotor.isMoving()) && !NXTMotor.isStalled()) ;
        this.NXTMotor.stop();
    }


    public void stop() {
        NXTMotor.stop();
    }


    private void setSensRotation(int sensRotation) {
        if (sensRotation != SENS_NEGATIF && sensRotation != SENS_POSITIF)
            sensRotation = SENS_POSITIF;
        this.sensRotation = sensRotation;
    }

    public char getPort() {
        return this.port;
    }
}
