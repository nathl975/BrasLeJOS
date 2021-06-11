package com.RobotArm.business;

/**
 * Classe représentant un utilisateur
 * @author Alvin
 *
 */
public class Utilisateur {
	private String login;
	private String pwd;
	private boolean admin; // Vrai si Administrateur, faux sinon
	
	
	public Utilisateur(String login, String pwd, boolean isAdmin)
	{
		this.login = login;
		this.pwd = pwd;
		this.admin = isAdmin;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	
	public void setLogin(String login)
	{
		this.login = login;
	}
	
	
	public String getPwd()
	{
		return pwd;
	}
	
	
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	
	
	public boolean isAdmin() {
		return admin;
	}
	
	
	public void setAdmin(boolean admin)
	{
		this.admin = admin;
	}
}
