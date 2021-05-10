class Gamme
{
	String id;
	String description;
	ArrayList<Operation> listeOperations;
	
	
	public Gamme(String i, String d)
	{
		this.id = i;
		this.description = d;		
		listeOperations = new ArrayList<Operation>();
	}
	
	
	public void AjouterOperation(Operation o)
	{
		if(!listeOperations.contains(o))
			listeOperations.Ajouter(o);		
		else
			throw new Exception("Cette opération est déjà présente dans la gamme.");
	}
	
	
	public void SupprimerOperation(Operation o)
	{
		if(listeOperations.contains(o))
			listeOperations.remove(o);		
		else
			throw new Exception("Cette opération n'est pas présente dans la gamme.");
	}
	
	
	public void executer()
	{
		for(Operation o : listeOperation)
		{
			o.executer();
		}
	}
	
}