package com.RobotArm.business;

import java.util.ArrayList;
import com.RobotArm.business.*;

public class Gamme
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
	
	
	public Gamme() {
		
	}


	public void AjouterOperation(Operation o) throws Exception
	{
		if(!listeOperations.contains(o))
			listeOperations.add(o);		
		else
			throw new Exception("Cette opération est déjà présente dans la gamme.");
	}
	
	
	public void SupprimerOperation(Operation o) throws Exception
	{
		if(listeOperations.contains(o))
			listeOperations.remove(o);		
		else
			throw new Exception("Cette opération n'est pas présente dans la gamme.");
	}
	
	
	public void executer()
	{
		for(Operation o : this.listeOperations)
		{
			o.executer();
		}
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public ArrayList<Operation> getListeOperations() {
		return listeOperations;
	}
	
	
	
	
}