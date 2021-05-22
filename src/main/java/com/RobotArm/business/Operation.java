package com.RobotArm.business;

import java.util.ArrayList;

public class Operation
{
	private int id;
	private String description;
	ArrayList<Tache> listeTaches;
	
	
	public Operation()
	{
		listeTaches = new ArrayList<Tache>();
	}
	
	
	public Operation(int id, String description) {
		this.id = id;
		this.description = description;
		this.listeTaches = new ArrayList<Tache>();
	}


	public void AjouterTache(Tache t)
	{
		listeTaches.add(t);		
	}
	
	
	public void SupprimerTache(Tache t)
	{
		listeTaches.remove(t);		
	}
	
	
	public void executer()
	{
		for(Tache t : this.listeTaches)
		{
			t.executer();
		}
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<Tache> getListeTaches()
	{
		return this.listeTaches;
	}
}