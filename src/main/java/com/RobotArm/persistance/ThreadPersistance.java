package com.RobotArm.persistance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection; // Ajouté
import java.sql.DriverManager; // Ajouté
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Ajouté
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.RobotArm.*;
import com.RobotArm.business.*;
import com.RobotArm.interfaces.*;

public class ThreadPersistance implements IPersistance {
	private ExecutorService execService;
	private Future execThread; // Ajouté
	private Connection conn;
	static String cheminBase = "/etc/base.db"; // Ajouté
	static String cheminInitScript = "/etc/"; // Ajouté

	public ThreadPersistance() throws IOException, SQLException {
		execService = Executors.newSingleThreadExecutor();
		initBase();
	}

	private void initBase() throws IOException, SQLException // Ajouté
	{
		Connexion();
		if (new File(ThreadPersistance.cheminBase).exists()) {
			creerBase();
		}
	}

	private void Connexion() {
		try {
			this.conn = DriverManager.getConnection(cheminBase);
		} catch (Exception e) {
			// Si la base de données n'est pas joignable, le programme quitte

			System.exit(1);
		}

	}

	private void creerBase() throws IOException // Ajouté
			, SQLException {
		if (conn != null) {
			File fichierInit = new File(this.cheminBase);
			String sqlInit = new

			String(Files.readAllBytes(Paths.get(this.cheminBase)));
			Statement execQuery = conn.createStatement();
			execQuery.executeQuery(sqlInit);
		}
	}

	public void creerGamme(Gamme g) throws SQLException {
		String gammeQuery = "INSERT INTO Gamme(id, description VALUES(?, ?);";

		PreparedStatement execGammeQuery = conn.prepareStatement(gammeQuery);
		execGammeQuery.setString(1, g.getId());
		execGammeQuery.setString(2, g.getDescription());
		execGammeQuery.executeQuery();
		for (Operation o : g.getListeOperations()) {
			String opeQuery = "INSERT INTO Operation(id, description, fkGamme) VALUES(?, ?, ?);";

			PreparedStatement execOpeQuery = conn.prepareStatement(opeQuery);

			execOpeQuery.setString(1, String.valueOf(o.getId()));
			execOpeQuery.setString(2, o.getDescription());
			execOpeQuery.setString(3, g.getId());
			execOpeQuery.executeQuery();

			for (Tache t : o.getListeTaches()) {
				String tacheQuery = "INSERT INTO Tache(id, description, fkOpe, fkAction) VALUES(?, ?, ?, ?);";
				PreparedStatement execTacheQuery = conn.prepareStatement(tacheQuery);

				execTacheQuery.setString(1, String.valueOf(t.getId()));
				execTacheQuery.setString(2, t.getDescription());
				execTacheQuery.setString(3, String.valueOf(o.getId()));
				execTacheQuery.setString(4, t.getTypeAction() == Tache.TypeAction.Attendre ? "Attendre" : "Tourner");
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
		PreparedStatement execGammeQuery =

				conn.prepareStatement(gammeQuery);

		execGammeQuery.setString(1, id);
		execGammeQuery.executeQuery();
	}

	public HashMap<String, Gamme> recupererGammes() throws SQLException {
		HashMap<String, Gamme> listeGammes = new HashMap<String, Gamme>();
		String getGammesQuery = "SELECT * FROM Gamme;";

		PreparedStatement execGammesQuery = conn.prepareStatement(getGammesQuery);
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

	private Gamme creerGammeDepuisResultat(ResultSet gammes) throws Exception
	{
		Gamme g = new Gamme(gammes.getString("id"),

				gammes.getString("description"));

		String getOpeQuery = "SELECT * FROM Operation WHERE fkGamme = ?;";

		PreparedStatement execGetOpeQuery = conn.prepareStatement(getOpeQuery);

		execGetOpeQuery.setString(1, g.getId());
		ResultSet opes = execGetOpeQuery.executeQuery();
		while (opes.next()) {
			Operation o = new Operation(Integer.parseInt(opes.getString("id")), opes.getString("description"));

			String getTachesQuery = "SELECT * FROM Tache WHERE fkOpe = ?;";

			PreparedStatement execGetTachesQuery = conn.prepareStatement(getTachesQuery);

			execGetTachesQuery.setString(1, String.valueOf(o.getId()));
			ResultSet taches = execGetTachesQuery.executeQuery();
			while (taches.next()) {
				String getActionQuery = "SELECT * FROM TypeAction WHERE type = ?;";

				PreparedStatement execGetActionQuery = conn.prepareStatement(getTachesQuery);

				execGetActionQuery.setString(1, taches.getString("typeAction"));

				ResultSet action = execGetActionQuery.executeQuery();
				Tache t = new Tache(Integer.parseInt(taches.getString("id")), taches.getString("description"),
						taches.getString("typeAction") == "Attendre" ? Tache.TypeAction.Attendre : Tache.TypeAction.Tourner,
						action.getInt("valeur"),
						action.getString("portMoteur").charAt(0));
				o.AjouterTache(t);
			}
			g.AjouterOperation(o);
		}
		return g;
	}

	public Gamme recupererGammeDefaut() throws SQLException {
		String getGammeQuery = "SELECT * FROM Gamme WHERE id=0 LIMIT 1;";

		PreparedStatement execGammeQuery = conn.prepareStatement(getGammeQuery);
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

		PreparedStatement execRapportQuery = conn.prepareStatement(rapportQuery);
		execRapportQuery.setString(1, log);

		execRapportQuery.executeQuery();
	}

	public ArrayList<String> recupererLogs() throws SQLException {
		ArrayList<String> listeLogs = new ArrayList<String>();
		String getLogsQuery = "SELECT * FROM RapportPanne;";
		PreparedStatement execGetLogsQuery = conn.prepareStatement(getLogsQuery);
		ResultSet logs = execGetLogsQuery.executeQuery();
		while (logs.next()) {
			listeLogs.add(logs.getString("erreur"));
		}
		return listeLogs;
	}

	public HashMap<String, String> getConfig(String nomConfig) throws SQLException {
		HashMap<String, String> listConfig = new HashMap<String, String>();
		String getConfigQuery = "SELECT * FROM Config;";
		PreparedStatement execGetConfigQuery = conn.prepareStatement(getConfigQuery);
		ResultSet configs = execGetConfigQuery.executeQuery();

		while (configs.next()) {
			listConfig.put(configs.getString("nom"), configs.getString("valeur"));
		}
		return listConfig;

	}

	public void creerCompte(String login, String pwd) throws SQLException {
		String userQuery = "INSERT INTO Utilisateur(nom, passwd) VALUES(?, ?)";

		PreparedStatement execUserQuery = conn.prepareStatement(userQuery);
		execUserQuery.setString(1, login);
		execUserQuery.setString(2, pwd);
		execUserQuery.executeQuery();
	}

	public void supprimerCompte(String login) throws SQLException {
		String userQuery = "DELETE FROM Utilisateur WHERE nom = ?;";
		PreparedStatement execUserQuery = conn.prepareStatement(userQuery);
		execUserQuery.setString(1, login);
		execUserQuery.executeQuery();
	}
}