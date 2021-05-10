interface IPilotage
{
	public void afficherEtatSysteme();
	
	public void afficherHistorique(ArrayList<String> rapports);
	
	public void envoyerMessage(String message);
}