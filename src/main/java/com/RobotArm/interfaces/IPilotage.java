package com.RobotArm.interfaces;

import java.util.ArrayList;

public interface IPilotage {
  void afficherEtatSysteme();
  
  void afficherHistorique(ArrayList<String> paramArrayList);
  
  void ecouter();
  
  void ajoutListener(IPilote paramIPilote);
  
  void envoyerMessage(String paramString);
  
  void fermerConnexion();
  
  void stop();
}
