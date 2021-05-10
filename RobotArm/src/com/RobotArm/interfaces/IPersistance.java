package com.RobotArm.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.RobotArm.business.*;
public interface IPersistance
{
	public void creerGamme(Gamme g) throws SQLException;

	public void modifierGamme(Gamme g) throws SQLException;

	public void supprimerGamme(String id) throws SQLException;

	public HashMap<String, Gamme> recupererGammes() throws SQLException;

	public Gamme recupererGammeDefaut() throws SQLException;

	public void sauverLog(String log) throws SQLException;

	public ArrayList<String> recupererLogs() throws SQLException;

	public HashMap<String, String> getConfig(String nomConfig) throws SQLException;

	public void creerCompte(String login, String pwd) throws SQLException;

	public void supprimerCompte(String login) throws SQLException;
}