package com.RobotArm;

import com.RobotArm.business.CapteurContact;
import com.RobotArm.business.CapteurCouleur;
import com.RobotArm.business.CapteurPince;
import com.RobotArm.business.Moteur;
import com.RobotArm.controller.Controller;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.interfaces.IPilotage;
import com.RobotArm.persistance.JsonPersistance;
import com.RobotArm.pilotage.WifiListener;

import java.io.IOException;


public class RobotArm {
    private static IPilotage pilotage;

    private static final String FICHIER_GAMMES = "gammes.json";
    private static final String FICHIER_USERS = "users.json";
    public static void main(String[] args) {
        System.out.println("Debut du programme");

        Controller controller;
        try {
            // Création et instanciation du contrôleur et des différents modules
            InitHardware();
            IPersistance persistance = new JsonPersistance(FICHIER_GAMMES, FICHIER_USERS);

            pilotage = new WifiListener();
            controller = new Controller(persistance, pilotage);
            pilotage.ajoutListener(controller);

            controller.start();
        } catch (IOException e) {
            System.out.println("Une erreur inconnue est survenue, impossible de démarrer !");
            e.printStackTrace();
        }

        pilotage.fermerConnexion();

        pilotage = null;
        System.gc();
        System.out.println("Fin du programme");
    }


    private static void InitHardware() {
        // Configuration des moteurs et des capteurs.
        //TODO Les valeurs sont écrites en dur, et devraient être plutôt écrites dans un fichier externe.
        float vitesse = 120;
        int acceleration = 360;
        CapteurContact.initCapteur('1');
        CapteurCouleur.initCapteur('3');
        CapteurPince.initCapteur();

        Moteur.initMoteur('A', 2, CapteurPince.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Pince
        Moteur.initMoteur('B', 5, CapteurContact.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Rotation
        Moteur.initMoteur('C', 3, CapteurCouleur.getInstance(), vitesse, acceleration, Moteur.SENS_NEGATIF); // Levée
    }
}
