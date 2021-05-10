interface IPersistance
{
	public void creerGamme(Gamme g);

	public void modifierGamme(Gamme g);

	public void supprimerGamme(String id);

	public HashMap<Gamme> recupererGammes();

	public Gamme recupererGammeDefaut();

	public void sauverLog(String log);

	public ArrayList<String> recupererLogs();

	public String getConfig(String nomConfig);

	public void creerCompte(String login, String pwd);

	public void supprimerCompte(String login);
}