class Tache
{
	enum TypeAction {Attendre, Tourner}
	TypeAction typeAction;
	Int valeur;
	Moteur moteur;
	
	
	public Tache(TypeAction ta, Int v)
	{
		this.typeAction = ta;
		this.valeur = v;
	}
	
	
	public executer()
	{
		switch(typeAction)
		case Attendre:
			sleep(this.valeur);
			break;
		case Tourner:
			moteur.tourner(this.valeur);
			break;			
	}
}