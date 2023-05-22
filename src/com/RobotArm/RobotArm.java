package com.RobotArm;

import com.RobotArm.adapter.JsonAdapter;
import com.RobotArm.builder.BrasArticuleBuilder;
import com.RobotArm.business.BrasArticule;
import com.RobotArm.business.Gamme;
import com.RobotArm.controller.Controller;
import com.RobotArm.interfaces.IAdapter;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.interfaces.IPilotage;
import com.RobotArm.persistance.JsonPersistance;
import com.RobotArm.pilotage.WifiListener;

import java.io.File;
import java.io.IOException;


public class RobotArm {

    private static final String FICHIER_GAMMES = "gammes.json";
    private static final String FICHIER_USERS = "users.json";
    public static void main(String[] args) {
        System.out.println("Debut du programme");

        Controller controller;
        BrasArticule brasArticule = InitHardware();
        IAdapter<Gamme> gammeAdapter = new JsonAdapter<>();
        IPilotage pilotage = new WifiListener();

        try {
            // Ici, on crée un objet File pour vérifier si le fichier existe, sinon on le crée.
            File fichierGammes = new File(FICHIER_GAMMES);
            if (fichierGammes.createNewFile()) {
                System.out.println("Fichier créé");
            }

            // Création et instanciation du contrôleur et des différents modules
            IPersistance persistance = new JsonPersistance(FICHIER_GAMMES, FICHIER_USERS);

            controller = new Controller(persistance, pilotage, gammeAdapter, brasArticule);
            pilotage.ajoutListener(controller);

            controller.start();
        } catch (IOException e) {
            System.out.println("Une erreur inconnue est survenue, impossible de démarrer !");
            e.printStackTrace();
        }

        pilotage.fermerConnexion();

        System.gc();
        System.out.println("Fin du programme");
    }


    private static BrasArticule InitHardware() {
        BrasArticuleBuilder builder = new BrasArticuleBuilder()
                .withPortCapteurContact('1')
                .withPortCapteurCouleur('3')
                .withPortMoteurGrab('A')
                .withPortMoteurTurn('B')
                .withPortMoteurLift('C');

        return builder.build();
    }
}
