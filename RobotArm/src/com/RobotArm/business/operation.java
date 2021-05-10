class Operations
{
	ArrayList<Tache> ListeTaches;
	
	
	public Operation()
	{
		ListeTaches = new ArrayList<Tache>();
	}
	
	
	public void AjouterTache(Tache t)
	{
		ListeTaches.Ajouter(o);		
	}
	
	
	public void SupprimerTache(Tache t)
	{
		ListeTaches.remove(o);		
	}
	
	
	public void executer()
	{
		for(Tache t : listeTaches)
		{
			t.executer();
		}
	}
}