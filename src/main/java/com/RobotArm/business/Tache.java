package com.RobotArm.business;

import lejos.utility.Delay;

public class Tache {
	public enum TypeAction {
		Attendre, Tourner
	}

	private int id;
	private String description;
	private TypeAction typeAction;
	int valeur;
	Moteur moteur;

	public Tache(TypeAction ta, int v) {
		this.setTypeAction(ta);
		this.valeur = v;
	}

	public Tache(int id, String description, TypeAction typeAction, int valeur, char moteur) {
		// TODO Auto-generated constructor stub
	}

	public void executer() {
		switch (getTypeAction()) {
		case Attendre:
			Delay.msDelay(this.valeur);
			break;
		case Tourner:
			moteur.tourner(this.valeur);
			break;
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

	public TypeAction getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(TypeAction typeAction) {
		this.typeAction = typeAction;
	}
}