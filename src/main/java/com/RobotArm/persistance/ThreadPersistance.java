 package com.RobotArm.persistance;
 
 import com.RobotArm.business.Gamme;
 import com.RobotArm.business.Operation;
 import com.RobotArm.business.Tache;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.interfaces.IPersistance;
 import java.io.File;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;
 import lejos.hardware.Wifi;
 
 
 public class ThreadPersistance
   implements IPersistance
 {
   private ExecutorService execService;
   private Future execThread;
   private Connection conn;
   static String jdbcAdapter = "jdbc:sqlite:";
   static String cheminBase = "base.db";
   static String cheminScriptInit = "./init.sql";
   static HashMap<String, String> configMap;
   
   public ThreadPersistance() throws IOException, SQLException {
     this.execService = Executors.newSingleThreadExecutor();
     initBase();
   }
 
   
   private void initBase() throws IOException, SQLException {
     Connexion();
     creerBase();
     loadConfig();
   }
   
   private void Connexion() throws SQLException {
     this.conn = DriverManager.getConnection(jdbcAdapter + cheminBase);
   }
 
   
   private void creerBase() throws IOException, SQLException {
     if (this.conn != null) {
       File sqlFile = new File(getClass().getClassLoader().getResource(cheminScriptInit).getFile());
       byte[] fileContent = Files.readAllBytes(sqlFile.toPath());
       String sqlInit = new String(fileContent);
       Statement execQuery = this.conn.createStatement();
       execQuery.execute(sqlInit);
     }  Wifi w = new Wifi();
   }
   
   public void creerGamme(Gamme g) throws SQLException {
     String gammeQuery = "INSERT INTO Gamme(id, description VALUES(?, ?);";
     
     PreparedStatement execGammeQuery = this.conn.prepareStatement(gammeQuery);
     execGammeQuery.setString(1, g.getId());
     execGammeQuery.setString(2, g.getDescription());
     execGammeQuery.executeQuery();
     for (Operation o : g.getListeOperations()) {
       String opeQuery = "INSERT INTO Operation(id, description, fkGamme) VALUES(?, ?, ?);";
       
       PreparedStatement execOpeQuery = this.conn.prepareStatement(opeQuery);
       
       execOpeQuery.setString(1, String.valueOf(o.getId()));
       execOpeQuery.setString(2, o.getDescription());
       execOpeQuery.setString(3, g.getId());
       execOpeQuery.executeQuery();
       
       for (Tache t : o.getListeTaches()) {
         String tacheQuery = "INSERT INTO Tache(id, description, fkOpe, fkAction) VALUES(?, ?, ?, ?);";
         PreparedStatement execTacheQuery = this.conn.prepareStatement(tacheQuery);
         
         execTacheQuery.setString(1, String.valueOf(t.getId()));
         execTacheQuery.setString(2, t.getDescription());
         execTacheQuery.setString(3, String.valueOf(o.getId()));
         execTacheQuery.setString(4, (t.getTypeAction() == Tache.TypeAction.Attendre) ? "Attendre" : "Tourner");
         execTacheQuery.executeQuery();
       } 
     } 
   }
   
   public void modifierGamme(Gamme g) throws SQLException {
     supprimerGamme(g.getId());
     creerGamme(g);
   }
   
   public void supprimerGamme(String id) throws SQLException {
     String gammeQuery = "DELETE FROM Gamme WHERE id = ?;";
 
     
     PreparedStatement execGammeQuery = this.conn.prepareStatement(gammeQuery);
     
     execGammeQuery.setString(1, id);
     execGammeQuery.executeQuery();
   }
   
   public HashMap<String, Gamme> recupererGammes() throws SQLException {
     HashMap<String, Gamme> listeGammes = new HashMap<>();
     String getGammesQuery = "SELECT * FROM Gamme;";
     
     PreparedStatement execGammesQuery = this.conn.prepareStatement(getGammesQuery);
     ResultSet gammes = execGammesQuery.executeQuery();
     
     while (gammes.next()) {
       try {
         listeGammes.put(String.valueOf(gammes.getInt("id")), creerGammeDepuisResultat(gammes));
       } catch (Exception e) {
         sauverLog(e.getMessage());
         return null;
       } 
     } 
     return listeGammes;
   }
 
 
 
   
   private Gamme creerGammeDepuisResultat(ResultSet gammes) throws Exception {
     Gamme g = new Gamme(gammes.getString("id"), gammes.getString("description"));
     
     String getOpeQuery = "SELECT * FROM Operation WHERE fkGamme = ?;";
     
     PreparedStatement execGetOpeQuery = this.conn.prepareStatement(getOpeQuery);
     
     execGetOpeQuery.setString(1, g.getId());
     ResultSet opes = execGetOpeQuery.executeQuery();
     while (opes.next()) {
       Operation o = new Operation(opes.getString("id"), opes.getString("description"));
       
       String getTachesQuery = "SELECT * FROM Tache WHERE fkOpe = ?;";
       
       PreparedStatement execGetTachesQuery = this.conn.prepareStatement(getTachesQuery);
       
       execGetTachesQuery.setString(1, String.valueOf(o.getId()));
       ResultSet taches = execGetTachesQuery.executeQuery();
       while (taches.next()) {
         String getActionQuery = "SELECT * FROM TypeAction WHERE type = ?;";
         
         PreparedStatement execGetActionQuery = this.conn.prepareStatement(getTachesQuery);
         
         execGetActionQuery.setString(1, taches.getString("typeAction"));
         
         ResultSet action = execGetActionQuery.executeQuery();
 
 
         
         //Tache t = new Tache(taches.getString("id"), taches.getString("description"), (taches.getString("typeAction") == "Attendre") ? Tache.TypeAction.Attendre : Tache.TypeAction.Tourner, action.getInt("valeur"), action.getString("portMoteur").charAt(0));
         //o.AjouterTache(t);
       } 
       g.AjouterOperation(o);
     } 
     return g;
   }
   
   public Gamme recupererGammeDefaut() throws SQLException {
     String getGammeQuery = "SELECT * FROM Gamme WHERE id=0 LIMIT 1;";
     
     PreparedStatement execGammeQuery = this.conn.prepareStatement(getGammeQuery);
     ResultSet gammes = execGammeQuery.executeQuery();
     try {
       return creerGammeDepuisResultat(gammes);
     } catch (Exception e) {
       sauverLog(e.getMessage());
       return null;
     } 
   }
   
   public void sauverLog(String log) throws SQLException {
     String rapportQuery = "INSERT INTO RapportPanne(date, erreur) VALUES(SELECT(date('now'), ?)";
     
     PreparedStatement execRapportQuery = this.conn.prepareStatement(rapportQuery);
     execRapportQuery.setString(1, log);
     
     execRapportQuery.executeQuery();
   }
   
   public ArrayList<String> recupererLogs() throws SQLException {
     ArrayList<String> listeLogs = new ArrayList<>();
     String getLogsQuery = "SELECT * FROM RapportPanne;";
     PreparedStatement execGetLogsQuery = this.conn.prepareStatement(getLogsQuery);
     ResultSet logs = execGetLogsQuery.executeQuery();
     while (logs.next()) {
       listeLogs.add(logs.getString("erreur"));
     }
     return listeLogs;
   }
   
   private HashMap<String, String> loadConfig() throws SQLException {
     HashMap<String, String> listConfig = new HashMap<>();
     String getConfigQuery = "SELECT * FROM Config;";
     PreparedStatement execGetConfigQuery = this.conn.prepareStatement(getConfigQuery);
     ResultSet configs = execGetConfigQuery.executeQuery();
     
     while (configs.next()) {
       listConfig.put(configs.getString("nom"), configs.getString("valeur"));
     }
     return listConfig;
   }
   
   public void creerCompte(String login, String pwd) throws SQLException {
     String userQuery = "INSERT INTO Utilisateur(nom, passwd) VALUES(?, ?)";
     
     PreparedStatement execUserQuery = this.conn.prepareStatement(userQuery);
     execUserQuery.setString(1, login);
     execUserQuery.setString(2, pwd);
     execUserQuery.executeQuery();
   }
   
   public void supprimerCompte(String login) throws SQLException {
     String userQuery = "DELETE FROM Utilisateur WHERE nom = ?;";
     PreparedStatement execUserQuery = this.conn.prepareStatement(userQuery);
     execUserQuery.setString(1, login);
     execUserQuery.executeQuery();
   }
 
   
   public String getConfig(String nomConfig) throws SQLException {
     return configMap.get(nomConfig);
   }


	@Override
	public Utilisateur trouverCompte(String l, String p) {
		return null;
	}
 }

