 package com.RobotArm;
 
 import com.RobotArm.controller.Controleur;
 import com.RobotArm.interfaces.IPersistance;
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;
 import com.RobotArm.persistance.DummyPersistance;
 import com.RobotArm.pilotage.WifiListener;
 import java.io.IOException;
 import java.sql.SQLException;
 import lejos.utility.Delay;

 
 public class RobotArm
 {
   private static IPersistance persistance;
   private static IPilotage pilotage;
   private static Controleur controleur;
   
   public static void main(String[] args) {
     System.out.println("Début du programme");
 
     
     try {
       persistance = (IPersistance)new DummyPersistance();
       pilotage = (IPilotage)new WifiListener();
       controleur = new Controleur(persistance, pilotage);
       pilotage.ajoutListener((IPilote)controleur);
       
       controleur.demarrer();
     }
     catch (IOException e) {
       System.out.println("Une erreur bloquane est survenue, impossible de démarrer !");
       e.printStackTrace();
     } catch (SQLException e) {
       System.out.println("Une erreur SQL bloquante est survenue, impossible de démarrer !");
       e.printStackTrace();
     } catch (Exception e) {
       System.out.println("Une erreur inconnue est survenue, impossible de démarrer !");
       e.printStackTrace();
     } 
     Delay.msDelay(3000L);
     pilotage.fermerConnexion();
     
     pilotage = null;
     persistance = null;
     controleur = null;
     System.gc();
     System.out.println("Fin du programme");
   }
 }
