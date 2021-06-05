package com.RobotArm.interfaces;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IPersistance {
  void creerGamme(Gamme paramGamme) throws SQLException;
  
  void modifierGamme(Gamme paramGamme) throws SQLException;
  
  void supprimerGamme(String paramString) throws Exception;
  
  HashMap<String, Gamme> recupererGammes() throws SQLException;
  
  Gamme recupererGammeDefaut() throws Exception;
  
  void sauverLog(String paramString) throws SQLException;
  
  ArrayList<String> recupererLogs() throws SQLException;
  
  String getConfig(String paramString) throws SQLException;
  
  void creerCompte(String paramString1, String paramString2) throws SQLException;

  Utilisateur trouverCompte(String l, String p);
  
  void supprimerCompte(String paramString) throws SQLException;
}
