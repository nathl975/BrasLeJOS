package com.RobotArm.interfaces;

import com.RobotArm.business.Gamme;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IPersistance {
 void creerGamme(Gamme paramGamme) throws SQLException;
 
 void modifierGamme(Gamme paramGamme) throws SQLException;
 
 void supprimerGamme(String paramString) throws SQLException;
 
 HashMap<String, Gamme> recupererGammes() throws SQLException;
 
 Gamme recupererGammeDefaut() throws SQLException;
 
 void sauverLog(String paramString) throws SQLException;
 
 ArrayList<String> recupererLogs() throws SQLException;
 
 String getConfig(String paramString) throws SQLException;
 
 void creerCompte(String paramString1, String paramString2) throws SQLException;
 
 void supprimerCompte(String paramString) throws SQLException;
}
